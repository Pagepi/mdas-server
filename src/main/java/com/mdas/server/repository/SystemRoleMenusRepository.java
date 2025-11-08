package com.mdas.server.repository;

import com.mdas.server.entity.SystemRoleMenus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemRoleMenusRepository extends JpaRepository<SystemRoleMenus, Integer>,
        JpaSpecificationExecutor<SystemRoleMenus> {

    // 根据角色代码查找权限
    List<SystemRoleMenus> findByRoleCode(String roleCode);

    // 根据菜单代码查找权限
    List<SystemRoleMenus> findByMenuCode(String menuCode);

    // 根据角色代码和菜单代码查找
    Optional<SystemRoleMenus> findByRoleCodeAndMenuCode(String roleCode, String menuCode);

    // 检查角色菜单权限是否存在
    boolean existsByRoleCodeAndMenuCode(String roleCode, String menuCode);

    boolean existsByRoleCodeAndMenuCodeAndPermissionType(String roleCode, String menuCode, String permissionType);

    // 根据状态查找权限
    List<SystemRoleMenus> findByStatus(String status);

    // 根据权限类型查找
    List<SystemRoleMenus> findByPermissionType(String permissionType);

    // 根据角色代码列表查找权限
    List<SystemRoleMenus> findByRoleCodeIn(List<String> roleCodes);

    @Query("SELECT rm.menuCode FROM SystemRoleMenus rm WHERE rm.roleCode = :roleCode")
    List<String> findMenuCodesByRoleCode(@Param("roleCode") String roleCode);

    // 删除角色的所有菜单权限
    @Modifying
    @Transactional
    @Query("DELETE FROM SystemRoleMenus rm WHERE rm.roleCode = :roleCode")
    void deleteByRoleCode(@Param("roleCode") String roleCode);

    // 批量删除角色菜单权限
    @Modifying
    @Transactional
    @Query("DELETE FROM SystemRoleMenus rm WHERE rm.roleCode = :roleCode AND rm.menuCode IN :menuCodes")
    void deleteByRoleCodeAndMenuCodes(@Param("roleCode") String roleCode, @Param("menuCodes") List<String> menuCodes);

    @Query("SELECT rm FROM SystemRoleMenus rm WHERE rm.roleCode LIKE %:keyword% OR rm.menuCode LIKE %:keyword%")
    Page<SystemRoleMenus> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(rm) FROM SystemRoleMenus rm WHERE rm.roleCode = :roleCode")
    Long countByRoleCode(@Param("roleCode") String roleCode);

    @Query("SELECT COUNT(rm) FROM SystemRoleMenus rm WHERE rm.menuCode = :menuCode")
    Long countByMenuCode(@Param("menuCode") String menuCode);
}