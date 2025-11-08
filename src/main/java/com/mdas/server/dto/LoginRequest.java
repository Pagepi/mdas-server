package com.mdas.server.dto;

public class LoginRequest {
    private String account;
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