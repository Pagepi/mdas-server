package com.mdas.server.service;
// package com.mdas.server.service.impl;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemMenus;
import com.mdas.server.repository.SystemMenusRepository;
import com.mdas.server.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final SystemMenusRepository systemMenusRepository;

    @Override
    @Transactional
    public SystemMenus createMenu(CreateMenuRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("创建菜单: {}, 操作人: {}", request.getMenuCode(), currentUserAccount);

        // 检查菜单编码是否已存在
        if (systemMenusRepository.existsByMenuCode(request.getMenuCode())) {
            throw new RuntimeException("菜单编码已存在: " + request.getMenuCode());
        }

        SystemMenus menu = new SystemMenus();
        menu.setMenuCode(request.getMenuCode());
        menu.setMenuName(request.getMenuName());
        menu.setMenuIcon(request.getMenuIcon());
        menu.setMenuPath(request.getMenuPath());
        menu.setComponentName(request.getComponentName());
        menu.setParentMenuCode(request.getParentMenuCode());
        menu.setSortOrder(request.getSortOrder());
        menu.setMenuType(request.getMenuType());
        menu.setApplicationType(request.getApplicationType());
        menu.setIsExternal(request.getIsExternal());
        menu.setExternalUrl(request.getExternalUrl());
        menu.setPermissionCode(request.getPermissionCode());
        menu.setIsVisible(request.getIsVisible());
        menu.setIsEnabled(request.getIsEnabled());
        menu.setStatus(request.getStatus());
        menu.setDescription(request.getDescription());

        // 计算菜单层级
        if (request.getParentMenuCode() == null || request.getParentMenuCode().isEmpty()) {
            menu.setMenuLevel(1);
        } else {
            SystemMenus parentMenu = systemMenusRepository.findByMenuCode(request.getParentMenuCode())
                    .orElseThrow(() -> new RuntimeException("父菜单不存在: " + request.getParentMenuCode()));
            menu.setMenuLevel(parentMenu.getMenuLevel() + 1);
        }

        menu.setCreatedBy(currentUserId);
        menu.setCreatedByAccount(currentUserAccount);
        menu.setCreatedByName(currentUserName);
        menu.setCreatedAt(LocalDateTime.now());

        return systemMenusRepository.save(menu);
    }

    @Override
    @Transactional
    public SystemMenus updateMenu(String menuCode, UpdateMenuRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("更新菜单: {}, 操作人: {}", menuCode, currentUserAccount);

        SystemMenus menu = systemMenusRepository.findByMenuCode(menuCode)
                .orElseThrow(() -> new RuntimeException("菜单不存在: " + menuCode));

        // 检查父菜单不能是自己
        if (request.getParentMenuCode() != null && request.getParentMenuCode().equals(menuCode)) {
            throw new RuntimeException("父菜单不能是自己");
        }

        menu.setMenuName(request.getMenuName());
        menu.setMenuIcon(request.getMenuIcon());
        menu.setMenuPath(request.getMenuPath());
        menu.setComponentName(request.getComponentName());
        menu.setSortOrder(request.getSortOrder());
        menu.setMenuType(request.getMenuType());
        menu.setApplicationType(request.getApplicationType());
        menu.setIsExternal(request.getIsExternal());
        menu.setExternalUrl(request.getExternalUrl());
        menu.setPermissionCode(request.getPermissionCode());
        menu.setIsVisible(request.getIsVisible());
        menu.setIsEnabled(request.getIsEnabled());
        menu.setStatus(request.getStatus());
        menu.setDescription(request.getDescription());

        // 如果更新了父菜单，重新计算层级
        if (request.getParentMenuCode() != null && !request.getParentMenuCode().equals(menu.getParentMenuCode())) {
            if (request.getParentMenuCode().isEmpty()) {
                menu.setParentMenuCode(null);
                menu.setMenuLevel(1);
            } else {
                SystemMenus parentMenu = systemMenusRepository.findByMenuCode(request.getParentMenuCode())
                        .orElseThrow(() -> new RuntimeException("父菜单不存在: " + request.getParentMenuCode()));
                menu.setParentMenuCode(request.getParentMenuCode());
                menu.setMenuLevel(parentMenu.getMenuLevel() + 1);
            }
        }

        menu.setUpdatedBy(currentUserId);
        menu.setUpdatedByAccount(currentUserAccount);
        menu.setUpdatedByName(currentUserName);
        menu.setUpdatedAt(LocalDateTime.now());

        return systemMenusRepository.save(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(String menuCode) {
        log.info("删除菜单: {}", menuCode);

        SystemMenus menu = systemMenusRepository.findByMenuCode(menuCode)
                .orElseThrow(() -> new RuntimeException("菜单不存在: " + menuCode));

        // 检查是否有子菜单
        List<SystemMenus> children = systemMenusRepository.findByParentMenuCode(menuCode);
        if (!children.isEmpty()) {
            throw new RuntimeException("请先删除子菜单");
        }

        // 检查菜单是否被角色使用（这里需要关联查询，暂时先删除）
        // TODO: 添加菜单使用情况检查

        systemMenusRepository.delete(menu);
        log.info("菜单删除成功: {}", menuCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemMenus> getMenuByCode(String menuCode) {
        return systemMenusRepository.findByMenuCode(menuCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemMenus> getMenuById(Integer id) {
        return systemMenusRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemMenus> getAllMenus() {
        return systemMenusRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MenuResponse> getMenusByPage(com.mdas.server.dto.PageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.fromString(pageRequest.getSortOrder()),
                pageRequest.getSortBy() != null ? pageRequest.getSortBy() : "sortOrder");
        Pageable pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        Page<SystemMenus> page = systemMenusRepository.findAll(pageable);

        List<MenuResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getTotalElements(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByMenuCode(String menuCode) {
        return systemMenusRepository.existsByMenuCode(menuCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemMenus> getChildrenByParentCode(String parentMenuCode) {
        return systemMenusRepository.findByParentMenuCodeOrderBySortOrderAsc(parentMenuCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuResponse> getMenuTree() {
        List<SystemMenus> allMenus = systemMenusRepository.findAllByOrderBySortOrderAsc();
        return buildMenuTree(null, allMenus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuTreeNode> getMenuTreeNodes() {
        List<SystemMenus> allMenus = systemMenusRepository.findAllByOrderBySortOrderAsc();
        return buildMenuTreeNodes(null, allMenus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuResponse> getMenuTreeByApplicationType(String applicationType) {
        List<SystemMenus> allMenus = systemMenusRepository.findByApplicationTypeOrderBySortOrderAsc(applicationType);
        return buildMenuTree(null, allMenus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemMenus> getAvailableMenus() {
        return systemMenusRepository.findByIsEnabledTrueAndStatusOrderBySortOrderAsc("active");
    }

    /**
     * 构建菜单树
     */
    private List<MenuResponse> buildMenuTree(String parentMenuCode, List<SystemMenus> allMenus) {
        return allMenus.stream()
                .filter(menu -> {
                    if (parentMenuCode == null) {
                        return menu.getParentMenuCode() == null || menu.getParentMenuCode().isEmpty();
                    } else {
                        return parentMenuCode.equals(menu.getParentMenuCode());
                    }
                })
                .map(menu -> {
                    MenuResponse response = convertToResponse(menu);
                    response.setChildren(buildMenuTree(menu.getMenuCode(), allMenus));
                    return response;
                })
                .sorted(Comparator.comparing(MenuResponse::getSortOrder))
                .collect(Collectors.toList());
    }

    /**
     * 构建菜单树节点（用于前端树形组件）
     */
    private List<MenuTreeNode> buildMenuTreeNodes(String parentMenuCode, List<SystemMenus> allMenus) {
        return allMenus.stream()
                .filter(menu -> {
                    if (parentMenuCode == null) {
                        return menu.getParentMenuCode() == null || menu.getParentMenuCode().isEmpty();
                    } else {
                        return parentMenuCode.equals(menu.getParentMenuCode());
                    }
                })
                .map(menu -> {
                    MenuTreeNode node = new MenuTreeNode(menu.getMenuCode(), menu.getMenuName());
                    node.setIcon(menu.getMenuIcon());
                    node.setPath(menu.getMenuPath());
                    node.setSortOrder(menu.getSortOrder());
                    node.setMenuType(menu.getMenuType());

                    List<MenuTreeNode> children = buildMenuTreeNodes(menu.getMenuCode(), allMenus);
                    node.setChildren(children);
                    node.setLeaf(children.isEmpty()); // 现在这个调用应该正常工作了

                    return node;
                })
                .sorted(Comparator.comparing(MenuTreeNode::getSortOrder))
                .collect(Collectors.toList());
    }

    private MenuResponse convertToResponse(SystemMenus menu) {
        MenuResponse response = new MenuResponse();
        response.setId(menu.getId());
        response.setMenuCode(menu.getMenuCode());
        response.setMenuName(menu.getMenuName());
        response.setMenuIcon(menu.getMenuIcon());
        response.setMenuPath(menu.getMenuPath());
        response.setComponentName(menu.getComponentName());
        response.setParentMenuCode(menu.getParentMenuCode());
        response.setMenuLevel(menu.getMenuLevel());
        response.setSortOrder(menu.getSortOrder());
        response.setMenuType(menu.getMenuType());
        response.setApplicationType(menu.getApplicationType());
        response.setIsExternal(menu.getIsExternal());
        response.setExternalUrl(menu.getExternalUrl());
        response.setPermissionCode(menu.getPermissionCode());
        response.setIsVisible(menu.getIsVisible());
        response.setIsEnabled(menu.getIsEnabled());
        response.setStatus(menu.getStatus());
        response.setDescription(menu.getDescription());
        response.setCreatedByAccount(menu.getCreatedByAccount());
        response.setCreatedByName(menu.getCreatedByName());
        response.setCreatedAt(menu.getCreatedAt());
        response.setUpdatedByAccount(menu.getUpdatedByAccount());
        response.setUpdatedByName(menu.getUpdatedByName());
        response.setUpdatedAt(menu.getUpdatedAt());
        return response;
    }
}