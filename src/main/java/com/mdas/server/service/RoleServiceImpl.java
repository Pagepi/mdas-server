package com.mdas.server.service;
// package com.mdas.server.service.impl;

import com.mdas.server.dto.*;
import com.mdas.server.entity.SystemRoles;
import com.mdas.server.repository.SystemRolesRepository;
import com.mdas.server.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final SystemRolesRepository systemRolesRepository;

    @Override
    @Transactional
    public SystemRoles createRole(CreateRoleRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("创建角色: {}, 操作人: {}", request.getRoleCode(), currentUserAccount);

        // 检查角色编码是否已存在
        if (systemRolesRepository.existsByRoleCode(request.getRoleCode())) {
            throw new RuntimeException("角色编码已存在: " + request.getRoleCode());
        }

        SystemRoles role = new SystemRoles();
        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        role.setRoleType(request.getRoleType());
        role.setStatus(request.getStatus());
        role.setDescription(request.getDescription());
        role.setCreatedBy(currentUserId);
        role.setCreatedByAccount(currentUserAccount);
        role.setCreatedByName(currentUserName);
        role.setCreatedAt(LocalDateTime.now());

        return systemRolesRepository.save(role);
    }

    @Override
    @Transactional
    public SystemRoles updateRole(String roleCode, UpdateRoleRequest request, Integer currentUserId, String currentUserAccount, String currentUserName) {
        log.info("更新角色: {}, 操作人: {}", roleCode, currentUserAccount);

        SystemRoles role = systemRolesRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleCode));

        role.setRoleName(request.getRoleName());
        role.setRoleType(request.getRoleType());
        role.setStatus(request.getStatus());
        role.setDescription(request.getDescription());
        role.setUpdatedBy(currentUserId);
        role.setUpdatedByAccount(currentUserAccount);
        role.setUpdatedByName(currentUserName);
        role.setUpdatedAt(LocalDateTime.now());

        return systemRolesRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(String roleCode) {
        log.info("删除角色: {}", roleCode);

        SystemRoles role = systemRolesRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleCode));

        // 检查角色是否被用户使用（这里需要关联查询，暂时先删除）
        // TODO: 添加角色使用情况检查

        systemRolesRepository.delete(role);
        log.info("角色删除成功: {}", roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemRoles> getRoleByCode(String roleCode) {
        return systemRolesRepository.findByRoleCode(roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemRoles> getRoleById(Integer id) {
        return systemRolesRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemRoles> getAllRoles() {
        return systemRolesRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RoleResponse> getRolesByPage(com.mdas.server.dto.PageRequest pageRequest) {
        // 构建分页和排序
        Sort sort = Sort.by(Sort.Direction.fromString(pageRequest.getSortOrder()),
                pageRequest.getSortBy() != null ? pageRequest.getSortBy() : "id");
        Pageable pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        Page<SystemRoles> page = systemRolesRepository.findAll(pageable);

        // 转换为DTO
        List<RoleResponse> content = page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getTotalElements(), pageRequest.getPage(), pageRequest.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRoleCode(String roleCode) {
        return systemRolesRepository.existsByRoleCode(roleCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemRoles> getRolesByStatus(String status) {
        return systemRolesRepository.findByStatus(status);
    }

    private RoleResponse convertToResponse(SystemRoles role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setRoleCode(role.getRoleCode());
        response.setRoleName(role.getRoleName());
        response.setRoleType(role.getRoleType());
        response.setStatus(role.getStatus());
        response.setDescription(role.getDescription());
        response.setCreatedByAccount(role.getCreatedByAccount());
        response.setCreatedByName(role.getCreatedByName());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedByAccount(role.getUpdatedByAccount());
        response.setUpdatedByName(role.getUpdatedByName());
        response.setUpdatedAt(role.getUpdatedAt());
        return response;
    }
}