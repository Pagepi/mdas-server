package com.mdas.server.service;
// package com.mdas.server.service;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemUserDepartments;
import java.util.List;

public interface UserDepartmentService {

    // 分配用户部门
    SystemUserDepartments assignUserDepartment(AssignUserDepartmentRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 批量分配用户部门
    List<SystemUserDepartments> batchAssignUserDepartments(BatchAssignUserDepartmentsRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 批量操作用户部门（添加、移除、设置主部门）
    UserDepartmentDetailResponse batchOperateUserDepartments(UserDepartmentsBatchRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 移除用户部门
    void removeUserDepartment(String userAccount, String departmentCode);

    // 移除用户的所有部门
    void removeAllUserDepartments(String userAccount);

    // 设置用户主部门
    SystemUserDepartments setPrimaryUserDepartment(String userAccount, String departmentCode, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 根据用户账号获取用户部门列表
    List<UserDepartmentResponse> getUserDepartmentsByUserAccount(String userAccount);

    // 根据部门编码获取用户部门列表
    List<UserDepartmentResponse> getUserDepartmentsByDepartmentCode(String departmentCode);

    // 获取用户部门详情（包含可分配部门）
    UserDepartmentDetailResponse getUserDepartmentDetail(String userAccount);

    // 检查用户是否属于某个部门
    boolean userInDepartment(String userAccount, String departmentCode);

    // 获取用户的主部门
    SystemUserDepartments getUserPrimaryDepartment(String userAccount);

    // 获取用户的所有部门编码
    List<String> getUserDepartmentCodes(String userAccount);

    // 获取部门下的所有用户
    List<UserDepartmentResponse> getUsersInDepartment(String departmentCode);

    // 分页查询用户部门关系
    PageResponse<UserDepartmentResponse> getUserDepartmentsByPage(PageRequest pageRequest);

    // 获取部门用户统计
    DepartmentUserStatsResponse getDepartmentUserStats(String departmentCode);
}