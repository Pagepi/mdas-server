package com.mdas.server.repository;

import com.mdas.server.entity.SystemRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemRolesRepository extends JpaRepository<SystemRoles, Integer>,
        JpaSpecificationExecutor<SystemRoles> {

    // 根据角色代码查找
    Optional<SystemRoles> findByRoleCode(String roleCode);

    // 检查角色代码是否存在
    boolean existsByRoleCode(String roleCode);

    // 根据状态查找角色
    List<SystemRoles> findByStatus(String status);

    // 根据角色类型查找
    List<SystemRoles> findByRoleType(String roleType);

    // 根据角色代码列表查找
    List<SystemRoles> findByRoleCodeIn(List<String> roleCodes);

    @Query("SELECT r FROM SystemRoles r WHERE r.roleName LIKE %:keyword% OR r.roleCode LIKE %:keyword%")
    Page<SystemRoles> findByKeyword(String keyword, Pageable pageable);
}