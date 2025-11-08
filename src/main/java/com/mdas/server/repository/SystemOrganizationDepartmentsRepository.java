package com.mdas.server.repository;

import com.mdas.server.entity.SystemOrganizationDepartments;
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
public interface SystemOrganizationDepartmentsRepository extends JpaRepository<SystemOrganizationDepartments, Integer>,
        JpaSpecificationExecutor<SystemOrganizationDepartments> {

    List<SystemOrganizationDepartments> findByParentDepartmentCodeOrderBySortOrderAsc(String parentDepartmentCode);

    List<SystemOrganizationDepartments> findByParentDepartmentCodeIsNullOrderBySortOrderAsc();

    List<SystemOrganizationDepartments> findAllByOrderBySortOrderAsc();

    List<SystemOrganizationDepartments> findByStatusOrderBySortOrderAsc(String status);

    List<SystemOrganizationDepartments> findByDepartmentTypeOrderBySortOrderAsc(String departmentType);

    @Query("SELECT d FROM SystemOrganizationDepartments d WHERE d.departmentName LIKE %:keyword% OR d.departmentCode LIKE %:keyword%")
    Page<SystemOrganizationDepartments> findByKeyword(String keyword, Pageable pageable);

    @Query("SELECT d FROM SystemOrganizationDepartments d WHERE d.fullPath LIKE %:path%")
    List<SystemOrganizationDepartments> findByFullPathContaining(String path);
    // 根据部门代码查找
    Optional<SystemOrganizationDepartments> findByDepartmentCode(String departmentCode);

    // 检查部门代码是否存在
    boolean existsByDepartmentCode(String departmentCode);

    // 根据父部门代码查找子部门
    List<SystemOrganizationDepartments> findByParentDepartmentCode(String parentDepartmentCode);

    // 根据状态查找部门
    List<SystemOrganizationDepartments> findByStatus(String status);

    // 根据部门类型查找
    List<SystemOrganizationDepartments> findByDepartmentType(String departmentType);

    // 根据层级查找部门
    List<SystemOrganizationDepartments> findByLevel(Integer level);

    // 查找用户管理的部门
    List<SystemOrganizationDepartments> findByManagerAccount(String managerAccount);

    // 根据完整路径查找部门
    @Query("SELECT d FROM SystemOrganizationDepartments d WHERE d.fullPath LIKE :path%")
    List<SystemOrganizationDepartments> findByFullPathStartingWith(@Param("path") String path);
}