package com.mdas.server.controller;

import com.mdas.server.entity.SystemUserAccounts;
import com.mdas.server.repository.SystemUserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private SystemUserAccountRepository userAccountRepository;

    @GetMapping("/")
    public String home() {
        return "MDAS Server is running! 点科源服务器运行正常！";
    }

    // 测试数据库连接 - 查询所有用户
    @GetMapping("/db-test")
    public String dbTest() {
        try {
            List<SystemUserAccounts> users = userAccountRepository.findAll();
            return "数据库连接成功！用户数量: " + users.size();
        } catch (Exception e) {
            return "数据库连接失败: " + e.getMessage();
        }
    }

    // 测试插入数据
    @GetMapping("/db-insert")
    public String dbInsert() {
        try {
            SystemUserAccounts user = new SystemUserAccounts();
            user.setAccount("testuser");
            user.setName("测试用户");
            user.setPassword("123456");
            user.setStatus("active");

            SystemUserAccounts savedUser = userAccountRepository.save(user);
            return "数据插入成功！用户ID: " + savedUser.getId();
        } catch (Exception e) {
            return "数据插入失败: " + e.getMessage();
        }
    }

    // 统计用户数量
    @GetMapping("/user-count")
    public String userCount() {
        long count = userAccountRepository.count();
        return "数据库中的用户总数: " + count;
    }
}