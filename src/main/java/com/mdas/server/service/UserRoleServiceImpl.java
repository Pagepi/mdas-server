package com.mdas.server.service;
// package com.mdas.server.service.impl;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemRoles;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.entity.SystemUserRoles;
import com.mdas.server.repository.SystemRolesRepository;
import com.mdas.server.repository.SystemUserAccountRepository;
import com.mdas.server.repository.SystemUserRolesRepository;
import com.mdas.server.service.UserRoleService;
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
public class UserRoleServiceImpl implements UserRoleService {

    private final SystemUserRolesRepository userRolesRepository;
    private final SystemUserAccountRepository userAccountsRepository;
    private final SystemRolesRepository rolesRepository;

    @Override
    @Transactional
    public SystemUserRoles assignUserRole(AssignUserRolesRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("分配用户角色: 用户={}, 角色={}, 操作人={}",
                request.getUserAccount(), request.getRoleCode(), currentUserAccount);

        // 验证用户是否存在
        SystemUserAccounts user = userAccountsRepository.findByAccount(request.getUserAccount())
                .orElseThrow(() -> new RuntimeException("用户不存在: " + request.getUserAccount()));

        // 验证角色是否存在
        SystemRoles role = rolesRepository.findByRoleCode(request.getRoleCode())
                .orElseThrow(() -> new RuntimeException("角色不存在: " + request.getRoleCode()));

        // 检查是否已分配该角色
        if (userRolesRepository.existsByUserAccountAndRoleCode(request.getUserAccount(), request.getRoleCode())) {
            throw new RuntimeException("用户已拥有该角色: " + request.getRoleCode());
        }

        SystemUserRoles userRole = new SystemUserRoles();
        userRole.setUserAccount(request.getUserAccount());
        userRole.setRoleCode(request.getRoleCode());
        userRole.setIsDefault(request.getIsDefault());
        userRole.setStatus(request.getStatus());
        userRole.setDescription(request.getDescription());
        userRole.setCreatedBy(currentUserId);
        userRole.setCreatedByAccount(currentUserAccount);
        userRole.setCreatedByName(currentUserName);
        userRole.setCreatedAt(LocalDateTime.now());

        // 如果设置为默认角色，需要取消其他默认角色
        if (request.getIsDefault()) {
            setUserDefaultRole(request.getUserAccount(), request.getRoleCode());
        }

        return userRolesRepository.save(userRole);
    }

    @Override
    @Transactional
    public List<SystemUserRoles> batchAssignUserRoles(BatchAssignUserRolesRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("批量分配用户角色: 用户={}, 角色数量={}, 操作人={}",
                request.getUserAccount(), request.getRoleCodes().size(), currentUserAccount);

        // 验证用户是否存在
        SystemUserAccounts user = userAccountsRepository.findByAccount(request.getUserAccount())
                .orElseThrow(() -> new RuntimeException("用户不存在: " + request.getUserAccount()));

        List<SystemUserRoles> result = new ArrayList<>();

        // 先移除用户的所有角色
        userRolesRepository.deleteByUserAccount(request.getUserAccount());

        // 批量分配新角色
        for (String roleCode : request.getRoleCodes()) {
            // 验证角色是否存在
            SystemRoles role = rolesRepository.findByRoleCode(roleCode)
                    .orElseThrow(() -> new RuntimeException("角色不存在: " + roleCode));

            SystemUserRoles userRole = new SystemUserRoles();
            userRole.setUserAccount(request.getUserAccount());
            userRole.setRoleCode(roleCode);
            userRole.setIsDefault(roleCode.equals(request.getDefaultRoleCode()));
            userRole.setStatus(request.getStatus());
            userRole.setCreatedBy(currentUserId);
            userRole.setCreatedByAccount(currentUserAccount);
            userRole.setCreatedByName(currentUserName);
            userRole.setCreatedAt(LocalDateTime.now());

            result.add(userRolesRepository.save(userRole));
        }

        // 设置默认角色
        if (request.getDefaultRoleCode() != null && !request.getDefaultRoleCode().isEmpty()) {
            setUserDefaultRole(request.getUserAccount(), request.getDefaultRoleCode());
        }

        return result;
    }

    @Override
    @Transactional
    public UserRoleDetailResponse batchOperateUserRoles(UserRolesBatchRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("批量操作用户角色: 用户={}, 操作人={}", request.getUserAccount(), currentUserAccount);

        // 验证用户是否存在
        SystemUserAccounts user = userAccountsRepository.findByAccount(request.getUserAccount())
                .orElseThrow(() -> new RuntimeException("用户不存在: " + request.getUserAccount()));

        // 添加角色
        if (request.getAddRoleCodes() != null && !request.getAddRoleCodes().isEmpty()) {
            for (String roleCode : request.getAddRoleCodes()) {
                if (!userHasRole(request.getUserAccount(), roleCode)) {
                    AssignUserRolesRequest assignRequest = new AssignUserRolesRequest();
                    assignRequest.setUserAccount(request.getUserAccount());
                    assignRequest.setRoleCode(roleCode);
                    assignRequest.setIsDefault(false);
                    assignUserRole(assignRequest, currentUserId, currentUserAccount, currentUserName);
                }
            }
        }

        // 移除角色
        if (request.getRemoveRoleCodes() != null && !request.getRemoveRoleCodes().isEmpty()) {
            for (String roleCode : request.getRemoveRoleCodes()) {
                if (userHasRole(request.getUserAccount(), roleCode)) {
                    removeUserRole(request.getUserAccount(), roleCode);
                }
            }
        }

        // 设置默认角色
        if (request.getSetDefaultRoleCode() != null && !request.getSetDefaultRoleCode().isEmpty()) {
            if (userHasRole(request.getUserAccount(), request.getSetDefaultRoleCode())) {
                setDefaultUserRole(request.getUserAccount(), request.getSetDefaultRoleCode(),
                        currentUserId, currentUserAccount, currentUserName);
            } else {
                throw new RuntimeException("用户未拥有该角色，无法设置为默认角色: " + request.getSetDefaultRoleCode());
            }
        }

        return getUserRoleDetail(request.getUserAccount());
    }

    @Override
    @Transactional
    public void removeUserRole(String userAccount, String roleCode) {
        log.info("移除用户角色: 用户={}, 角色={}", userAccount, roleCode);

        SystemUserRoles userRole = userRolesRepository.findByUserAccountAndRoleCode(userAccount, roleCode)
                .orElseThrow(() -> new RuntimeException("用户角色关系不存在"));

        // 如果是默认角色，需要重新设置默认角色
        boolean wasDefault = userRole.getIsDefault();

        userRolesRepository.delete(userRole);

        // 如果删除的是默认角色，需要设置新的默认角色
        if (wasDefault) {
            setNewDefaultRole(userAccount);
        }
    }

    @Override
    @Transactional
    public void removeAllUserRoles(String userAccount) {
        log.info("移除用户所有角色: 用户={}", userAccount);
        userRolesRepository.deleteByUserAccount(userAccount);
    }

    @Override
    @Transactional
    public SystemUserRoles setDefaultUserRole(String userAccount, String roleCode, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("设置用户默认角色: 用户={}, 角色={}, 操作人={}", userAccount, roleCode, currentUserAccount);

        // 验证用户角色关系是否存在
        SystemUserRoles userRole = userRolesRepository.findByUserAccountAndRoleCode(userAccount, roleCode)
                .orElseThrow(() -> new RuntimeException("用户角色关系不存在"));

        setUserDefaultRole(userAccount, roleCode);

        // 更新操作人信息
        userRole.setUpdatedBy(currentUserId);
        userRole.setUpdatedByAccount(currentUserAccount);
        userRole.setUpdatedByName(currentUserName);
        userRole.setUpdatedAt(LocalDateTime.now());

        return userRolesRepository.save(userRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponse> getUserRolesByUserAccount(String userAccount) {
        List<SystemUserRoles> userRoles = userRolesRepository.findByUserAccount(userAccount);
        return userRoles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponse> getUserRolesByRoleCode(String roleCode) {
        List<SystemUserRoles> userRoles = userRolesRepository.findByRoleCode(roleCode);
        return userRoles.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserRoleDetailResponse getUserRoleDetail(String userAccount) {
        // 获取用户信息
        SystemUserAccounts user = userAccountsRepository.findByAccount(userAccount)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userAccount));

        // 获取用户已分配的角色
        List<UserRoleResponse> assignedRoles = getUserRolesByUserAccount(userAccount);

        // 获取所有可用角色
        List<SystemRoles> allRoles = rolesRepository.findAll();
        List<RoleResponse> availableRoles = allRoles.stream()
                .map(this::convertRoleToResponse)
                .collect(Collectors.toList());

        // 获取默认角色编码
        String defaultRoleCode = assignedRoles.stream()
                .filter(UserRoleResponse::getIsDefault)
                .map(UserRoleResponse::getRoleCode)
                .findFirst()
                .orElse(null);

        UserRoleDetailResponse response = new UserRoleDetailResponse();
        response.setUserAccount(userAccount);
        response.setUserName(user.getName());
        response.setAssignedRoles(assignedRoles);
        response.setAvailableRoles(availableRoles);
        response.setDefaultRoleCode(defaultRoleCode);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userHasRole(String userAccount, String roleCode) {
        return userRolesRepository.existsByUserAccountAndRoleCode(userAccount, roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userHasPermission(String userAccount, String permissionCode) {
        // 这里需要关联查询角色菜单权限表
        // 暂时返回false，后续在权限模块实现
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public SystemUserRoles getUserDefaultRole(String userAccount) {
        return userRolesRepository.findByUserAccountAndIsDefault(userAccount, true)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUserRoleCodes(String userAccount) {
        return userRolesRepository.findRoleCodesByUserAccount(userAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserRoleResponse> getUserRolesByPage(com.mdas.server.dto.PageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.fromString(pageRequest.getSortOrder()),
                pageRequest.getSortBy() != null ? pageRequest.getSortBy() : "id");
        Pageable pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        Page<SystemUserRoles> page = userRolesRepository.findAll(pageable);

        List<UserRoleResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getTotalElements(), pageRequest.getPage(), pageRequest.getSize());
    }

    /**
     * 设置用户默认角色
     */
    private void setUserDefaultRole(String userAccount, String roleCode) {
        // 先取消所有默认角色
        userRolesRepository.updateAllIsDefaultByUserAccount(userAccount, false);

        // 设置新的默认角色
        userRolesRepository.updateIsDefaultByUserAccountAndRoleCode(userAccount, roleCode, true);
    }

    /**
     * 设置新的默认角色（当原默认角色被删除时调用）
     */
    private void setNewDefaultRole(String userAccount) {
        List<SystemUserRoles> userRoles = userRolesRepository.findByUserAccount(userAccount);
        if (!userRoles.isEmpty()) {
            // 取第一个角色作为新的默认角色
            SystemUserRoles firstRole = userRoles.get(0);
            setUserDefaultRole(userAccount, firstRole.getRoleCode());
        }
    }

    private UserRoleResponse convertToResponse(SystemUserRoles userRole) {
        UserRoleResponse response = new UserRoleResponse();
        response.setId(userRole.getId());
        response.setUserAccount(userRole.getUserAccount());
        response.setRoleCode(userRole.getRoleCode());
        response.setIsDefault(userRole.getIsDefault());
        response.setStatus(userRole.getStatus());
        response.setDescription(userRole.getDescription());
        response.setCreatedByAccount(userRole.getCreatedByAccount());
        response.setCreatedByName(userRole.getCreatedByName());
        response.setCreatedAt(userRole.getCreatedAt());
        response.setUpdatedByAccount(userRole.getUpdatedByAccount());
        response.setUpdatedByName(userRole.getUpdatedByName());
        response.setUpdatedAt(userRole.getUpdatedAt());

        // 设置角色名称
        rolesRepository.findByRoleCode(userRole.getRoleCode())
                .ifPresent(role -> response.setRoleName(role.getRoleName()));

        return response;
    }

    private RoleResponse convertRoleToResponse(SystemRoles role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setRoleCode(role.getRoleCode());
        response.setRoleName(role.getRoleName());
        response.setRoleType(role.getRoleType());
        response.setStatus(role.getStatus());
        response.setDescription(role.getDescription());
        response.setCreatedByAccount(role.getCreatedByAccount());
        response.setCreatedByName(role.getCreatedByName());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedByAccount(role.getUpdatedByAccount());
        response.setUpdatedByName(role.getUpdatedByName());
        response.setUpdatedAt(role.getUpdatedAt());
        return response;
    }
}