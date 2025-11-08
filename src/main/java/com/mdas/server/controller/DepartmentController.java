package com.mdas.server.controller;
// package com.mdas.server.controller;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemOrganizationDepartments;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.service.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/system/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 创建部门
     */
    @PostMapping
    public ApiResponse<SystemOrganizationDepartments> createDepartment(@Valid @RequestBody CreateDepartmentRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemOrganizationDepartments department = departmentService.createDepartment(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(department);
        } catch (Exception e) {
            log.error("创建部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("创建部门失败: " + e.getMessage());
        }
    }

    /**
     * 更新部门
     */
    @PutMapping("/{departmentCode}")
    public ApiResponse<SystemOrganizationDepartments> updateDepartment(@PathVariable String departmentCode,
                                                                       @Valid @RequestBody UpdateDepartmentRequest request,
                                                                       HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemOrganizationDepartments department = departmentService.updateDepartment(departmentCode, request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(department);
        } catch (Exception e) {
            log.error("更新部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("更新部门失败: " + e.getMessage());
        }
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{departmentCode}")
    public ApiResponse<Void> deleteDepartment(@PathVariable String departmentCode) {
        try {
            departmentService.deleteDepartment(departmentCode);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("删除部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("删除部门失败: " + e.getMessage());
        }
    }

    /**
     * 根据部门编码获取部门详情
     */
    @GetMapping("/{departmentCode}")
    public ApiResponse<SystemOrganizationDepartments> getDepartmentByCode(@PathVariable String departmentCode) {
        try {
            Optional<SystemOrganizationDepartments> department = departmentService.getDepartmentByCode(departmentCode);
            return department.map(ApiResponse::success)
                    .orElseGet(() -> ApiResponse.error("部门不存在"));
        } catch (Exception e) {
            log.error("获取部门详情失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取部门详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有部门列表
     */
    @GetMapping("/list")
    public ApiResponse<List<SystemOrganizationDepartments>> getAllDepartments() {
        try {
            List<SystemOrganizationDepartments> departments = departmentService.getAllDepartments();
            return ApiResponse.success(departments);
        } catch (Exception e) {
            log.error("获取部门列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取部门列表失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询部门
     */
    @GetMapping("/page")
    public ApiResponse<PageResponse<DepartmentResponse>> getDepartmentsByPage(@Valid PageRequest pageRequest) {
        try {
            PageResponse<DepartmentResponse> pageResponse = departmentService.getDepartmentsByPage(pageRequest);
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            log.error("分页查询部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("分页查询部门失败: " + e.getMessage());
        }
    }

    /**
     * 获取完整的部门树
     */
    @GetMapping("/tree")
    public ApiResponse<List<DepartmentResponse>> getDepartmentTree() {
        try {
            List<DepartmentResponse> departmentTree = departmentService.getDepartmentTree();
            return ApiResponse.success(departmentTree);
        } catch (Exception e) {
            log.error("获取部门树失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取部门树失败: " + e.getMessage());
        }
    }

    /**
     * 获取部门树节点（用于前端树形组件）
     */
    @GetMapping("/tree-nodes")
    public ApiResponse<List<DepartmentTreeNode>> getDepartmentTreeNodes() {
        try {
            List<DepartmentTreeNode> treeNodes = departmentService.getDepartmentTreeNodes();
            return ApiResponse.success(treeNodes);
        } catch (Exception e) {
            log.error("获取部门树节点失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取部门树节点失败: " + e.getMessage());
        }
    }

    /**
     * 根据父部门编码获取子部门
     */
    @GetMapping("/parent/{parentDepartmentCode}/children")
    public ApiResponse<List<SystemOrganizationDepartments>> getChildrenByParentCode(@PathVariable String parentDepartmentCode) {
        try {
            List<SystemOrganizationDepartments> children = departmentService.getChildrenByParentCode(parentDepartmentCode);
            return ApiResponse.success(children);
        } catch (Exception e) {
            log.error("获取子部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取子部门失败: " + e.getMessage());
        }
    }

    /**
     * 检查部门编码是否存在
     */
    @GetMapping("/check/{departmentCode}")
    public ApiResponse<Boolean> checkDepartmentCodeExists(@PathVariable String departmentCode) {
        try {
            boolean exists = departmentService.existsByDepartmentCode(departmentCode);
            return ApiResponse.success(exists);
        } catch (Exception e) {
            log.error("检查部门编码失败: {}", e.getMessage(), e);
            return ApiResponse.error("检查部门编码失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态获取部门列表
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<SystemOrganizationDepartments>> getDepartmentsByStatus(@PathVariable String status) {
        try {
            List<SystemOrganizationDepartments> departments = departmentService.getDepartmentsByStatus(status);
            return ApiResponse.success(departments);
        } catch (Exception e) {
            log.error("根据状态查询部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("根据状态查询部门失败: " + e.getMessage());
        }
    }

    /**
     * 根据部门类型获取部门列表
     */
    @GetMapping("/type/{departmentType}")
    public ApiResponse<List<SystemOrganizationDepartments>> getDepartmentsByType(@PathVariable String departmentType) {
        try {
            List<SystemOrganizationDepartments> departments = departmentService.getDepartmentsByType(departmentType);
            return ApiResponse.success(departments);
        } catch (Exception e) {
            log.error("根据类型查询部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("根据类型查询部门失败: " + e.getMessage());
        }
    }

    /**
     * 获取根部门列表
     */
    @GetMapping("/roots")
    public ApiResponse<List<SystemOrganizationDepartments>> getRootDepartments() {
        try {
            List<SystemOrganizationDepartments> rootDepartments = departmentService.getRootDepartments();
            return ApiResponse.success(rootDepartments);
        } catch (Exception e) {
            log.error("获取根部门失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取根部门失败: " + e.getMessage());
        }
    }

    /**
     * 更新部门完整路径
     */
    @PutMapping("/{departmentCode}/update-full-path")
    public ApiResponse<Void> updateDepartmentFullPath(@PathVariable String departmentCode) {
        try {
            departmentService.updateDepartmentFullPath(departmentCode);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("更新部门路径失败: {}", e.getMessage(), e);
            return ApiResponse.error("更新部门路径失败: " + e.getMessage());
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
