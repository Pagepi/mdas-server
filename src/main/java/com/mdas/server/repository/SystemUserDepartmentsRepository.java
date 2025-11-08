package com.mdas.server.repository;

import com.mdas.server.entity.SystemUserDepartments;
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
public interface SystemUserDepartmentsRepository extends JpaRepository<SystemUserDepartments, Integer>,
        JpaSpecificationExecutor<SystemUserDepartments> {

    // 根据用户账号查找部门关系
    List<SystemUserDepartments> findByUserAccount(String userAccount);

    // 根据部门代码查找用户关系
    List<SystemUserDepartments> findByDepartmentCode(String departmentCode);

    // 根据用户账号和部门代码查找
    Optional<SystemUserDepartments> findByUserAccountAndDepartmentCode(String userAccount, String departmentCode);

    // 查找用户的主部门
    Optional<SystemUserDepartments> findByUserAccountAndIsPrimaryTrue(String userAccount);

    // 根据状态查找关系
    List<SystemUserDepartments> findByStatus(String status);

    // 检查用户部门关系是否存在
    boolean existsByUserAccountAndDepartmentCode(String userAccount, String departmentCode);

    // 删除用户的所有部门关系
    @Modifying
    @Transactional
    @Query("DELETE FROM SystemUserDepartments ud WHERE ud.userAccount = :userAccount")
    void deleteByUserAccount(@Param("userAccount") String userAccount);

    // 删除用户的特定部门关系
    @Modifying
    @Transactional
    @Query("DELETE FROM SystemUserDepartments ud WHERE ud.userAccount = :userAccount AND ud.departmentCode = :departmentCode")
    void deleteByUserAccountAndDepartmentCode(@Param("userAccount") String userAccount, @Param("departmentCode") String departmentCode);

    // 更新用户的主部门
    @Modifying
    @Transactional
    @Query("UPDATE SystemUserDepartments ud SET ud.isPrimary = false WHERE ud.userAccount = :userAccount")
    void clearPrimaryDepartment(@Param("userAccount") String userAccount);

    Optional<SystemUserDepartments> findByUserAccountAndIsPrimary(String userAccount, Boolean isPrimary);

    @Query("SELECT ud.departmentCode FROM SystemUserDepartments ud WHERE ud.userAccount = :userAccount")
    List<String> findDepartmentCodesByUserAccount(@Param("userAccount") String userAccount);

    @Modifying
    @Query("UPDATE SystemUserDepartments ud SET ud.isPrimary = :isPrimary WHERE ud.userAccount = :userAccount")
    void updateAllIsPrimaryByUserAccount(@Param("userAccount") String userAccount, @Param("isPrimary") Boolean isPrimary);

    @Modifying
    @Query("UPDATE SystemUserDepartments ud SET ud.isPrimary = :isPrimary WHERE ud.userAccount = :userAccount AND ud.departmentCode = :departmentCode")
    void updateIsPrimaryByUserAccountAndDepartmentCode(@Param("userAccount") String userAccount,
                                                       @Param("departmentCode") String departmentCode,
                                                       @Param("isPrimary") Boolean isPrimary);

    @Query("SELECT ud FROM SystemUserDepartments ud WHERE ud.userAccount LIKE %:keyword% OR ud.departmentCode LIKE %:keyword%")
    Page<SystemUserDepartments> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(ud) FROM SystemUserDepartments ud WHERE ud.departmentCode = :departmentCode")
    Long countByDepartmentCode(@Param("departmentCode") String departmentCode);

    @Query("SELECT COUNT(ud) FROM SystemUserDepartments ud WHERE ud.departmentCode = :departmentCode AND ud.isPrimary = true")
    Long countPrimaryByDepartmentCode(@Param("departmentCode") String departmentCode);
}