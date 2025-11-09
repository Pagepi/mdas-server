package com.mdas.server.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限制的key，支持SpEL表达式
     * 例如: #ipAddress, #userId, #request.getRemoteAddr()
     */
    String key() default "";

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 时间间隔
     */
    int time() default 1;

    /**
     * 允许的请求次数
     */
    int limit() default 10;

    /**
     * 限制提示信息
     */
    String message() default "请求过于频繁，请稍后再试";
}