package com.mdas.server.service;
// package com.mdas.server.service;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemRoleMenus;
import java.util.List;

public interface RoleMenuService {

    // 分配角色菜单权限
    SystemRoleMenus assignRoleMenu(AssignRoleMenuRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 批量分配角色菜单权限
    List<SystemRoleMenus> batchAssignRoleMenus(BatchAssignRoleMenusRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 批量操作用户角色（添加、移除、更新权限）
    RoleMenuDetailResponse batchOperateRoleMenus(RoleMenusBatchRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 移除角色菜单权限
    void removeRoleMenu(String roleCode, String menuCode);

    // 移除角色的所有菜单权限
    void removeAllRoleMenus(String roleCode);

    // 更新角色菜单权限类型
    SystemRoleMenus updateRoleMenuPermission(String roleCode, String menuCode, String permissionType, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 根据角色编码获取角色菜单权限列表
    List<RoleMenuResponse> getRoleMenusByRoleCode(String roleCode);

    // 根据菜单编码获取角色菜单权限列表
    List<RoleMenuResponse> getRoleMenusByMenuCode(String menuCode);

    // 获取角色菜单权限详情（包含可分配菜单）
    RoleMenuDetailResponse getRoleMenuDetail(String roleCode);

    // 检查角色是否拥有某个菜单权限
    boolean roleHasMenuPermission(String roleCode, String menuCode, String permissionType);

    // 获取角色的所有菜单编码
    List<String> getRoleMenuCodes(String roleCode);

    // 获取用户的所有菜单权限（用于登录后生成菜单）
    UserMenuPermissionResponse getUserMenuPermissions(String userAccount);

    // 分页查询角色菜单权限关系
    PageResponse<RoleMenuResponse> getRoleMenusByPage(PageRequest pageRequest);

    // 获取权限类型列表
    List<String> getPermissionTypes();
}