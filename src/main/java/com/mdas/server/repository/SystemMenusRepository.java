package com.mdas.server.repository;

import com.mdas.server.entity.SystemMenus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemMenusRepository extends JpaRepository<SystemMenus, Integer>,
        JpaSpecificationExecutor<SystemMenus> {

    // 根据菜单代码查找
    Optional<SystemMenus> findByMenuCode(String menuCode);

    // 检查菜单代码是否存在
    boolean existsByMenuCode(String menuCode);

    // 根据父菜单代码查找子菜单
    List<SystemMenus> findByParentMenuCode(String parentMenuCode);

    List<SystemMenus> findByParentMenuCodeOrderBySortOrderAsc(String parentMenuCode);

    List<SystemMenus> findAllByOrderBySortOrderAsc();

    List<SystemMenus> findByApplicationTypeOrderBySortOrderAsc(String applicationType);

    List<SystemMenus> findByIsEnabledTrueAndStatusOrderBySortOrderAsc(String status);

    List<SystemMenus> findByMenuTypeOrderBySortOrderAsc(String menuType);

    @Query("SELECT m FROM SystemMenus m WHERE m.menuName LIKE %:keyword% OR m.menuCode LIKE %:keyword%")
    Page<SystemMenus> findByKeyword(String keyword, Pageable pageable);

    @Query("SELECT m FROM SystemMenus m WHERE m.isEnabled = true AND m.status = 'active' AND m.isVisible = true ORDER BY m.sortOrder ASC")
    List<SystemMenus> findVisibleMenus();

    // 根据菜单层级查找
    List<SystemMenus> findByMenuLevel(Integer menuLevel);

    // 根据菜单类型查找
    List<SystemMenus> findByMenuType(String menuType);

    // 根据状态查找菜单
    List<SystemMenus> findByStatus(String status);

    // 根据是否启用查找菜单
    List<SystemMenus> findByIsEnabled(Boolean isEnabled);

    // 根据是否可见查找菜单
    List<SystemMenus> findByIsVisible(Boolean isVisible);

    // 根据应用类型查找菜单
    List<SystemMenus> findByApplicationType(String applicationType);

    // 根据权限代码查找菜单
    Optional<SystemMenus> findByPermissionCode(String permissionCode);

    // 查找用户有权限的菜单（通过角色关联）
    @Query("SELECT DISTINCT m FROM SystemMenus m " +
            "JOIN SystemRoleMenus rm ON m.menuCode = rm.menuCode " +
            "JOIN SystemUserRoles ur ON rm.roleCode = ur.roleCode " +
            "WHERE ur.userAccount = :userAccount " +
            "AND m.isEnabled = true AND m.isVisible = true " +
            "AND rm.status = 'active' AND ur.status = 'active'")
    List<SystemMenus> findUserMenus(@Param("userAccount") String userAccount);
}