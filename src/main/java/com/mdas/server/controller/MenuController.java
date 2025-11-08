package com.mdas.server.controller;
// package com.mdas.server.controller;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemMenus;
import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/system/menus")
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final MenuService menuService;

    /**
     * 创建菜单
     */
    @PostMapping
    public ApiResponse<SystemMenus> createMenu(@Valid @RequestBody CreateMenuRequest request, HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemMenus menu = menuService.createMenu(request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(menu);
        } catch (Exception e) {
            log.error("创建菜单失败: {}", e.getMessage(), e);
            return ApiResponse.error("创建菜单失败: " + e.getMessage());
        }
    }

    /**
     * 更新菜单
     */
    @PutMapping("/{menuCode}")
    public ApiResponse<SystemMenus> updateMenu(@PathVariable String menuCode,
                                               @Valid @RequestBody UpdateMenuRequest request,
                                               HttpServletRequest httpRequest) {
        try {
            SystemUserAccounts currentUser = getCurrentUser(httpRequest);
            SystemMenus menu = menuService.updateMenu(menuCode, request,
                    currentUser.getId(), currentUser.getAccount(), currentUser.getName());
            return ApiResponse.success(menu);
        } catch (Exception e) {
            log.error("更新菜单失败: {}", e.getMessage(), e);
            return ApiResponse.error("更新菜单失败: " + e.getMessage());
        }
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuCode}")
    public ApiResponse<Void> deleteMenu(@PathVariable String menuCode) {
        try {
            menuService.deleteMenu(menuCode);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("删除菜单失败: {}", e.getMessage(), e);
            return ApiResponse.error("删除菜单失败: " + e.getMessage());
        }
    }

    /**
     * 根据菜单编码获取菜单详情
     */
    @GetMapping("/{menuCode}")
    public ApiResponse<SystemMenus> getMenuByCode(@PathVariable String menuCode) {
        try {
            Optional<SystemMenus> menu = menuService.getMenuByCode(menuCode);
            return menu.map(ApiResponse::success)
                    .orElseGet(() -> ApiResponse.error("菜单不存在"));
        } catch (Exception e) {
            log.error("获取菜单详情失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取菜单详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有菜单列表
     */
    @GetMapping("/list")
    public ApiResponse<List<SystemMenus>> getAllMenus() {
        try {
            List<SystemMenus> menus = menuService.getAllMenus();
            return ApiResponse.success(menus);
        } catch (Exception e) {
            log.error("获取菜单列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取菜单列表失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询菜单
     */
    @GetMapping("/page")
    public ApiResponse<PageResponse<MenuResponse>> getMenusByPage(@Valid PageRequest pageRequest) {
        try {
            PageResponse<MenuResponse> pageResponse = menuService.getMenusByPage(pageRequest);
            return ApiResponse.success(pageResponse);
        } catch (Exception e) {
            log.error("分页查询菜单失败: {}", e.getMessage(), e);
            return ApiResponse.error("分页查询菜单失败: " + e.getMessage());
        }
    }

    /**
     * 获取完整的菜单树
     */
    @GetMapping("/tree")
    public ApiResponse<List<MenuResponse>> getMenuTree() {
        try {
            List<MenuResponse> menuTree = menuService.getMenuTree();
            return ApiResponse.success(menuTree);
        } catch (Exception e) {
            log.error("获取菜单树失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取菜单树失败: " + e.getMessage());
        }
    }

    /**
     * 获取菜单树节点（用于前端树形组件）
     */
    @GetMapping("/tree-nodes")
    public ApiResponse<List<MenuTreeNode>> getMenuTreeNodes() {
        try {
            List<MenuTreeNode> treeNodes = menuService.getMenuTreeNodes();
            return ApiResponse.success(treeNodes);
        } catch (Exception e) {
            log.error("获取菜单树节点失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取菜单树节点失败: " + e.getMessage());
        }
    }

    /**
     * 根据父菜单编码获取子菜单
     */
    @GetMapping("/parent/{parentMenuCode}/children")
    public ApiResponse<List<SystemMenus>> getChildrenByParentCode(@PathVariable String parentMenuCode) {
        try {
            List<SystemMenus> children = menuService.getChildrenByParentCode(parentMenuCode);
            return ApiResponse.success(children);
        } catch (Exception e) {
            log.error("获取子菜单失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取子菜单失败: " + e.getMessage());
        }
    }

    /**
     * 检查菜单编码是否存在
     */
    @GetMapping("/check/{menuCode}")
    public ApiResponse<Boolean> checkMenuCodeExists(@PathVariable String menuCode) {
        try {
            boolean exists = menuService.existsByMenuCode(menuCode);
            return ApiResponse.success(exists);
        } catch (Exception e) {
            log.error("检查菜单编码失败: {}", e.getMessage(), e);
            return ApiResponse.error("检查菜单编码失败: " + e.getMessage());
        }
    }

    /**
     * 根据应用类型获取菜单树
     */
    @GetMapping("/application/{applicationType}/tree")
    public ApiResponse<List<MenuResponse>> getMenuTreeByApplicationType(@PathVariable String applicationType) {
        try {
            List<MenuResponse> menuTree = menuService.getMenuTreeByApplicationType(applicationType);
            return ApiResponse.success(menuTree);
        } catch (Exception e) {
            log.error("根据应用类型获取菜单树失败: {}", e.getMessage(), e);
            return ApiResponse.error("根据应用类型获取菜单树失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有可用的菜单（用于角色授权）
     */
    @GetMapping("/available")
    public ApiResponse<List<SystemMenus>> getAvailableMenus() {
        try {
            List<SystemMenus> menus = menuService.getAvailableMenus();
            return ApiResponse.success(menus);
        } catch (Exception e) {
            log.error("获取可用菜单失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取可用菜单失败: " + e.getMessage());
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