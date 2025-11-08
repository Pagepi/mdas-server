package com.mdas.server.controller;
// package com.mdas.server.controller;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.entity.SystemUserRoles;
import com.mdas.server.service.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/user-roles")
@RequiredArgsConstructor
@Slf4j
public class UserRoleController {

    private final UserRoleService userRoleService;

    /**
     * 分配用户角色
     */
    @PostMapping("/assign")
    public ApiResponse<SystemUserRoles> assignUserRole(@Valid @RequestBody AssignUserRolesRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemUserRoles userRole = userRoleService.assignUserRole(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(userRole);
        } catch (Exception e) {
            log.error("分配用户角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("分配用户角色失败: " + e.getMessage());
        }
    }

    /**
     * 批量分配用户角色
     */
    @PostMapping("/batch-assign")
    public ApiResponse<List<SystemUserRoles>> batchAssignUserRoles(@Valid @RequestBody BatchAssignUserRolesRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            List<SystemUserRoles> userRoles = userRoleService.batchAssignUserRoles(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(userRoles);
        } catch (Exception e) {
            log.error("批量分配用户角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("批量分配用户角色失败: " + e.getMessage());
        }
    }

    /**
     * 批量操作用户角色（添加、移除、设置默认）
     */
    @PostMapping("/batch-operate")
    public ApiResponse<UserRoleDetailResponse> batchOperateUserRoles(@Valid @RequestBody UserRolesBatchRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            UserRoleDetailResponse result = userRoleService.batchOperateUserRoles(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("批量操作用户角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("批量操作用户角色失败: " + e.getMessage());
        }
    }

    /**
     * 移除用户角色
     */
    @DeleteMapping("/user/{userAccount}/role/{roleCode}")
    public ApiResponse<Void> removeUserRole(@PathVariable String userAccount, @PathVariable String roleCode) {
        try {
            userRoleService.removeUserRole(userAccount, roleCode);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("移除用户角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("移除用户角色失败: " + e.getMessage());
        }
    }

    /**
     * 移除用户的所有角色
     */
    @DeleteMapping("/user/{userAccount}/all")
    public ApiResponse<Void> removeAllUserRoles(@PathVariable String userAccount) {
        try {
            userRoleService.removeAllUserRoles(userAccount);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("移除用户所有角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("移除用户所有角色失败: " + e.getMessage());
        }
    }

    /**
     * 设置用户默认角色
     */
    @PutMapping("/user/{userAccount}/default-role/{roleCode}")
    public ApiResponse<SystemUserRoles> setDefaultUserRole(@PathVariable String userAccount, @PathVariable String roleCode, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemUserRoles userRole = userRoleService.setDefaultUserRole(userAccount, roleCode,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(userRole);
        } catch (Exception e) {
            log.error("设置用户默认角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("设置用户默认角色失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户账号获取用户角色列表
     */
    @GetMapping("/user/{userAccount}")
    public ApiResponse<List<UserRoleResponse>> getUserRolesByUserAccount(@PathVariable String userAccount) {
        try {
            List<UserRoleResponse> userRoles = userRoleService.getUserRolesByUserAccount(userAccount);
            return ApiResponse.success(userRoles);
        } catch (Exception e) {
            log.error("获取用户角色列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户角色列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据角色编码获取用户角色列表
     */
    @GetMapping("/role/{roleCode}")
    public ApiResponse<List<UserRoleResponse>> getUserRolesByRoleCode(@PathVariable String roleCode) {
        try {
            List<UserRoleResponse> userRoles = userRoleService.getUserRolesByRoleCode(roleCode);
            return ApiResponse.success(userRoles);
        } catch (Exception e) {
            log.error("根据角色获取用户列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("根据角色获取用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户角色详情（包含可分配角色）
     */
    @GetMapping("/user/{userAccount}/detail")
    public ApiResponse<UserRoleDetailResponse> getUserRoleDetail(@PathVariable String userAccount) {
        try {
            UserRoleDetailResponse detail = userRoleService.getUserRoleDetail(userAccount);
            return ApiResponse.success(detail);
        } catch (Exception e) {
            log.error("获取用户角色详情失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户角色详情失败: " + e.getMessage());
        }
    }

    /**
     * 检查用户是否拥有某个角色
     */
    @GetMapping("/user/{userAccount}/has-role/{roleCode}")
    public ApiResponse<Boolean> userHasRole(@PathVariable String userAccount, @PathVariable String roleCode) {
        try {
            boolean hasRole = userRoleService.userHasRole(userAccount, roleCode);
            return ApiResponse.success(hasRole);
        } catch (Exception e) {
            log.error("检查用户角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("检查用户角色失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的默认角色
     */
    @GetMapping("/user/{userAccount}/default-role")
    public ApiResponse<SystemUserRoles> getUserDefaultRole(@PathVariable String userAccount) {
        try {
            SystemUserRoles defaultRole = userRoleService.getUserDefaultRole(userAccount);
            return ApiResponse.success(defaultRole);
        } catch (Exception e) {
            log.error("获取用户默认角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户默认角色失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的所有角色编码
     */
    @GetMapping("/user/{userAccount}/role-codes")
    public ApiResponse<List<String>> getUserRoleCodes(@PathVariable String userAccount) {
        try {
            List<String> roleCodes = userRoleService.getUserRoleCodes(userAccount);
            return ApiResponse.success(roleCodes);
        } catch (Exception e) {
            log.error("获取用户角色编码失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户角色编码失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询用户角色关系
     */
    @GetMapping("/page")
    public ApiResponse<PageResponse<UserRoleResponse>> getUserRolesByPage(@Valid PageRequest pageRequest) {
        try {
            PageResponse<UserRoleResponse> pageResponse = userRoleService.getUserRolesByPage(pageRequest);
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            log.error("分页查询用户角色关系失败: {}", e.getMessage(), e);
            return ApiResponse.error("分页查询用户角色关系失败: " + e.getMessage());
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