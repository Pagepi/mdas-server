package com.mdas.server.service;

import com.mdas.server.entity.SystemMenus;
import com.mdas.server.entity.SystemRoles;

import java.util.List;
import java.util.Set;

/**
 * 权限服务接口
 */
public interface PermissionService {

    // 用户权限相关
    boolean hasPermission(String userAccount, String permissionCode);
    boolean hasAnyPermission(String userAccount, Set<String> permissionCodes);
    boolean hasAllPermissions(String userAccount, Set<String> permissionCodes);

    // 用户角色相关
    boolean hasRole(String userAccount, String roleCode);
    boolean hasAnyRole(String userAccount, Set<String> roleCodes);
    boolean hasAllRoles(String userAccount, Set<String> roleCodes);

    // 获取用户信息
    List<String> getUserRoles(String userAccount);
    List<String> getUserPermissions(String userAccount);
    List<SystemMenus> getUserMenus(String userAccount);

    // 角色权限相关
    List<String> getRolePermissions(String roleCode);
    List<SystemMenus> getRoleMenus(String roleCode);
    boolean roleHasPermission(String roleCode, String permissionCode);

    // 数据权限相关
    boolean hasDataPermission(String userAccount, String departmentCode);
    List<String> getUserAccessibleDepartments(String userAccount);
    boolean isDepartmentManager(String userAccount, String departmentCode);
}