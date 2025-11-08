package com.mdas.server.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private UserInfo user;
    private Boolean suspicious; // 新增：是否可疑登录

    public LoginResponse() {}

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponse(boolean success, String message, UserInfo user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    @Data
    public static class UserInfo {
        private Integer id;
        private String account;
        private String name;
        private String email;
        private String mobile;

        public UserInfo() {}

        public UserInfo(Integer id, String account, String name, String email, String mobile) {
            this.id = id;
            this.account = account;
            this.name = name;
            this.email = email;
            this.mobile = mobile;
        }
    }
}