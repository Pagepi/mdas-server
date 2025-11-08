package com.mdas.server.config;

import com.mdas.server.interceptor.AuthInterceptor;
import com.mdas.server.interceptor.LoggingInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类 - 注册拦截器
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 先注册日志拦截器（最前面）
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**");  // 拦截所有请求

        // 注册认证拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")                    // 拦截所有API接口
                .excludePathPatterns("/api/auth/**")           // 排除认证相关接口
                .excludePathPatterns("/api/test/**")           // 排除测试接口
                .excludePathPatterns("/swagger-ui/**")         // 排除Swagger UI
                .excludePathPatterns("/v3/api-docs/**")        // 排除API文档
                .excludePathPatterns("/error");                // 排除错误页面

//        log.info("认证拦截器注册完成 - 拦截路径: /api/**");
//        log.info("=== 拦截器注册完成 ===");
//        log.info("拦截路径: /api/**");
//        log.info("排除路径: /api/auth/**, /api/test/**, /swagger-ui/**, /v3/api-docs/**, /error");
    }
}