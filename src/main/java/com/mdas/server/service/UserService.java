package com.mdas.server.service;

import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.repository.SystemUserAccountRepository;
import com.mdas.server.util.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private SystemUserAccountRepository userRepository;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 获取所有用户（添加权限检查）
    public List<SystemUserAccounts> getAllUsers(SystemUserAccounts currentUser) {
        log.info("用户 {} 查询用户列表", currentUser.getAccount());
        return userRepository.findAll();
    }

    /**
     * 拦截器专用 - 根据ID获取用户（不需要当前用户参数）
     */
    public Optional<SystemUserAccounts> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // 根据ID获取用户（添加权限检查）
    public Optional<SystemUserAccounts> getUserById(Integer id, SystemUserAccounts currentUser) {
        log.info("用户 {} 查询用户详情 {}", currentUser.getAccount(), id);
        return userRepository.findById(id);
    }

    /**
     * 获取客户端IP（简化版）
     */
    private String getClientIp() {
        // 在实际项目中可以从RequestContextHolder获取
        return "unknown";
    }

    /**
     * 用户对象转JSON字符串（简化版）
     */
    private String userToJson(SystemUserAccounts user) {
        if (user == null) return null;

        return String.format("{\"id\":%d,\"account\":\"%s\",\"name\":\"%s\",\"email\":\"%s\",\"mobile\":\"%s\",\"status\":\"%s\",\"role\":\"%s\"}",
                user.getId(), user.getAccount(), user.getName(),
                user.getEmail() != null ? user.getEmail() : "",
                user.getMobile() != null ? user.getMobile() : "",
                user.getStatus(), user.getRole());
    }

    // 创建用户
    public SystemUserAccounts createUser(SystemUserAccounts user, SystemUserAccounts currentUser) {
        // 检查账号是否已存在
        if (userRepository.existsByAccount(user.getAccount())) {
            operationLogService.logFailedOperation("user", "create",
                    "创建用户失败: 账号已存在", currentUser, "账号已存在: " + user.getAccount(), getClientIp());
            throw new RuntimeException("账号已存在");
        }

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            log.info("用户密码已加密保存");
        }
        else {
            String encodedPassword = passwordEncoder.encode("123456");
            user.setPassword(encodedPassword);
            log.info("用户密码已加密保存");
        }

        // 设置创建信息
        user.setCreatedBy(currentUser.getId());
        user.setCreatedByAccount(currentUser.getAccount());
        user.setCreatedByName(currentUser.getName());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus("active");
        }
        if (user.getRole() == null) {
            user.setRole("user");
        }

        SystemUserAccounts savedUser = userRepository.save(user);

        // 记录操作日志
        String operationContent = String.format("创建用户: %s(%s)", user.getAccount(), user.getName());
        operationLogService.logUserOperation("create", currentUser, savedUser.getId(),
                operationContent, null, userToJson(savedUser), "all", getClientIp());

        log.info("用户 {} 创建了新用户: {}", currentUser.getAccount(), user.getAccount());
        return savedUser;
    }

    // 更新用户
    public SystemUserAccounts updateUser(Integer id, SystemUserAccounts userDetails, SystemUserAccounts currentUser) {
        SystemUserAccounts user = userRepository.findById(id)
                .orElseThrow(() -> {
                    operationLogService.logFailedOperation("user", "update",
                            "更新用户失败: 用户不存在", currentUser, "用户不存在: " + id, getClientIp());
                    return new RuntimeException("用户不存在");
                });

        // 记录变更前的数据
        String changeBefore = userToJson(user);

        // 记录变更的字段
        StringBuilder changedFields = new StringBuilder();

        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
            // 使用matches方法比较密码是否相同
            boolean isSamePassword = passwordEncoder.matches(userDetails.getPassword(), user.getPassword());

            if (!isSamePassword) {
                // 加密新密码
                String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
                user.setPassword(encodedPassword);
                changedFields.append("密码,");
                log.info("用户 {} 密码已更新并加密", user.getAccount());
            } else {
                log.info("用户 {} 密码未变更，跳过更新", user.getAccount());
            }
        }

        // 更新字段并记录变更
        if (userDetails.getName() != null && !userDetails.getName().equals(user.getName())) {
            user.setName(userDetails.getName());
            changedFields.append("姓名,");
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            user.setEmail(userDetails.getEmail());
            changedFields.append("邮箱,");
        }
        if (userDetails.getMobile() != null && !userDetails.getMobile().equals(user.getMobile())) {
            user.setMobile(userDetails.getMobile());
            changedFields.append("手机,");
        }
        if (userDetails.getStatus() != null && !userDetails.getStatus().equals(user.getStatus())) {
            user.setStatus(userDetails.getStatus());
            changedFields.append("状态,");
        }
        if (userDetails.getDescription() != null && !userDetails.getDescription().equals(user.getDescription())) {
            user.setDescription(userDetails.getDescription());
            changedFields.append("描述,");
        }
        if (userDetails.getRole() != null && !userDetails.getRole().equals(user.getRole())) {
            user.setRole(userDetails.getRole());
            changedFields.append("角色,");
        }

        // 如果没有实质性变更
        if (changedFields.length() == 0) {
            log.info("用户 {} 更新用户 {} 但无实质性变更", currentUser.getAccount(), id);
            return user;
        }

        // 设置更新信息
        user.setUpdatedBy(currentUser.getId());
        user.setUpdatedByAccount(currentUser.getAccount());
        user.setUpdatedByName(currentUser.getName());
        user.setUpdatedAt(LocalDateTime.now());

        SystemUserAccounts updatedUser = userRepository.save(user);

        // 记录变更后的数据
        String changeAfter = userToJson(updatedUser);
        String fields = changedFields.substring(0, changedFields.length() - 1); // 移除最后一个逗号

        // 记录操作日志
        String operationContent = String.format("更新用户: %s(%s)", user.getAccount(), user.getName());
        operationLogService.logUserOperation("update", currentUser, id,
                operationContent, changeBefore, changeAfter, fields, getClientIp());

        log.info("用户 {} 更新了用户 {} 的信息", currentUser.getAccount(), id);
        return updatedUser;
    }

    // 删除用户
    public void deleteUser(Integer id, SystemUserAccounts currentUser) {
        SystemUserAccounts user = userRepository.findById(id)
                .orElseThrow(() -> {
                    operationLogService.logFailedOperation("user", "delete",
                            "删除用户失败: 用户不存在", currentUser, "用户不存在: " + id, getClientIp());
                    return new RuntimeException("用户不存在");
                });

        // 防止删除自己
        if (id.equals(currentUser.getId())) {
            operationLogService.logFailedOperation("user", "delete",
                    "删除用户失败: 不能删除自己", currentUser, "尝试删除自己的账户", getClientIp());
            throw new RuntimeException("不能删除自己的账户");
        }

        // 记录删除前的数据
        String userInfo = userToJson(user);

        userRepository.delete(user);

        // 记录操作日志
        String operationContent = String.format("删除用户: %s(%s)", user.getAccount(), user.getName());
        operationLogService.logUserOperation("delete", currentUser, id,
                operationContent, userInfo, null, "all", getClientIp());

        log.warn("用户 {} 删除了用户 {}", currentUser.getAccount(), id);
    }


    // 检查账号是否存在（添加权限检查）
    public boolean isAccountExists(String account, SystemUserAccounts currentUser) {
        log.debug("用户 {} 检查账号是否存在: {}", currentUser.getAccount(), account);
        return userRepository.existsByAccount(account);
    }

    // 记录用户变更信息（为后续的审计日志准备）
    private void logUserChanges(SystemUserAccounts oldUser, SystemUserAccounts newUser, SystemUserAccounts operator) {
        // 这里可以记录详细的变更信息
        // 后续可以集成到审计日志系统中
        log.info("用户变更 - 操作人: {}, 目标用户: {}, 变更字段: {}",
                operator.getAccount(),
                oldUser.getId(),
                getChangedFields(oldUser, newUser));
    }

    // 获取变更的字段列表
    private String getChangedFields(SystemUserAccounts oldUser, SystemUserAccounts newUser) {
        StringBuilder changes = new StringBuilder();

        if (!equals(oldUser.getName(), newUser.getName())) {
            changes.append("姓名,");
        }
        if (!equals(oldUser.getEmail(), newUser.getEmail())) {
            changes.append("邮箱,");
        }
        if (!equals(oldUser.getMobile(), newUser.getMobile())) {
            changes.append("手机,");
        }
        if (!equals(oldUser.getStatus(), newUser.getStatus())) {
            changes.append("状态,");
        }
        if (!equals(oldUser.getRole(), newUser.getRole())) {
            changes.append("角色,");
        }

        return changes.length() > 0 ? changes.substring(0, changes.length() - 1) : "无变更";
    }

    private boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) return true;
        if (obj1 == null || obj2 == null) return false;
        return obj1.equals(obj2);
    }
}