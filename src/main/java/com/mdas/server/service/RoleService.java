package com.mdas.server.service;
// package com.mdas.server.service;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemRoles;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    // 创建角色
    SystemRoles createRole(CreateRoleRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 更新角色
    SystemRoles updateRole(String roleCode, UpdateRoleRequest request, Integer currentUserId, String currentUserAccount, String currentUserName);

    // 删除角色
    void deleteRole(String roleCode);

    // 根据角色编码获取角色
    Optional<SystemRoles> getRoleByCode(String roleCode);

    // 根据ID获取角色
    Optional<SystemRoles> getRoleById(Integer id);

    // 获取所有角色列表
    List<SystemRoles> getAllRoles();

    // 分页查询角色
    PageResponse<RoleResponse> getRolesByPage(PageRequest pageRequest);

    // 检查角色编码是否存在
    boolean existsByRoleCode(String roleCode);

    // 根据状态获取角色列表
    List<SystemRoles> getRolesByStatus(String status);
}