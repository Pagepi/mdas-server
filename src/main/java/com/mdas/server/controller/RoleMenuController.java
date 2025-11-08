package com.mdas.server.controller;
// package com.mdas.server.controller;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemRoleMenus;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.service.RoleMenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/role-menus")
@RequiredArgsConstructor
@Slf4j
public class RoleMenuController {

    private final RoleMenuService roleMenuService;

    /**
     * 分配角色菜单权限
     */
    @PostMapping("/assign")
    public ApiResponse<SystemRoleMenus> assignRoleMenu(@Valid @RequestBody AssignRoleMenuRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemRoleMenus roleMenu = roleMenuService.assignRoleMenu(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(roleMenu);
        } catch (Exception e) {
            log.error("分配角色菜单权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("分配角色菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 批量分配角色菜单权限
     */
    @PostMapping("/batch-assign")
    public ApiResponse<List<SystemRoleMenus>> batchAssignRoleMenus(@Valid @RequestBody BatchAssignRoleMenusRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            List<SystemRoleMenus> roleMenus = roleMenuService.batchAssignRoleMenus(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(roleMenus);
        } catch (Exception e) {
            log.error("批量分配角色菜单权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("批量分配角色菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 批量操作角色菜单权限（添加、移除、更新权限）
     */
    @PostMapping("/batch-operate")
    public ApiResponse<RoleMenuDetailResponse> batchOperateRoleMenus(@Valid @RequestBody RoleMenusBatchRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            RoleMenuDetailResponse result = roleMenuService.batchOperateRoleMenus(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("批量操作角色菜单权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("批量操作角色菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 移除角色菜单权限
     */
    @DeleteMapping("/role/{roleCode}/menu/{menuCode}")
    public ApiResponse<Void> removeRoleMenu(@PathVariable String roleCode, @PathVariable String menuCode) {
        try {
            roleMenuService.removeRoleMenu(roleCode, menuCode);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("移除角色菜单权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("移除角色菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 移除角色的所有菜单权限
     */
    @DeleteMapping("/role/{roleCode}/all")
    public ApiResponse<Void> removeAllRoleMenus(@PathVariable String roleCode) {
        try {
            roleMenuService.removeAllRoleMenus(roleCode);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("移除角色所有菜单权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("移除角色所有菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 更新角色菜单权限类型
     */
    @PutMapping("/role/{roleCode}/menu/{menuCode}/permission/{permissionType}")
    public ApiResponse<SystemRoleMenus> updateRoleMenuPermission(@PathVariable String roleCode,
                                                                 @PathVariable String menuCode,
                                                                 @PathVariable String permissionType,
                                                                 HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemRoleMenus roleMenu = roleMenuService.updateRoleMenuPermission(roleCode, menuCode, permissionType,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(roleMenu);
        } catch (Exception e) {
            log.error("更新角色菜单权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("更新角色菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 根据角色编码获取角色菜单权限列表
     */
    @GetMapping("/role/{roleCode}")
    public ApiResponse<List<RoleMenuResponse>> getRoleMenusByRoleCode(@PathVariable String roleCode) {
        try {
            List<RoleMenuResponse> roleMenus = roleMenuService.getRoleMenusByRoleCode(roleCode);
            return ApiResponse.success(roleMenus);
        } catch (Exception e) {
            log.error("获取角色菜单权限列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取角色菜单权限列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据菜单编码获取角色菜单权限列表
     */
    @GetMapping("/menu/{menuCode}")
    public ApiResponse<List<RoleMenuResponse>> getRoleMenusByMenuCode(@PathVariable String menuCode) {
        try {
            List<RoleMenuResponse> roleMenus = roleMenuService.getRoleMenusByMenuCode(menuCode);
            return ApiResponse.success(roleMenus);
        } catch (Exception e) {
            log.error("根据菜单获取角色权限列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("根据菜单获取角色权限列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取角色菜单权限详情（包含可分配菜单）
     */
    @GetMapping("/role/{roleCode}/detail")
    public ApiResponse<RoleMenuDetailResponse> getRoleMenuDetail(@PathVariable String roleCode) {
        try {
            RoleMenuDetailResponse detail = roleMenuService.getRoleMenuDetail(roleCode);
            return ApiResponse.success(detail);
        } catch (Exception e) {
            log.error("获取角色菜单权限详情失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取角色菜单权限详情失败: " + e.getMessage());
        }
    }

    /**
     * 检查角色是否拥有某个菜单权限
     */
    @GetMapping("/role/{roleCode}/menu/{menuCode}/permission/{permissionType}")
    public ApiResponse<Boolean> roleHasMenuPermission(@PathVariable String roleCode,
                                                      @PathVariable String menuCode,
                                                      @PathVariable String permissionType) {
        try {
            boolean hasPermission = roleMenuService.roleHasMenuPermission(roleCode, menuCode, permissionType);
            return ApiResponse.success(hasPermission);
        } catch (Exception e) {
            log.error("检查角色菜单权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("检查角色菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 获取角色的所有菜单编码
     */
    @GetMapping("/role/{roleCode}/menu-codes")
    public ApiResponse<List<String>> getRoleMenuCodes(@PathVariable String roleCode) {
        try {
            List<String> menuCodes = roleMenuService.getRoleMenuCodes(roleCode);
            return ApiResponse.success(menuCodes);
        } catch (Exception e) {
            log.error("获取角色菜单编码失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取角色菜单编码失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的所有菜单权限（用于登录后生成菜单）
     */
    @GetMapping("/user/{userAccount}/menu-permissions")
    public ApiResponse<UserMenuPermissionResponse> getUserMenuPermissions(@PathVariable String userAccount) {
        try {
            UserMenuPermissionResponse permissions = roleMenuService.getUserMenuPermissions(userAccount);
            return ApiResponse.success(permissions);
        } catch (Exception e) {
            log.error("获取用户菜单权限失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户菜单权限失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询角色菜单权限关系
     */
    @GetMapping("/page")
    public ApiResponse<PageResponse<RoleMenuResponse>> getRoleMenusByPage(@Valid PageRequest pageRequest) {
        try {
            PageResponse<RoleMenuResponse> pageResponse = roleMenuService.getRoleMenusByPage(pageRequest);
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            log.error("分页查询角色菜单权限关系失败: {}", e.getMessage(), e);
            return ApiResponse.error("分页查询角色菜单权限关系失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限类型列表
     */
    @GetMapping("/permission-types")
    public ApiResponse<List<String>> getPermissionTypes() {
        try {
            List<String> permissionTypes = roleMenuService.getPermissionTypes();
            return ApiResponse.success(permissionTypes);
        } catch (Exception e) {
            log.error("获取权限类型列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取权限类型列表失败: " + e.getMessage());
        }
    }

    /**
     * 从请求中获取当前用户信息
     */
    private SystemUserAccounts getCurrentUser(HttpServletRequest request) {
        Object currentUser = request.getAttribute("currentUser");
        if (currentUser instanceof SystemUserAccounts) {
            return (SystemUserAccounts) currentUser;
        }
        throw new RuntimeException("用户未登录或会话已过期");
    }
}
