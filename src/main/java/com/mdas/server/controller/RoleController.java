package com.mdas.server.controller;
// package com.mdas.server.controller;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemRoles;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/system/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    /**
     * 创建角色
     */
    @PostMapping
    public ApiResponse<SystemRoles> createRole(@Valid @RequestBody CreateRoleRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemRoles role = roleService.createRole(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(role);
        } catch (Exception e) {
            log.error("创建角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("创建角色失败: " + e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @PutMapping("/{roleCode}")
    public ApiResponse<SystemRoles> updateRole(@PathVariable String roleCode,
                                               @Valid @RequestBody UpdateRoleRequest request,
                                               HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemRoles role = roleService.updateRole(roleCode, request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(role);
        } catch (Exception e) {
            log.error("更新角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("更新角色失败: " + e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleCode}")
    public ApiResponse<Void> deleteRole(@PathVariable String roleCode) {
        try {
            roleService.deleteRole(roleCode);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("删除角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("删除角色失败: " + e.getMessage());
        }
    }

    /**
     * 根据角色编码获取角色详情
     */
    @GetMapping("/{roleCode}")
    public ApiResponse<SystemRoles> getRoleByCode(@PathVariable String roleCode) {
        try {
            Optional<SystemRoles> role = roleService.getRoleByCode(roleCode);
            return role.map(ApiResponse::success)
                    .orElseGet(() -> ApiResponse.error("角色不存在"));
        } catch (Exception e) {
            log.error("获取角色详情失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取角色详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有角色列表
     */
    @GetMapping("/list")
    public ApiResponse<List<SystemRoles>> getAllRoles() {
        try {
            List<SystemRoles> roles = roleService.getAllRoles();
            return ApiResponse.success(roles);
        } catch (Exception e) {
            log.error("获取角色列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取角色列表失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询角色
     */
    @GetMapping("/page")
    public ApiResponse<PageResponse<RoleResponse>> getRolesByPage(@Valid PageRequest pageRequest) {
        try {
            PageResponse<RoleResponse> pageResponse = roleService.getRolesByPage(pageRequest);
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            log.error("分页查询角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("分页查询角色失败: " + e.getMessage());
        }
    }

    /**
     * 检查角色编码是否存在
     */
    @GetMapping("/check/{roleCode}")
    public ApiResponse<Boolean> checkRoleCodeExists(@PathVariable String roleCode) {
        try {
            boolean exists = roleService.existsByRoleCode(roleCode);
            return ApiResponse.success(exists);
        } catch (Exception e) {
            log.error("检查角色编码失败: {}", e.getMessage(), e);
            return ApiResponse.error("检查角色编码失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态获取角色列表
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<SystemRoles>> getRolesByStatus(@PathVariable String status) {
        try {
            List<SystemRoles> roles = roleService.getRolesByStatus(status);
            return ApiResponse.success(roles);
        } catch (Exception e) {
            log.error("根据状态查询角色失败: {}", e.getMessage(), e);
            return ApiResponse.error("根据状态查询角色失败: " + e.getMessage());
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