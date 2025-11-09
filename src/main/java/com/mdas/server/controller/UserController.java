package com.mdas.server.controller;

import com.mdas.server.dto.ApiResponse;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    private static final String CURRENT_USER_ATTRIBUTE = "currentUser";

    // 获取所有用户
    @GetMapping
    public ResponseEntity<ApiResponse<List<SystemUserAccounts>>> getAllUsers(HttpServletRequest request) {
        SystemUserAccounts currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("未认证", 401));
        }

        List<SystemUserAccounts> users = userService.getAllUsers(currentUser);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // 根据ID获取用户
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SystemUserAccounts>> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        SystemUserAccounts currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("未认证", 401));
        }

        return userService.getUserById(id, currentUser)
                .map(user -> ResponseEntity.ok(ApiResponse.success(user)))
                .orElse(ResponseEntity.ok(ApiResponse.error("用户不存在")));
    }

    // 创建用户
    @PostMapping
    public ResponseEntity<ApiResponse<SystemUserAccounts>> createUser(@RequestBody @Valid SystemUserAccounts user, HttpServletRequest request) {
        SystemUserAccounts currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("未认证", 401));
        }

        try {
            SystemUserAccounts createdUser = userService.createUser(user, currentUser);
            log.info("用户 {} 成功创建用户 {}", currentUser.getAccount(), createdUser.getAccount());
            return ResponseEntity.ok(ApiResponse.success(createdUser));
        } catch (RuntimeException e) {
            log.error("创建用户失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 更新用户
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SystemUserAccounts>> updateUser(@PathVariable Integer id,
                                                                      @RequestBody SystemUserAccounts userDetails,
                                                                      HttpServletRequest request) {
        SystemUserAccounts currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("未认证", 401));
        }

        try {
            SystemUserAccounts updatedUser = userService.updateUser(id, userDetails, currentUser);
            log.info("用户 {} 成功更新用户 {}", currentUser.getAccount(), id);
            return ResponseEntity.ok(ApiResponse.success(updatedUser));
        } catch (RuntimeException e) {
            log.error("更新用户失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        SystemUserAccounts currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("未认证", 401));
        }

        try {
            userService.deleteUser(id, currentUser);
            log.info("用户 {} 成功删除用户 {}", currentUser.getAccount(), id);
            return ResponseEntity.ok(ApiResponse.success("用户删除成功"));
        } catch (RuntimeException e) {
            log.error("删除用户失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // 检查账号是否存在
    @GetMapping("/check-account")
    public ResponseEntity<ApiResponse<Object>> checkAccountExists(@RequestParam String account, HttpServletRequest request) {
        SystemUserAccounts currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("未认证", 401));
        }

        boolean exists = userService.isAccountExists(account, currentUser);
        return ResponseEntity.ok(ApiResponse.success(Map.of("exists", exists)));
    }

    // ========== 私有工具方法 ==========

    private SystemUserAccounts getCurrentUser(HttpServletRequest request) {
        return (SystemUserAccounts) request.getAttribute(CURRENT_USER_ATTRIBUTE);
    }
}