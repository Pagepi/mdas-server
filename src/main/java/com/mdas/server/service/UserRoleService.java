package com.mdas.server.service;
// package com.mdas.server.service;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemUserRoles;
import java.util.List;

public interface UserRoleService {

    // 分配用户角色
    SystemUserRoles assignUserRole(AssignUserRolesRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 批量分配用户角色
    List<SystemUserRoles> batchAssignUserRoles(BatchAssignUserRolesRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 批量操作用户角色（添加、移除、设置默认）
    UserRoleDetailResponse batchOperateUserRoles(UserRolesBatchRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 移除用户角色
    void removeUserRole(String userAccount, String roleCode);

    // 移除用户的所有角色
    void removeAllUserRoles(String userAccount);

    // 设置用户默认角色
    SystemUserRoles setDefaultUserRole(String userAccount, String roleCode, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 根据用户账号获取用户角色列表
    List<UserRoleResponse> getUserRolesByUserAccount(String userAccount);

    // 根据角色编码获取用户角色列表
    List<UserRoleResponse> getUserRolesByRoleCode(String roleCode);

    // 获取用户角色详情（包含可分配角色）
    UserRoleDetailResponse getUserRoleDetail(String userAccount);

    // 检查用户是否拥有某个角色
    boolean userHasRole(String userAccount, String roleCode);

    // 检查用户是否拥有某个权限（通过角色）
    boolean userHasPermission(String userAccount, String permissionCode);

    // 获取用户的默认角色
    SystemUserRoles getUserDefaultRole(String userAccount);

    // 获取用户的所有角色编码
    List<String> getUserRoleCodes(String userAccount);

    // 分页查询用户角色关系
    PageResponse<UserRoleResponse> getUserRolesByPage(PageRequest pageRequest);
}