package com.mdas.server.controller;
// package com.mdas.server.controller;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.entity.SystemUserDepartments;
import com.mdas.server.service.UserDepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/user-departments")
@RequiredArgsConstructor
@Slf4j
public class UserDepartmentController {

    private final UserDepartmentService userDepartmentService;

    /**
     * 分配用户部门
     */
    @PostMapping("/assign")
    public ApiResponse<SystemUserDepartments> assignUserDepartment(@Valid @RequestBody AssignUserDepartmentRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemUserDepartments userDepartment = userDepartmentService.assignUserDepartment(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(userDepartment);
        } catch (Exception e) {
            log.error("分配用户部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("分配用户部门失败: " + e.getMessage());
        }
    }

    /**
     * 批量分配用户部门
     */
    @PostMapping("/batch-assign")
    public ApiResponse<List<SystemUserDepartments>> batchAssignUserDepartments(@Valid @RequestBody BatchAssignUserDepartmentsRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            List<SystemUserDepartments> userDepartments = userDepartmentService.batchAssignUserDepartments(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(userDepartments);
        } catch (Exception e) {
            log.error("批量分配用户部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("批量分配用户部门失败: " + e.getMessage());
        }
    }

    /**
     * 批量操作用户部门（添加、移除、设置主部门）
     */
    @PostMapping("/batch-operate")
    public ApiResponse<UserDepartmentDetailResponse> batchOperateUserDepartments(@Valid @RequestBody UserDepartmentsBatchRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            UserDepartmentDetailResponse result = userDepartmentService.batchOperateUserDepartments(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("批量操作用户部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("批量操作用户部门失败: " + e.getMessage());
        }
    }

    /**
     * 移除用户部门
     */
    @DeleteMapping("/user/{userAccount}/department/{departmentCode}")
    public ApiResponse<Void> removeUserDepartment(@PathVariable String userAccount, @PathVariable String departmentCode) {
        try {
            userDepartmentService.removeUserDepartment(userAccount, departmentCode);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("移除用户部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("移除用户部门失败: " + e.getMessage());
        }
    }

    /**
     * 移除用户的所有部门
     */
    @DeleteMapping("/user/{userAccount}/all")
    public ApiResponse<Void> removeAllUserDepartments(@PathVariable String userAccount) {
        try {
            userDepartmentService.removeAllUserDepartments(userAccount);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("移除用户所有部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("移除用户所有部门失败: " + e.getMessage());
        }
    }

    /**
     * 设置用户主部门
     */
    @PutMapping("/user/{userAccount}/primary-department/{departmentCode}")
    public ApiResponse<SystemUserDepartments> setPrimaryUserDepartment(@PathVariable String userAccount, @PathVariable String departmentCode, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemUserDepartments userDepartment = userDepartmentService.setPrimaryUserDepartment(userAccount, departmentCode,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(userDepartment);
        } catch (Exception e) {
            log.error("设置用户主部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("设置用户主部门失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户账号获取用户部门列表
     */
    @GetMapping("/user/{userAccount}")
    public ApiResponse<List<UserDepartmentResponse>> getUserDepartmentsByUserAccount(@PathVariable String userAccount) {
        try {
            List<UserDepartmentResponse> userDepartments = userDepartmentService.getUserDepartmentsByUserAccount(userAccount);
            return ApiResponse.success(userDepartments);
        } catch (Exception e) {
            log.error("获取用户部门列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户部门列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据部门编码获取用户部门列表
     */
    @GetMapping("/department/{departmentCode}")
    public ApiResponse<List<UserDepartmentResponse>> getUserDepartmentsByDepartmentCode(@PathVariable String departmentCode) {
        try {
            List<UserDepartmentResponse> userDepartments = userDepartmentService.getUserDepartmentsByDepartmentCode(departmentCode);
            return ApiResponse.success(userDepartments);
        } catch (Exception e) {
            log.error("根据部门获取用户列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("根据部门获取用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户部门详情（包含可分配部门）
     */
    @GetMapping("/user/{userAccount}/detail")
    public ApiResponse<UserDepartmentDetailResponse> getUserDepartmentDetail(@PathVariable String userAccount) {
        try {
            UserDepartmentDetailResponse detail = userDepartmentService.getUserDepartmentDetail(userAccount);
            return ApiResponse.success(detail);
        } catch (Exception e) {
            log.error("获取用户部门详情失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户部门详情失败: " + e.getMessage());
        }
    }

    /**
     * 检查用户是否属于某个部门
     */
    @GetMapping("/user/{userAccount}/in-department/{departmentCode}")
    public ApiResponse<Boolean> userInDepartment(@PathVariable String userAccount, @PathVariable String departmentCode) {
        try {
            boolean inDepartment = userDepartmentService.userInDepartment(userAccount, departmentCode);
            return ApiResponse.success(inDepartment);
        } catch (Exception e) {
            log.error("检查用户部门关系失败: {}", e.getMessage(), e);
            return ApiResponse.error("检查用户部门关系失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的主部门
     */
    @GetMapping("/user/{userAccount}/primary-department")
    public ApiResponse<SystemUserDepartments> getUserPrimaryDepartment(@PathVariable String userAccount) {
        try {
            SystemUserDepartments primaryDepartment = userDepartmentService.getUserPrimaryDepartment(userAccount);
            return ApiResponse.success(primaryDepartment);
        } catch (Exception e) {
            log.error("获取用户主部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户主部门失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的所有部门编码
     */
    @GetMapping("/user/{userAccount}/department-codes")
    public ApiResponse<List<String>> getUserDepartmentCodes(@PathVariable String userAccount) {
        try {
            List<String> departmentCodes = userDepartmentService.getUserDepartmentCodes(userAccount);
            return ApiResponse.success(departmentCodes);
        } catch (Exception e) {
            log.error("获取用户部门编码失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取用户部门编码失败: " + e.getMessage());
        }
    }

    /**
     * 获取部门下的所有用户
     */
    @GetMapping("/department/{departmentCode}/users")
    public ApiResponse<List<UserDepartmentResponse>> getUsersInDepartment(@PathVariable String departmentCode) {
        try {
            List<UserDepartmentResponse> users = userDepartmentService.getUsersInDepartment(departmentCode);
            return ApiResponse.success(users);
        } catch (Exception e) {
            log.error("获取部门用户列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取部门用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询用户部门关系
     */
    @GetMapping("/page")
    public ApiResponse<PageResponse<UserDepartmentResponse>> getUserDepartmentsByPage(@Valid PageRequest pageRequest) {
        try {
            PageResponse<UserDepartmentResponse> pageResponse = userDepartmentService.getUserDepartmentsByPage(pageRequest);
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            log.error("分页查询用户部门关系失败: {}", e.getMessage(), e);
            return ApiResponse.error("分页查询用户部门关系失败: " + e.getMessage());
        }
    }

    /**
     * 获取部门用户统计
     */
    @GetMapping("/department/{departmentCode}/stats")
    public ApiResponse<DepartmentUserStatsResponse> getDepartmentUserStats(@PathVariable String departmentCode) {
        try {
            DepartmentUserStatsResponse stats = userDepartmentService.getDepartmentUserStats(departmentCode);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            log.error("获取部门用户统计失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取部门用户统计失败: " + e.getMessage());
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