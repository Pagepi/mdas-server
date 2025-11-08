package com.mdas.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器 - 统一处理所有控制器异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(
            RuntimeException e, HttpServletRequest request) {

        log.warn("业务异常: {} - {}", request.getRequestURI(), e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", e.getMessage());
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("path", request.getRequestURI());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.warn("参数验证失败: {} - {}", request.getRequestURI(), errors);

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "参数验证失败");
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);
        response.put("path", request.getRequestURI());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理资源未找到异常（Spring 6新增）
     */
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(
            org.springframework.web.servlet.resource.NoResourceFoundException e, HttpServletRequest request) {

        log.warn("资源未找到: {} {}", e.getHttpMethod(), e.getResourcePath());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "接口不存在");
        response.put("code", HttpStatus.NOT_FOUND.value());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 处理参数类型转换异常
     */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatchException(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException e, HttpServletRequest request) {

        String errorMessage = String.format("参数 '%s' 类型错误: 需要 %s, 但收到 '%s'",
                e.getName(), e.getRequiredType().getSimpleName(), e.getValue());

        log.warn("参数类型错误: {} - {}", request.getRequestURI(), errorMessage);

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", errorMessage);
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("path", request.getRequestURI());

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(
            NoHandlerFoundException e, HttpServletRequest request) {

        log.warn("接口不存在: {} {}", e.getHttpMethod(), e.getRequestURL());

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "接口不存在");
        response.put("code", HttpStatus.NOT_FOUND.value());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllException(
            Exception e, HttpServletRequest request) {

        log.error("系统异常: {} - {}", request.getRequestURI(), e.getMessage(), e);

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "系统内部错误");
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("path", request.getRequestURI());

        // 生产环境可以隐藏详细错误信息
        if (isDevelopment()) {
            response.put("error", e.getMessage());
            response.put("exception", e.getClass().getSimpleName());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private boolean isDevelopment() {
        // 这里可以根据环境配置判断是否为开发环境
        return true; // 暂时返回true便于调试
    }
}