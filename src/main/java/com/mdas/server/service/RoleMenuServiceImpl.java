package com.mdas.server.service;
// package com.mdas.server.service.impl;

import com.mdas.server.dto.*;
import com.mdas.server.entity.*;
import com.mdas.server.repository.*;
import com.mdas.server.service.RoleMenuService;
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
public class RoleMenuServiceImpl implements RoleMenuService {

    private final SystemRoleMenusRepository roleMenusRepository;
    private final SystemRolesRepository rolesRepository;
    private final SystemMenusRepository menusRepository;
    private final SystemUserRolesRepository userRolesRepository;
    private final SystemUserAccountRepository userAccountsRepository;

    @Override
    @Transactional
    public SystemRoleMenus assignRoleMenu(AssignRoleMenuRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("分配角色菜单权限: 角色={}, 菜单={}, 权限类型={}, 操作人={}",
                request.getRoleCode(), request.getMenuCode(), request.getPermissionType(), currentUserAccount);

        // 验证角色是否存在
        SystemRoles role = rolesRepository.findByRoleCode(request.getRoleCode())
                .orElseThrow(() -> new RuntimeException("角色不存在: " + request.getRoleCode()));

        // 验证菜单是否存在
        SystemMenus menu = menusRepository.findByMenuCode(request.getMenuCode())
                .orElseThrow(() -> new RuntimeException("菜单不存在: " + request.getMenuCode()));

        // 检查是否已分配该菜单权限
        if (roleMenusRepository.existsByRoleCodeAndMenuCode(request.getRoleCode(), request.getMenuCode())) {
            throw new RuntimeException("角色已拥有该菜单权限: " + request.getMenuCode());
        }

        SystemRoleMenus roleMenu = new SystemRoleMenus();
        roleMenu.setRoleCode(request.getRoleCode());
        roleMenu.setMenuCode(request.getMenuCode());
        roleMenu.setPermissionType(request.getPermissionType());
        roleMenu.setStatus(request.getStatus());
        roleMenu.setDescription(request.getDescription());
        roleMenu.setCreatedBy(currentUserId);
        roleMenu.setCreatedByAccount(currentUserAccount);
        roleMenu.setCreatedByName(currentUserName);
        roleMenu.setCreatedAt(LocalDateTime.now());

        return roleMenusRepository.save(roleMenu);
    }

    @Override
    @Transactional
    public List<SystemRoleMenus> batchAssignRoleMenus(BatchAssignRoleMenusRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("批量分配角色菜单权限: 角色={}, 权限数量={}, 操作人={}",
                request.getRoleCode(), request.getPermissions().size(), currentUserAccount);

        // 验证角色是否存在
        SystemRoles role = rolesRepository.findByRoleCode(request.getRoleCode())
                .orElseThrow(() -> new RuntimeException("角色不存在: " + request.getRoleCode()));

        List<SystemRoleMenus> result = new ArrayList<>();

        // 先移除角色的所有菜单权限
        roleMenusRepository.deleteByRoleCode(request.getRoleCode());

        // 批量分配新权限
        for (RoleMenuPermission permission : request.getPermissions()) {
            // 验证菜单是否存在
            SystemMenus menu = menusRepository.findByMenuCode(permission.getMenuCode())
                    .orElseThrow(() -> new RuntimeException("菜单不存在: " + permission.getMenuCode()));

            SystemRoleMenus roleMenu = new SystemRoleMenus();
            roleMenu.setRoleCode(request.getRoleCode());
            roleMenu.setMenuCode(permission.getMenuCode());
            roleMenu.setPermissionType(permission.getPermissionType());
            roleMenu.setStatus(request.getStatus());
            roleMenu.setCreatedBy(currentUserId);
            roleMenu.setCreatedByAccount(currentUserAccount);
            roleMenu.setCreatedByName(currentUserName);
            roleMenu.setCreatedAt(LocalDateTime.now());

            result.add(roleMenusRepository.save(roleMenu));
        }

        return result;
    }

    @Override
    @Transactional
    public RoleMenuDetailResponse batchOperateRoleMenus(RoleMenusBatchRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("批量操作角色菜单权限: 角色={}, 操作人={}", request.getRoleCode(), currentUserAccount);

        // 验证角色是否存在
        SystemRoles role = rolesRepository.findByRoleCode(request.getRoleCode())
                .orElseThrow(() -> new RuntimeException("角色不存在: " + request.getRoleCode()));

        // 添加权限
        if (request.getAddPermissions() != null && !request.getAddPermissions().isEmpty()) {
            for (RoleMenuPermission permission : request.getAddPermissions()) {
                if (!roleHasMenuPermission(request.getRoleCode(), permission.getMenuCode(), permission.getPermissionType())) {
                    AssignRoleMenuRequest assignRequest = new AssignRoleMenuRequest();
                    assignRequest.setRoleCode(request.getRoleCode());
                    assignRequest.setMenuCode(permission.getMenuCode());
                    assignRequest.setPermissionType(permission.getPermissionType());
                    assignRoleMenu(assignRequest, currentUserId, currentUserAccount, currentUserName);
                }
            }
        }

        // 移除权限
        if (request.getRemoveMenuCodes() != null && !request.getRemoveMenuCodes().isEmpty()) {
            for (String menuCode : request.getRemoveMenuCodes()) {
                if (roleMenusRepository.existsByRoleCodeAndMenuCode(request.getRoleCode(), menuCode)) {
                    removeRoleMenu(request.getRoleCode(), menuCode);
                }
            }
        }

        // 更新权限
        if (request.getUpdatePermissions() != null && !request.getUpdatePermissions().isEmpty()) {
            for (RoleMenuPermission permission : request.getUpdatePermissions()) {
                if (roleMenusRepository.existsByRoleCodeAndMenuCode(request.getRoleCode(), permission.getMenuCode())) {
                    updateRoleMenuPermission(request.getRoleCode(), permission.getMenuCode(),
                            permission.getPermissionType(), currentUserId, currentUserAccount, currentUserName);
                }
            }
        }

        return getRoleMenuDetail(request.getRoleCode());
    }

    @Override
    @Transactional
    public void removeRoleMenu(String roleCode, String menuCode) {
        log.info("移除角色菜单权限: 角色={}, 菜单={}", roleCode, menuCode);

        SystemRoleMenus roleMenu = roleMenusRepository.findByRoleCodeAndMenuCode(roleCode, menuCode)
                .orElseThrow(() -> new RuntimeException("角色菜单权限关系不存在"));

        roleMenusRepository.delete(roleMenu);
    }

    @Override
    @Transactional
    public void removeAllRoleMenus(String roleCode) {
        log.info("移除角色所有菜单权限: 角色={}", roleCode);
        roleMenusRepository.deleteByRoleCode(roleCode);
    }

    @Override
    @Transactional
    public SystemRoleMenus updateRoleMenuPermission(String roleCode, String menuCode, String permissionType, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("更新角色菜单权限: 角色={}, 菜单={}, 权限类型={}, 操作人={}",
                roleCode, menuCode, permissionType, currentUserAccount);

        SystemRoleMenus roleMenu = roleMenusRepository.findByRoleCodeAndMenuCode(roleCode, menuCode)
                .orElseThrow(() -> new RuntimeException("角色菜单权限关系不存在"));

        roleMenu.setPermissionType(permissionType);
        roleMenu.setUpdatedBy(currentUserId);
        roleMenu.setUpdatedByAccount(currentUserAccount);
        roleMenu.setUpdatedByName(currentUserName);
        roleMenu.setUpdatedAt(LocalDateTime.now());

        return roleMenusRepository.save(roleMenu);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleMenuResponse> getRoleMenusByRoleCode(String roleCode) {
        List<SystemRoleMenus> roleMenus = roleMenusRepository.findByRoleCode(roleCode);
        return roleMenus.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleMenuResponse> getRoleMenusByMenuCode(String menuCode) {
        List<SystemRoleMenus> roleMenus = roleMenusRepository.findByMenuCode(menuCode);
        return roleMenus.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleMenuDetailResponse getRoleMenuDetail(String roleCode) {
        // 获取角色信息
        SystemRoles role = rolesRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleCode));

        // 获取角色已分配的菜单权限
        List<RoleMenuResponse> assignedMenus = getRoleMenusByRoleCode(roleCode);

        // 获取所有可用菜单
        List<SystemMenus> allMenus = menusRepository.findByIsEnabledTrueAndStatusOrderBySortOrderAsc("active");
        List<MenuResponse> availableMenus = allMenus.stream()
                .map(this::convertMenuToResponse)
                .collect(Collectors.toList());

        // 获取权限类型列表
        List<String> permissionTypes = getPermissionTypes();

        RoleMenuDetailResponse response = new RoleMenuDetailResponse();
        response.setRoleCode(roleCode);
        response.setRoleName(role.getRoleName());
        response.setAssignedMenus(assignedMenus);
        response.setAvailableMenus(availableMenus);
        response.setPermissionTypes(permissionTypes);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean roleHasMenuPermission(String roleCode, String menuCode, String permissionType) {
        return roleMenusRepository.existsByRoleCodeAndMenuCodeAndPermissionType(roleCode, menuCode, permissionType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getRoleMenuCodes(String roleCode) {
        return roleMenusRepository.findMenuCodesByRoleCode(roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public UserMenuPermissionResponse getUserMenuPermissions(String userAccount) {
        // 获取用户信息
        SystemUserAccounts user = userAccountsRepository.findByAccount(userAccount)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userAccount));

        // 获取用户的所有角色编码
        List<String> roleCodes = userRolesRepository.findRoleCodesByUserAccount(userAccount);

        if (roleCodes.isEmpty()) {
            // 用户没有角色，返回空菜单
            UserMenuPermissionResponse response = new UserMenuPermissionResponse();
            response.setUserAccount(userAccount);
            response.setUserName(user.getName());
            response.setMenus(new ArrayList<>());
            response.setPermissions(new ArrayList<>());
            return response;
        }

        // 获取用户所有角色的菜单权限
        List<SystemRoleMenus> userRoleMenus = roleMenusRepository.findByRoleCodeIn(roleCodes);

        // 获取用户有权限访问的菜单编码（去重）
        Set<String> userMenuCodes = userRoleMenus.stream()
                .map(SystemRoleMenus::getMenuCode)
                .collect(Collectors.toSet());

        // 获取所有可用的菜单
        List<SystemMenus> allMenus = menusRepository.findByIsEnabledTrueAndStatusOrderBySortOrderAsc("active");

        // 过滤出用户有权限的菜单
        List<SystemMenus> userMenus = allMenus.stream()
                .filter(menu -> userMenuCodes.contains(menu.getMenuCode()))
                .collect(Collectors.toList());

        // 构建菜单树
        List<MenuResponse> menuTree = buildUserMenuTree(null, userMenus);

        // 获取用户的所有权限编码
        List<String> userPermissions = userRoleMenus.stream()
                .map(roleMenu -> roleMenu.getPermissionType() + ":" + roleMenu.getMenuCode())
                .distinct()
                .collect(Collectors.toList());

        UserMenuPermissionResponse response = new UserMenuPermissionResponse();
        response.setUserAccount(userAccount);
        response.setUserName(user.getName());
        response.setMenus(menuTree);
        response.setPermissions(userPermissions);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoleMenuResponse> getRoleMenusByPage(com.mdas.server.dto.PageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.fromString(pageRequest.getSortOrder()),
                pageRequest.getSortBy() != null ? pageRequest.getSortBy() : "id");
        Pageable pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        Page<SystemRoleMenus> page = roleMenusRepository.findAll(pageable);

        List<RoleMenuResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getTotalElements(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getPermissionTypes() {
        return Arrays.asList("read", "write", "delete", "export", "import", "approve", "admin");
    }

    /**
     * 构建用户菜单树
     */
    private List<MenuResponse> buildUserMenuTree(String parentMenuCode, List<SystemMenus> userMenus) {
        return userMenus.stream()
                .filter(menu -> {
                    if (parentMenuCode == null) {
                        return menu.getParentMenuCode() == null || menu.getParentMenuCode().isEmpty();
                    } else {
                        return parentMenuCode.equals(menu.getParentMenuCode());
                    }
                })
                .map(menu -> {
                    MenuResponse response = convertMenuToResponse(menu);
                    response.setChildren(buildUserMenuTree(menu.getMenuCode(), userMenus));
                    return response;
                })
                .sorted(Comparator.comparing(MenuResponse::getSortOrder))
                .collect(Collectors.toList());
    }

    private RoleMenuResponse convertToResponse(SystemRoleMenus roleMenu) {
        RoleMenuResponse response = new RoleMenuResponse();
        response.setId(roleMenu.getId());
        response.setRoleCode(roleMenu.getRoleCode());
        response.setMenuCode(roleMenu.getMenuCode());
        response.setPermissionType(roleMenu.getPermissionType());
        response.setStatus(roleMenu.getStatus());
        response.setDescription(roleMenu.getDescription());
        response.setCreatedByAccount(roleMenu.getCreatedByAccount());
        response.setCreatedByName(roleMenu.getCreatedByName());
        response.setCreatedAt(roleMenu.getCreatedAt());
        response.setUpdatedByAccount(roleMenu.getUpdatedByAccount());
        response.setUpdatedByName(roleMenu.getUpdatedByName());
        response.setUpdatedAt(roleMenu.getUpdatedAt());

        // 设置角色名称
        rolesRepository.findByRoleCode(roleMenu.getRoleCode())
                .ifPresent(role -> response.setRoleName(role.getRoleName()));

        // 设置菜单名称
        menusRepository.findByMenuCode(roleMenu.getMenuCode())
                .ifPresent(menu -> response.setMenuName(menu.getMenuName()));

        return response;
    }

    private MenuResponse convertMenuToResponse(SystemMenus menu) {
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