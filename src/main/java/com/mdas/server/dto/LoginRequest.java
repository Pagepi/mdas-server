package com.mdas.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 20, message = "账号长度3-20位")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码至少6位")
    private String password;

    // 构造方法
    public LoginRequest() {}

    public LoginRequest(String account, String password) {
        this.account = account;
        this.password = password;
    }

    // Getter和Setter
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}