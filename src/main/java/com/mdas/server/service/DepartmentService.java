package com.mdas.server.service;
// package com.mdas.server.service;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemOrganizationDepartments;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    // 创建部门
    SystemOrganizationDepartments createDepartment(CreateDepartmentRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 更新部门
    SystemOrganizationDepartments updateDepartment(String departmentCode, UpdateDepartmentRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 删除部门
    void deleteDepartment(String departmentCode);

    // 根据部门编码获取部门
    Optional<SystemOrganizationDepartments> getDepartmentByCode(String departmentCode);

    // 根据ID获取部门
    Optional<SystemOrganizationDepartments> getDepartmentById(Integer id);

    // 获取所有部门列表
    List<SystemOrganizationDepartments> getAllDepartments();

    // 分页查询部门
    PageResponse<DepartmentResponse> getDepartmentsByPage(PageRequest pageRequest);

    // 检查部门编码是否存在
    boolean existsByDepartmentCode(String departmentCode);

    // 根据父部门编码获取子部门
    List<SystemOrganizationDepartments> getChildrenByParentCode(String parentDepartmentCode);

    // 获取完整的部门树
    List<DepartmentResponse> getDepartmentTree();

    // 获取部门树节点（用于前端树形组件）
    List<DepartmentTreeNode> getDepartmentTreeNodes();

    // 根据状态获取部门列表
    List<SystemOrganizationDepartments> getDepartmentsByStatus(String status);

    // 根据部门类型获取部门列表
    List<SystemOrganizationDepartments> getDepartmentsByType(String departmentType);

    // 获取根部门（没有父部门的部门）
    List<SystemOrganizationDepartments> getRootDepartments();

    // 更新部门完整路径
    void updateDepartmentFullPath(String departmentCode);
}