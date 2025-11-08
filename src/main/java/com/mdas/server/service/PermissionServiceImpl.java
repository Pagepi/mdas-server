package com.mdas.server.service;

import com.mdas.server.entity.SystemMenus;
import com.mdas.server.entity.SystemRoles;
import com.mdas.server.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SystemUserRolesRepository userRolesRepository;

    @Autowired
    private SystemRoleMenusRepository roleMenusRepository;

    @Autowired
    private SystemMenusRepository menusRepository;

    @Autowired
    private SystemUserDepartmentsRepository userDepartmentsRepository;

    @Autowired
    private SystemOrganizationDepartmentsRepository departmentsRepository;

    @Override
    public boolean hasPermission(String userAccount, String permissionCode) {
        try {
            // 获取用户所有角色
            List<String> userRoles = getUserRoles(userAccount);
            if (userRoles.isEmpty()) {
                return false;
            }

            // 检查每个角色是否有该权限
            for (String roleCode : userRoles) {
                if (roleHasPermission(roleCode, permissionCode)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("检查用户权限失败: user={}, permission={}", userAccount, permissionCode, e);
            return false;
        }
    }

    @Override
    public boolean hasAnyPermission(String userAccount, Set<String> permissionCodes) {
        if (permissionCodes == null || permissionCodes.isEmpty()) {
            return true;
        }
        return permissionCodes.stream().anyMatch(permission -> hasPermission(userAccount, permission));
    }

    @Override
    public boolean hasAllPermissions(String userAccount, Set<String> permissionCodes) {
        if (permissionCodes == null || permissionCodes.isEmpty()) {
            return true;
        }
        return permissionCodes.stream().allMatch(permission -> hasPermission(userAccount, permission));
    }

    @Override
    public boolean hasRole(String userAccount, String roleCode) {
        try {
            return userRolesRepository.findByUserAccountAndRoleCode(userAccount, roleCode)
                    .map(ur -> "active".equals(ur.getStatus()))
                    .orElse(false);
        } catch (Exception e) {
            log.error("检查用户角色失败: user={}, role={}", userAccount, roleCode, e);
            return false;
        }
    }

    @Override
    public boolean hasAnyRole(String userAccount, Set<String> roleCodes) {
        if (roleCodes == null || roleCodes.isEmpty()) {
            return true;
        }
        List<String> userRoles = getUserRoles(userAccount);
        return roleCodes.stream().anyMatch(userRoles::contains);
    }

    @Override
    public boolean hasAllRoles(String userAccount, Set<String> roleCodes) {
        if (roleCodes == null || roleCodes.isEmpty()) {
            return true;
        }
        List<String> userRoles = getUserRoles(userAccount);
        return userRoles.containsAll(roleCodes);
    }

    @Override
    public List<String> getUserRoles(String userAccount) {
        try {
            return userRolesRepository.findByUserAccount(userAccount).stream()
                    .filter(ur -> "active".equals(ur.getStatus()))
                    .map(ur -> ur.getRoleCode())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取用户角色失败: user={}", userAccount, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getUserPermissions(String userAccount) {
        try {
            List<String> userRoles = getUserRoles(userAccount);
            if (userRoles.isEmpty()) {
                return Collections.emptyList();
            }

            return roleMenusRepository.findByRoleCodeIn(userRoles).stream()
                    .filter(rm -> "active".equals(rm.getStatus()))
                    .map(rm -> rm.getMenuCode())
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取用户权限失败: user={}", userAccount, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<SystemMenus> getUserMenus(String userAccount) {
        try {
            return menusRepository.findUserMenus(userAccount);
        } catch (Exception e) {
            log.error("获取用户菜单失败: user={}", userAccount, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getRolePermissions(String roleCode) {
        try {
            return roleMenusRepository.findByRoleCode(roleCode).stream()
                    .filter(rm -> "active".equals(rm.getStatus()))
                    .map(rm -> rm.getMenuCode())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取角色权限失败: role={}", roleCode, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<SystemMenus> getRoleMenus(String roleCode) {
        try {
            List<String> menuCodes = getRolePermissions(roleCode);
            if (menuCodes.isEmpty()) {
                return Collections.emptyList();
            }
            return menusRepository.findAllById(menuCodes.stream()
                    .map(this::getMenuIdByCode)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("获取角色菜单失败: role={}", roleCode, e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean roleHasPermission(String roleCode, String permissionCode) {
        try {
            // 首先通过菜单代码检查
            boolean hasMenuPermission = roleMenusRepository.findByRoleCodeAndMenuCode(roleCode, permissionCode)
                    .map(rm -> "active".equals(rm.getStatus()))
                    .orElse(false);

            if (hasMenuPermission) {
                return true;
            }

            // 然后通过权限代码检查
            return menusRepository.findByPermissionCode(permissionCode)
                    .map(menu -> roleMenusRepository.findByRoleCodeAndMenuCode(roleCode, menu.getMenuCode())
                            .map(rm -> "active".equals(rm.getStatus()))
                            .orElse(false))
                    .orElse(false);
        } catch (Exception e) {
            log.error("检查角色权限失败: role={}, permission={}", roleCode, permissionCode, e);
            return false;
        }
    }

    @Override
    public boolean hasDataPermission(String userAccount, String departmentCode) {
        try {
            // 检查用户是否属于该部门
            boolean inDepartment = userDepartmentsRepository.findByUserAccountAndDepartmentCode(userAccount, departmentCode)
                    .map(ud -> "active".equals(ud.getStatus()))
                    .orElse(false);

            if (inDepartment) {
                return true;
            }

            // 检查用户是否是部门管理员
            return isDepartmentManager(userAccount, departmentCode);
        } catch (Exception e) {
            log.error("检查数据权限失败: user={}, department={}", userAccount, departmentCode, e);
            return false;
        }
    }

    @Override
    public List<String> getUserAccessibleDepartments(String userAccount) {
        try {
            // 获取用户直接所属的部门
            List<String> userDepartments = userDepartmentsRepository.findByUserAccount(userAccount).stream()
                    .filter(ud -> "active".equals(ud.getStatus()))
                    .map(ud -> ud.getDepartmentCode())
                    .collect(Collectors.toList());

            // 获取用户管理的部门
            List<String> managedDepartments = departmentsRepository.findByManagerAccount(userAccount).stream()
                    .filter(d -> "active".equals(d.getStatus()))
                    .map(d -> d.getDepartmentCode())
                    .collect(Collectors.toList());

            // 合并去重
            userDepartments.addAll(managedDepartments);
            return userDepartments.stream().distinct().collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取用户可访问部门失败: user={}", userAccount, e);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isDepartmentManager(String userAccount, String departmentCode) {
        try {
            return departmentsRepository.findByDepartmentCode(departmentCode)
                    .map(department -> userAccount.equals(department.getManagerAccount()))
                    .orElse(false);
        } catch (Exception e) {
            log.error("检查部门管理员失败: user={}, department={}", userAccount, departmentCode, e);
            return false;
        }
    }

    /**
     * 根据菜单代码获取菜单ID（辅助方法）
     */
    private Integer getMenuIdByCode(String menuCode) {
        return menusRepository.findByMenuCode(menuCode)
                .map(SystemMenus::getId)
                .orElse(-1);
    }
}