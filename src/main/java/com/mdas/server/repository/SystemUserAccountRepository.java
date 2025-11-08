package com.mdas.server.repository;

import com.mdas.server.entity.SystemUserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SystemUserAccountRepository extends JpaRepository<SystemUserAccounts, Integer> {

    // 根据账号查找用户（Spring Data JPA 会根据方法名自动生成SQL）
    Optional<SystemUserAccounts> findByAccount(String account);
    Optional<SystemUserAccounts> findByEmail(String email);
    // 检查账号是否存在
    boolean existsByAccount(String account);
    // 根据邮箱查找用户
    List<SystemUserAccounts> findByStatus(String status);
    List<SystemUserAccounts> findByRole(String role);
}