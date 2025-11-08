package com.mdas.server.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * API访问日志表 - 记录所有API接口的访问情况
 */
@Entity
@Table(name = "system_api_access_logs")
@Data
public class SystemApiAccessLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id", nullable = false, length = 64)
    private String traceId;

    // 请求信息
    @Column(name = "request_method", nullable = false, length = 10)
    private String requestMethod; // GET, POST, PUT, DELETE

    @Column(name = "request_uri", nullable = false, columnDefinition = "TEXT")
    private String requestUri;

    @Column(name = "request_query_string", columnDefinition = "TEXT")
    private String requestQueryString;

    @Column(name = "request_headers", columnDefinition = "TEXT")
    private String requestHeaders;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;

    @Column(name = "request_ip", nullable = false, length = 45)
    private String requestIp;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    // 用户信息
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_account", length = 50)
    private String userAccount;

    @Column(name = "user_name", length = 100)
    private String userName;

    // 响应信息
    @Column(name = "response_status", nullable = false)
    private Integer responseStatus;

    @Column(name = "response_headers", columnDefinition = "TEXT")
    private String responseHeaders;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    // 性能信息
    @Column(name = "request_time", nullable = false)
    private LocalDateTime requestTime;

    @Column(name = "response_time")
    private LocalDateTime responseTime;

    @Column(name = "cost_time", nullable = false)
    private Long costTime;

    // 服务信息
    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName = "mdas-server";

    @Column(name = "handler_method", length = 200)
    private String handlerMethod;

    // 错误信息
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "exception_stack_trace", columnDefinition = "TEXT")
    private String exceptionStackTrace;

    // 附加信息
    @Column(name = "tags", length = 200)
    private String tags;

    @PrePersist
    protected void onCreate() {
        if (requestTime == null) {
            requestTime = LocalDateTime.now();
        }
        if (serviceName == null) {
            serviceName = "mdas-server";
        }
    }

    // 便捷方法
    public boolean isSuccess() {
        return responseStatus >= 200 && responseStatus < 300;
    }

    public boolean isClientError() {
        return responseStatus >= 400 && responseStatus < 500;
    }

    public boolean isServerError() {
        return responseStatus >= 500;
    }

    public boolean isSlowRequest(long slowThreshold) {
        return costTime > slowThreshold;
    }

    // 计算耗时
    public void calculateCostTime() {
        if (responseTime != null && requestTime != null) {
            this.costTime = java.time.Duration.between(requestTime, responseTime).toMillis();
        }
    }

    // 添加标签
    public void addTag(String tag) {
        if (tags == null || tags.isEmpty()) {
            tags = tag;
        } else {
            tags = tags + "," + tag;
        }
    }

    // 检查是否包含标签
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    // 获取请求的简要信息
    public String getRequestSummary() {
        return String.format("%s %s - %d (%dms)",
                requestMethod, requestUri, responseStatus, costTime);
    }

    // 判断是否需要记录请求体（避免记录大文件上传等）
    public boolean shouldLogRequestBody() {
        if (requestBody == null) return true;
        // 不记录超过10KB的请求体
        return requestBody.length() <= 10240;
    }

    // 判断是否需要记录响应体（避免记录大文件下载等）
    public boolean shouldLogResponseBody() {
        if (responseBody == null) return true;
        // 不记录超过20KB的响应体
        return responseBody.length() <= 20480;
    }
}