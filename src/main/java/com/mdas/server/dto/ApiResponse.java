package com.mdas.server.dto;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一API响应格式
 */
@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private Integer code;
    private T data;
    private Long timestamp;

    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("操作成功");
        response.setCode(200);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    // 失败响应 - 使用泛型保持类型一致
    public static <T> ApiResponse<T> error(String message) {
        return error(message, 400);
    }

    public static <T> ApiResponse<T> error(String message, Integer code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setCode(code);
        response.setData(null);
        return response;
    }

    // 带数据的失败响应
    public static <T> ApiResponse<T> error(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setCode(400);
        response.setData(data);
        return response;
    }
}