package com.mdas.server.util;

import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.repository.SystemUserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordMigrationRunner implements CommandLineRunner {

    @Autowired
    private SystemUserAccountRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<SystemUserAccounts> users = userRepository.findAll();
        int migratedCount = 0;

        for (SystemUserAccounts user : users) {
            String currentPassword = user.getPassword();

            // 检测条件：不是新的加密格式（新的格式包含冒号）
            if (currentPassword != null && !currentPassword.contains(":")) {
                System.out.println(" migrating password for user: " + user.getAccount());

                // 重置为默认密码（使用新的加密方式）
                user.setPassword(passwordEncoder.encode("123456"));
                userRepository.save(user);
                migratedCount++;
            }
        }

        if (migratedCount > 0) {
            System.out.println(" Password migration completed. " + migratedCount + " users reset to '123456'");
        } else {
            System.out.println(" No password migration needed.");
        }
    }
}