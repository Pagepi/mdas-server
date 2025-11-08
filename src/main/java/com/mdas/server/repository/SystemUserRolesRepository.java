package com.mdas.server.repository;

import com.mdas.server.entity.SystemUserRoles;
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
public interface SystemUserRolesRepository extends JpaRepository<SystemUserRoles, Integer>,
        JpaSpecificationExecutor<SystemUserRoles> {

    Optional<SystemUserRoles> findByUserAccountAndIsDefault(String userAccount, Boolean isDefault);

    List<SystemUserRoles> findByUserAccount(String userAccount);

    @Query("SELECT ur.roleCode FROM SystemUserRoles ur WHERE ur.userAccount = :userAccount")
    List<String> findRoleCodesByUserAccount(@Param("userAccount") String userAccount);

    @Modifying
    @Query("UPDATE SystemUserRoles ur SET ur.isDefault = :isDefault WHERE ur.userAccount = :userAccount")
    void updateAllIsDefaultByUserAccount(@Param("userAccount") String userAccount, @Param("isDefault") Boolean isDefault);

    @Modifying
    @Query("UPDATE SystemUserRoles ur SET ur.isDefault = :isDefault WHERE ur.userAccount = :userAccount AND ur.roleCode = :roleCode")
    void updateIsDefaultByUserAccountAndRoleCode(@Param("userAccount") String userAccount,
                                                 @Param("roleCode") String roleCode,
                                                 @Param("isDefault") Boolean isDefault);

    @Query("SELECT ur FROM SystemUserRoles ur WHERE ur.userAccount LIKE %:keyword% OR ur.roleCode LIKE %:keyword%")
    Page<SystemUserRoles> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(ur) FROM SystemUserRoles ur WHERE ur.roleCode = :roleCode")
    Long countByRoleCode(@Param("roleCode") String roleCode);

    // 根据角色代码查找用户关系
    List<SystemUserRoles> findByRoleCode(String roleCode);

    // 根据用户账号和角色代码查找
    Optional<SystemUserRoles> findByUserAccountAndRoleCode(String userAccount, String roleCode);

    // 查找用户的默认角色
    Optional<SystemUserRoles> findByUserAccountAndIsDefaultTrue(String userAccount);

    // 根据状态查找关系
    List<SystemUserRoles> findByStatus(String status);

    // 检查用户角色关系是否存在
    boolean existsByUserAccountAndRoleCode(String userAccount, String roleCode);

    // 根据用户账号列表查找角色关系
    List<SystemUserRoles> findByUserAccountIn(List<String> userAccounts);

    // 根据角色代码列表查找用户关系
    List<SystemUserRoles> findByRoleCodeIn(List<String> roleCodes);

    // 删除用户的所有角色关系
    @Modifying
    @Transactional
    @Query("DELETE FROM SystemUserRoles ur WHERE ur.userAccount = :userAccount")
    void deleteByUserAccount(@Param("userAccount") String userAccount);

    // 删除用户的特定角色关系
    @Modifying
    @Transactional
    @Query("DELETE FROM SystemUserRoles ur WHERE ur.userAccount = :userAccount AND ur.roleCode = :roleCode")
    void deleteByUserAccountAndRoleCode(@Param("userAccount") String userAccount, @Param("roleCode") String roleCode);

    // 更新用户的默认角色
    @Modifying
    @Transactional
    @Query("UPDATE SystemUserRoles ur SET ur.isDefault = false WHERE ur.userAccount = :userAccount")
    void clearDefaultRole(@Param("userAccount") String userAccount);

    // 查找拥有指定角色的用户账号列表
    @Query("SELECT ur.userAccount FROM SystemUserRoles ur WHERE ur.roleCode = :roleCode AND ur.status = 'active'")
    List<String> findUserAccountsByRoleCode(@Param("roleCode") String roleCode);
}