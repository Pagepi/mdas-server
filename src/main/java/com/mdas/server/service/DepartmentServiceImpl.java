package com.mdas.server.service;
// package com.mdas.server.service.impl;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemOrganizationDepartments;
import com.mdas.server.repository.SystemOrganizationDepartmentsRepository;
import com.mdas.server.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final SystemOrganizationDepartmentsRepository departmentRepository;

    @Override
    @Transactional
    public SystemOrganizationDepartments createDepartment(CreateDepartmentRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("创建部门: {}, 操作人: {}", request.getDepartmentCode(), currentUserAccount);

        // 检查部门编码是否已存在
        if (departmentRepository.existsByDepartmentCode(request.getDepartmentCode())) {
            throw new RuntimeException("部门编码已存在: " + request.getDepartmentCode());
        }

        SystemOrganizationDepartments department = new SystemOrganizationDepartments();
        department.setDepartmentCode(request.getDepartmentCode());
        department.setDepartmentName(request.getDepartmentName());
        department.setParentDepartmentCode(request.getParentDepartmentCode());
        department.setDepartmentType(request.getDepartmentType());
        department.setSortOrder(request.getSortOrder());
        department.setManagerUserId(request.getManagerUserId());
        department.setManagerAccount(request.getManagerAccount());
        department.setManagerName(request.getManagerName());
        department.setStatus(request.getStatus());
        department.setDescription(request.getDescription());

        // 计算部门层级和完整路径
        calculateDepartmentLevelAndPath(department, request.getParentDepartmentCode());

        department.setCreatedBy(currentUserId);
        department.setCreatedByAccount(currentUserAccount);
        department.setCreatedByName(currentUserName);
        department.setCreatedAt(LocalDateTime.now());

        SystemOrganizationDepartments savedDepartment = departmentRepository.save(department);

        // 更新完整路径（包含自身ID）
        updateFullPathWithSelf(savedDepartment);

        return savedDepartment;
    }

    @Override
    @Transactional
    public SystemOrganizationDepartments updateDepartment(String departmentCode, UpdateDepartmentRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("更新部门: {}, 操作人: {}", departmentCode, currentUserAccount);

        SystemOrganizationDepartments department = departmentRepository.findByDepartmentCode(departmentCode)
                .orElseThrow(() -> new RuntimeException("部门不存在: " + departmentCode));

        // 检查父部门不能是自己
        if (request.getParentDepartmentCode() != null && request.getParentDepartmentCode().equals(departmentCode)) {
            throw new RuntimeException("父部门不能是自己");
        }

        // 检查是否形成循环依赖
        if (request.getParentDepartmentCode() != null) {
            checkCircularDependency(departmentCode, request.getParentDepartmentCode());
        }

        String oldParentCode = department.getParentDepartmentCode();
        String newParentCode = request.getParentDepartmentCode();

        department.setDepartmentName(request.getDepartmentName());
        department.setDepartmentType(request.getDepartmentType());
        department.setSortOrder(request.getSortOrder());
        department.setManagerUserId(request.getManagerUserId());
        department.setManagerAccount(request.getManagerAccount());
        department.setManagerName(request.getManagerName());
        department.setStatus(request.getStatus());
        department.setDescription(request.getDescription());

        // 如果父部门发生变化，重新计算层级和路径
        boolean parentChanged = !Objects.equals(oldParentCode, newParentCode);
        if (parentChanged) {
            department.setParentDepartmentCode(newParentCode);
            calculateDepartmentLevelAndPath(department, newParentCode);
        }

        department.setUpdatedBy(currentUserId);
        department.setUpdatedByAccount(currentUserAccount);
        department.setUpdatedByName(currentUserName);
        department.setUpdatedAt(LocalDateTime.now());

        SystemOrganizationDepartments updatedDepartment = departmentRepository.save(department);

        // 如果父部门发生变化，更新所有子部门的路径
        if (parentChanged) {
            updateFullPathWithSelf(updatedDepartment);
            updateChildrenFullPath(departmentCode);
        }

        return updatedDepartment;
    }

    @Override
    @Transactional
    public void deleteDepartment(String departmentCode) {
        log.info("删除部门: {}", departmentCode);

        SystemOrganizationDepartments department = departmentRepository.findByDepartmentCode(departmentCode)
                .orElseThrow(() -> new RuntimeException("部门不存在: " + departmentCode));

        // 检查是否有子部门
        List<SystemOrganizationDepartments> children = departmentRepository.findByParentDepartmentCode(departmentCode);
        if (!children.isEmpty()) {
            throw new RuntimeException("请先删除子部门");
        }

        // 检查部门下是否有用户（这里需要关联查询，暂时先删除）
        // TODO: 添加部门用户检查

        departmentRepository.delete(department);
        log.info("部门删除成功: {}", departmentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemOrganizationDepartments> getDepartmentByCode(String departmentCode) {
        return departmentRepository.findByDepartmentCode(departmentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemOrganizationDepartments> getDepartmentById(Integer id) {
        return departmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemOrganizationDepartments> getAllDepartments() {
        return departmentRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DepartmentResponse> getDepartmentsByPage(com.mdas.server.dto.PageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.fromString(pageRequest.getSortOrder()),
                pageRequest.getSortBy() != null ? pageRequest.getSortBy() : "sortOrder");
        Pageable pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        Page<SystemOrganizationDepartments> page = departmentRepository.findAll(pageable);

        List<DepartmentResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getTotalElements(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDepartmentCode(String departmentCode) {
        return departmentRepository.existsByDepartmentCode(departmentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemOrganizationDepartments> getChildrenByParentCode(String parentDepartmentCode) {
        return departmentRepository.findByParentDepartmentCodeOrderBySortOrderAsc(parentDepartmentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartmentTree() {
        List<SystemOrganizationDepartments> allDepartments = departmentRepository.findAllByOrderBySortOrderAsc();
        return buildDepartmentTree(null, allDepartments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentTreeNode> getDepartmentTreeNodes() {
        List<SystemOrganizationDepartments> allDepartments = departmentRepository.findAllByOrderBySortOrderAsc();
        return buildDepartmentTreeNodes(null, allDepartments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemOrganizationDepartments> getDepartmentsByStatus(String status) {
        return departmentRepository.findByStatusOrderBySortOrderAsc(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemOrganizationDepartments> getDepartmentsByType(String departmentType) {
        return departmentRepository.findByDepartmentTypeOrderBySortOrderAsc(departmentType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemOrganizationDepartments> getRootDepartments() {
        return departmentRepository.findByParentDepartmentCodeIsNullOrderBySortOrderAsc();
    }

    @Override
    @Transactional
    public void updateDepartmentFullPath(String departmentCode) {
        SystemOrganizationDepartments department = departmentRepository.findByDepartmentCode(departmentCode)
                .orElseThrow(() -> new RuntimeException("部门不存在: " + departmentCode));
        updateFullPathWithSelf(department);
    }

    /**
     * 计算部门层级和路径
     */
    private void calculateDepartmentLevelAndPath(SystemOrganizationDepartments department, String parentDepartmentCode) {
        if (parentDepartmentCode == null || parentDepartmentCode.isEmpty()) {
            department.setLevel(1);
            department.setFullPath(department.getDepartmentCode());
        } else {
            SystemOrganizationDepartments parentDepartment = departmentRepository.findByDepartmentCode(parentDepartmentCode)
                    .orElseThrow(() -> new RuntimeException("父部门不存在: " + parentDepartmentCode));
            department.setLevel(parentDepartment.getLevel() + 1);
            department.setFullPath(parentDepartment.getFullPath() + "/" + department.getDepartmentCode());
        }
    }

    /**
     * 更新完整路径（包含自身）
     */
    private void updateFullPathWithSelf(SystemOrganizationDepartments department) {
        if (department.getParentDepartmentCode() == null || department.getParentDepartmentCode().isEmpty()) {
            department.setFullPath(department.getDepartmentCode());
        } else {
            SystemOrganizationDepartments parentDepartment = departmentRepository.findByDepartmentCode(department.getParentDepartmentCode())
                    .orElseThrow(() -> new RuntimeException("父部门不存在: " + department.getParentDepartmentCode()));
            department.setFullPath(parentDepartment.getFullPath() + "/" + department.getDepartmentCode());
        }
        departmentRepository.save(department);
    }

    /**
     * 更新所有子部门的完整路径
     */
    private void updateChildrenFullPath(String parentDepartmentCode) {
        List<SystemOrganizationDepartments> children = departmentRepository.findByParentDepartmentCode(parentDepartmentCode);
        for (SystemOrganizationDepartments child : children) {
            updateFullPathWithSelf(child);
            updateChildrenFullPath(child.getDepartmentCode());
        }
    }

    /**
     * 检查循环依赖
     */
    private void checkCircularDependency(String currentCode, String newParentCode) {
        // 简单的循环依赖检查：新父部门不能是当前部门的子部门
        Set<String> allChildrenCodes = getAllChildrenCodes(currentCode);
        if (allChildrenCodes.contains(newParentCode)) {
            throw new RuntimeException("不能将部门设置为其子部门的子部门");
        }
    }

    /**
     * 获取所有子部门编码（包括子孙部门）
     */
    private Set<String> getAllChildrenCodes(String departmentCode) {
        Set<String> childrenCodes = new HashSet<>();
        List<SystemOrganizationDepartments> directChildren = departmentRepository.findByParentDepartmentCode(departmentCode);
        for (SystemOrganizationDepartments child : directChildren) {
            childrenCodes.add(child.getDepartmentCode());
            childrenCodes.addAll(getAllChildrenCodes(child.getDepartmentCode()));
        }
        return childrenCodes;
    }

    /**
     * 构建部门树
     */
    private List<DepartmentResponse> buildDepartmentTree(String parentDepartmentCode, List<SystemOrganizationDepartments> allDepartments) {
        return allDepartments.stream()
                .filter(dept -> {
                    if (parentDepartmentCode == null) {
                        return dept.getParentDepartmentCode() == null || dept.getParentDepartmentCode().isEmpty();
                    } else {
                        return parentDepartmentCode.equals(dept.getParentDepartmentCode());
                    }
                })
                .map(dept -> {
                    DepartmentResponse response = convertToResponse(dept);
                    response.setChildren(buildDepartmentTree(dept.getDepartmentCode(), allDepartments));
                    return response;
                })
                .sorted(Comparator.comparing(DepartmentResponse::getSortOrder))
                .collect(Collectors.toList());
    }

    /**
     * 构建部门树节点（用于前端树形组件）
     */
    private List<DepartmentTreeNode> buildDepartmentTreeNodes(String parentDepartmentCode, List<SystemOrganizationDepartments> allDepartments) {
        return allDepartments.stream()
                .filter(dept -> {
                    if (parentDepartmentCode == null) {
                        return dept.getParentDepartmentCode() == null || dept.getParentDepartmentCode().isEmpty();
                    } else {
                        return parentDepartmentCode.equals(dept.getParentDepartmentCode());
                    }
                })
                .map(dept -> {
                    DepartmentTreeNode node = new DepartmentTreeNode(dept.getDepartmentCode(), dept.getDepartmentName());
                    node.setLevel(dept.getLevel());
                    node.setDepartmentType(dept.getDepartmentType());
                    node.setSortOrder(dept.getSortOrder());

                    List<DepartmentTreeNode> children = buildDepartmentTreeNodes(dept.getDepartmentCode(), allDepartments);
                    node.setChildren(children);
                    node.setLeaf(children.isEmpty()); // 现在这个调用应该正常工作了

                    return node;
                })
                .sorted(Comparator.comparing(DepartmentTreeNode::getSortOrder))
                .collect(Collectors.toList());
    }

    private DepartmentResponse convertToResponse(SystemOrganizationDepartments department) {
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

        // 设置父部门名称
        if (department.getParentDepartmentCode() != null && !department.getParentDepartmentCode().isEmpty()) {
            departmentRepository.findByDepartmentCode(department.getParentDepartmentCode())
                    .ifPresent(parent -> response.setParentDepartmentName(parent.getDepartmentName()));
        }

        return response;
    }
}