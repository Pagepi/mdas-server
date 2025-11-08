package com.mdas.server.dto;
// package com.mdas.server.dto;

import lombok.Data;
import java.util.List;

/**
 * 用户菜单权限响应DTO（用于登录后获取用户菜单）
 */
@Data
public class UserMenuPermissionResponse {
    private String userAccount;
    private String userName;
    private List<MenuResponse> menus; // 用户有权限访问的菜单树
    private List<String> permissions; // 用户的所有权限编码列表
}