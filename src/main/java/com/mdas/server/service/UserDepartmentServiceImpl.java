package com.mdas.server.service;
// package com.mdas.server.service.impl;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemOrganizationDepartments;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.entity.SystemUserDepartments;
import com.mdas.server.repository.SystemOrganizationDepartmentsRepository;
import com.mdas.server.repository.SystemUserAccountRepository;
import com.mdas.server.repository.SystemUserDepartmentsRepository;
import com.mdas.server.service.UserDepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDepartmentServiceImpl implements UserDepartmentService {

    private final SystemUserDepartmentsRepository userDepartmentsRepository;
    private final SystemUserAccountRepository userAccountsRepository;
    private final SystemOrganizationDepartmentsRepository departmentsRepository;

    @Override
    @Transactional
    public SystemUserDepartments assignUserDepartment(AssignUserDepartmentRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("分配用户部门: 用户={}, 部门={}, 操作人={}",
                request.getUserAccount(), request.getDepartmentCode(), currentUserAccount);

        // 验证用户是否存在
        SystemUserAccounts user = userAccountsRepository.findByAccount(request.getUserAccount())
                .orElseThrow(() -> new RuntimeException("用户不存在: " + request.getUserAccount()));

        // 验证部门是否存在
        SystemOrganizationDepartments department = departmentsRepository.findByDepartmentCode(request.getDepartmentCode())
                .orElseThrow(() -> new RuntimeException("部门不存在: " + request.getDepartmentCode()));

        // 检查是否已分配该部门
        if (userDepartmentsRepository.existsByUserAccountAndDepartmentCode(request.getUserAccount(), request.getDepartmentCode())) {
            throw new RuntimeException("用户已属于该部门: " + request.getDepartmentCode());
        }

        SystemUserDepartments userDepartment = new SystemUserDepartments();
        userDepartment.setUserAccount(request.getUserAccount());
        userDepartment.setDepartmentCode(request.getDepartmentCode());
        userDepartment.setIsPrimary(request.getIsPrimary());
        userDepartment.setPosition(request.getPosition());
        userDepartment.setStatus(request.getStatus());
        userDepartment.setDescription(request.getDescription());
        userDepartment.setCreatedBy(currentUserId);
        userDepartment.setCreatedByAccount(currentUserAccount);
        userDepartment.setCreatedByName(currentUserName);
        userDepartment.setCreatedAt(LocalDateTime.now());

        // 如果设置为主部门，需要取消其他主部门
        if (request.getIsPrimary()) {
            setUserPrimaryDepartment(request.getUserAccount(), request.getDepartmentCode());
        }

        return userDepartmentsRepository.save(userDepartment);
    }

    @Override
    @Transactional
    public List<SystemUserDepartments> batchAssignUserDepartments(BatchAssignUserDepartmentsRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("批量分配用户部门: 用户={}, 部门数量={}, 操作人={}",
                request.getUserAccount(), request.getDepartmentCodes().size(), currentUserAccount);

        // 验证用户是否存在
        SystemUserAccounts user = userAccountsRepository.findByAccount(request.getUserAccount())
                .orElseThrow(() -> new RuntimeException("用户不存在: " + request.getUserAccount()));

        List<SystemUserDepartments> result = new ArrayList<>();

        // 先移除用户的所有部门关系
        userDepartmentsRepository.deleteByUserAccount(request.getUserAccount());

        // 批量分配新部门
        for (String departmentCode : request.getDepartmentCodes()) {
            // 验证部门是否存在
            SystemOrganizationDepartments department = departmentsRepository.findByDepartmentCode(departmentCode)
                    .orElseThrow(() -> new RuntimeException("部门不存在: " + departmentCode));

            SystemUserDepartments userDepartment = new SystemUserDepartments();
            userDepartment.setUserAccount(request.getUserAccount());
            userDepartment.setDepartmentCode(departmentCode);
            userDepartment.setIsPrimary(departmentCode.equals(request.getPrimaryDepartmentCode()));
            userDepartment.setStatus(request.getStatus());
            userDepartment.setCreatedBy(currentUserId);
            userDepartment.setCreatedByAccount(currentUserAccount);
            userDepartment.setCreatedByName(currentUserName);
            userDepartment.setCreatedAt(LocalDateTime.now());

            result.add(userDepartmentsRepository.save(userDepartment));
        }

        // 设置主部门
        if (request.getPrimaryDepartmentCode() != null && !request.getPrimaryDepartmentCode().isEmpty()) {
            setUserPrimaryDepartment(request.getUserAccount(), request.getPrimaryDepartmentCode());
        }

        return result;
    }

    @Override
    @Transactional
    public UserDepartmentDetailResponse batchOperateUserDepartments(UserDepartmentsBatchRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("批量操作用户部门: 用户={}, 操作人={}", request.getUserAccount(), currentUserAccount);

        // 验证用户是否存在
        SystemUserAccounts user = userAccountsRepository.findByAccount(request.getUserAccount())
                .orElseThrow(() -> new RuntimeException("用户不存在: " + request.getUserAccount()));

        // 添加部门
        if (request.getAddDepartmentCodes() != null && !request.getAddDepartmentCodes().isEmpty()) {
            for (String departmentCode : request.getAddDepartmentCodes()) {
                if (!userInDepartment(request.getUserAccount(), departmentCode)) {
                    AssignUserDepartmentRequest assignRequest = new AssignUserDepartmentRequest();
                    assignRequest.setUserAccount(request.getUserAccount());
                    assignRequest.setDepartmentCode(departmentCode);
                    assignRequest.setIsPrimary(false);
                    assignUserDepartment(assignRequest, currentUserId, currentUserAccount, currentUserName);
                }
            }
        }

        // 移除部门
        if (request.getRemoveDepartmentCodes() != null && !request.getRemoveDepartmentCodes().isEmpty()) {
            for (String departmentCode : request.getRemoveDepartmentCodes()) {
                if (userInDepartment(request.getUserAccount(), departmentCode)) {
                    removeUserDepartment(request.getUserAccount(), departmentCode);
                }
            }
        }

        // 设置主部门
        if (request.getSetPrimaryDepartmentCode() != null && !request.getSetPrimaryDepartmentCode().isEmpty()) {
            if (userInDepartment(request.getUserAccount(), request.getSetPrimaryDepartmentCode())) {
                setPrimaryUserDepartment(request.getUserAccount(), request.getSetPrimaryDepartmentCode(),
                        currentUserId, currentUserAccount, currentUserName);
            } else {
                throw new RuntimeException("用户不属于该部门，无法设置为主部门: " + request.getSetPrimaryDepartmentCode());
            }
        }

        return getUserDepartmentDetail(request.getUserAccount());
    }

    @Override
    @Transactional
    public void removeUserDepartment(String userAccount, String departmentCode) {
        log.info("移除用户部门: 用户={}, 部门={}", userAccount, departmentCode);

        SystemUserDepartments userDepartment = userDepartmentsRepository.findByUserAccountAndDepartmentCode(userAccount, departmentCode)
                .orElseThrow(() -> new RuntimeException("用户部门关系不存在"));

        // 如果是主部门，需要重新设置主部门
        boolean wasPrimary = userDepartment.getIsPrimary();

        userDepartmentsRepository.delete(userDepartment);

        // 如果删除的是主部门，需要设置新的主部门
        if (wasPrimary) {
            setNewPrimaryDepartment(userAccount);
        }
    }

    @Override
    @Transactional
    public void removeAllUserDepartments(String userAccount) {
        log.info("移除用户所有部门: 用户={}", userAccount);
        userDepartmentsRepository.deleteByUserAccount(userAccount);
    }

    @Override
    @Transactional
    public SystemUserDepartments setPrimaryUserDepartment(String userAccount, String departmentCode, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("设置用户主部门: 用户={}, 部门={}, 操作人={}", userAccount, departmentCode, currentUserAccount);

        // 验证用户部门关系是否存在
        SystemUserDepartments userDepartment = userDepartmentsRepository.findByUserAccountAndDepartmentCode(userAccount, departmentCode)
                .orElseThrow(() -> new RuntimeException("用户部门关系不存在"));

        setUserPrimaryDepartment(userAccount, departmentCode);

        // 更新操作人信息
        userDepartment.setUpdatedBy(currentUserId);
        userDepartment.setUpdatedByAccount(currentUserAccount);
        userDepartment.setUpdatedByName(currentUserName);
        userDepartment.setUpdatedAt(LocalDateTime.now());

        return userDepartmentsRepository.save(userDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDepartmentResponse> getUserDepartmentsByUserAccount(String userAccount) {
        List<SystemUserDepartments> userDepartments = userDepartmentsRepository.findByUserAccount(userAccount);
        return userDepartments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDepartmentResponse> getUserDepartmentsByDepartmentCode(String departmentCode) {
        List<SystemUserDepartments> userDepartments = userDepartmentsRepository.findByDepartmentCode(departmentCode);
        return userDepartments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDepartmentDetailResponse getUserDepartmentDetail(String userAccount) {
        // 获取用户信息
        SystemUserAccounts user = userAccountsRepository.findByAccount(userAccount)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userAccount));

        // 获取用户已分配的部门
        List<UserDepartmentResponse> assignedDepartments = getUserDepartmentsByUserAccount(userAccount);

        // 获取所有可用部门
        List<SystemOrganizationDepartments> allDepartments = departmentsRepository.findAll();
        List<DepartmentResponse> availableDepartments = allDepartments.stream()
                .map(this::convertDepartmentToResponse)
                .collect(Collectors.toList());

        // 获取主部门信息
        String primaryDepartmentCode = null;
        String primaryDepartmentName = null;
        for (UserDepartmentResponse assignedDept : assignedDepartments) {
            if (assignedDept.getIsPrimary()) {
                primaryDepartmentCode = assignedDept.getDepartmentCode();
                primaryDepartmentName = assignedDept.getDepartmentName();
                break;
            }
        }

        UserDepartmentDetailResponse response = new UserDepartmentDetailResponse();
        response.setUserAccount(userAccount);
        response.setUserName(user.getName());
        response.setAssignedDepartments(assignedDepartments);
        response.setAvailableDepartments(availableDepartments);
        response.setPrimaryDepartmentCode(primaryDepartmentCode);
        response.setPrimaryDepartmentName(primaryDepartmentName);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userInDepartment(String userAccount, String departmentCode) {
        return userDepartmentsRepository.existsByUserAccountAndDepartmentCode(userAccount, departmentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public SystemUserDepartments getUserPrimaryDepartment(String userAccount) {
        return userDepartmentsRepository.findByUserAccountAndIsPrimary(userAccount, true)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUserDepartmentCodes(String userAccount) {
        return userDepartmentsRepository.findDepartmentCodesByUserAccount(userAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDepartmentResponse> getUsersInDepartment(String departmentCode) {
        List<SystemUserDepartments> userDepartments = userDepartmentsRepository.findByDepartmentCode(departmentCode);
        return userDepartments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDepartmentResponse> getUserDepartmentsByPage(com.mdas.server.dto.PageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.fromString(pageRequest.getSortOrder()),
                pageRequest.getSortBy() != null ? pageRequest.getSortBy() : "id");
        Pageable pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        Page<SystemUserDepartments> page = userDepartmentsRepository.findAll(pageable);

        List<UserDepartmentResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getTotalElements(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentUserStatsResponse getDepartmentUserStats(String departmentCode) {
        // 验证部门是否存在
        SystemOrganizationDepartments department = departmentsRepository.findByDepartmentCode(departmentCode)
                .orElseThrow(() -> new RuntimeException("部门不存在: " + departmentCode));

        // 获取部门下的所有用户关系
        List<SystemUserDepartments> userDepartments = userDepartmentsRepository.findByDepartmentCode(departmentCode);

        DepartmentUserStatsResponse stats = new DepartmentUserStatsResponse();
        stats.setDepartmentCode(departmentCode);
        stats.setDepartmentName(department.getDepartmentName());
        stats.setTotalUsers(userDepartments.size());
        stats.setPrimaryUsers((int) userDepartments.stream()
                .filter(SystemUserDepartments::getIsPrimary)
                .count());

        // 统计活跃用户（需要关联用户表查询状态）
        long activeUsers = userDepartments.stream()
                .map(ud -> userAccountsRepository.findByAccount(ud.getUserAccount()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(user -> "active".equals(user.getStatus()))
                .count();
        stats.setActiveUsers((int) activeUsers);

        return stats;
    }

    /**
     * 设置用户主部门
     */
    private void setUserPrimaryDepartment(String userAccount, String departmentCode) {
        // 先取消所有主部门
        userDepartmentsRepository.updateAllIsPrimaryByUserAccount(userAccount, false);

        // 设置新的主部门
        userDepartmentsRepository.updateIsPrimaryByUserAccountAndDepartmentCode(userAccount, departmentCode, true);
    }

    /**
     * 设置新的主部门（当原主部门被删除时调用）
     */
    private void setNewPrimaryDepartment(String userAccount) {
        List<SystemUserDepartments> userDepartments = userDepartmentsRepository.findByUserAccount(userAccount);
        if (!userDepartments.isEmpty()) {
            // 取第一个部门作为新的主部门
            SystemUserDepartments firstDept = userDepartments.get(0);
            setUserPrimaryDepartment(userAccount, firstDept.getDepartmentCode());
        }
    }

    private UserDepartmentResponse convertToResponse(SystemUserDepartments userDepartment) {
        UserDepartmentResponse response = new UserDepartmentResponse();
        response.setId(userDepartment.getId());
        response.setUserAccount(userDepartment.getUserAccount());
        response.setDepartmentCode(userDepartment.getDepartmentCode());
        response.setIsPrimary(userDepartment.getIsPrimary());
        response.setPosition(userDepartment.getPosition());
        response.setStatus(userDepartment.getStatus());
        response.setDescription(userDepartment.getDescription());
        response.setCreatedByAccount(userDepartment.getCreatedByAccount());
        response.setCreatedByName(userDepartment.getCreatedByName());
        response.setCreatedAt(userDepartment.getCreatedAt());
        response.setUpdatedByAccount(userDepartment.getUpdatedByAccount());
        response.setUpdatedByName(userDepartment.getUpdatedByName());
        response.setUpdatedAt(userDepartment.getUpdatedAt());

        // 设置用户名称
        userAccountsRepository.findByAccount(userDepartment.getUserAccount())
                .ifPresent(user -> response.setUserName(user.getName()));

        // 设置部门名称
        departmentsRepository.findByDepartmentCode(userDepartment.getDepartmentCode())
                .ifPresent(dept -> response.setDepartmentName(dept.getDepartmentName()));

        return response;
    }

    private DepartmentResponse convertDepartmentToResponse(SystemOrganizationDepartments department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setDepartmentCode(department.getDepartmentCode());
        response.setDepartmentName(department.getDepartmentName());
        response.setLevel(department.getLevel());
        response.setParentDepartmentCode(department.getParentDepartmentCode());
        response.setDepartmentType(department.getDepartmentType());
        response.setSortOrder(department.getSortOrder());
        response.setFullPath(department.getFullPath());
        response.setManagerUserId(department.getManagerUserId());
        response.setManagerAccount(department.getManagerAccount());
        response.setManagerName(department.getManagerName());
        response.setStatus(department.getStatus());
        response.setDescription(department.getDescription());
        response.setCreatedByAccount(department.getCreatedByAccount());
        response.setCreatedByName(department.getCreatedByName());
        response.setCreatedAt(department.getCreatedAt());
        response.setUpdatedByAccount(department.getUpdatedByAccount());
        response.setUpdatedByName(department.getUpdatedByName());
        response.setUpdatedAt(department.getUpdatedAt());
        return response;
    }
}