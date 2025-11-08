package com.mdas.server.service;
// package com.mdas.server.service;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemMenus;
import java.util.List;
import java.util.Optional;

public interface MenuService {

    // 创建菜单
    SystemMenus createMenu(CreateMenuRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 更新菜单
    SystemMenus updateMenu(String menuCode, UpdateMenuRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 删除菜单
    void deleteMenu(String menuCode);

    // 根据菜单编码获取菜单
    Optional<SystemMenus> getMenuByCode(String menuCode);

    // 根据ID获取菜单
    Optional<SystemMenus> getMenuById(Integer id);

    // 获取所有菜单列表
    List<SystemMenus> getAllMenus();

    // 分页查询菜单
    PageResponse<MenuResponse> getMenusByPage(PageRequest pageRequest);

    // 检查菜单编码是否存在
    boolean existsByMenuCode(String menuCode);

    // 根据父菜单编码获取子菜单
    List<SystemMenus> getChildrenByParentCode(String parentMenuCode);

    // 获取完整的菜单树
    List<MenuResponse> getMenuTree();

    // 获取菜单树节点（用于前端树形组件）
    List<MenuTreeNode> getMenuTreeNodes();

    // 根据应用类型获取菜单树
    List<MenuResponse> getMenuTreeByApplicationType(String applicationType);

    // 获取所有可用的菜单（用于角色授权）
    List<SystemMenus> getAvailableMenus();
}