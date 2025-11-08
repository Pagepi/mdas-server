package com.mdas.server.repository;

import com.mdas.server.entity.SystemOperationLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends JpaRepository<SystemOperationLogs, Long>,
        JpaSpecificationExecutor<SystemOperationLogs> {
}