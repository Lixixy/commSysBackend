package com.commsys.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记需要认证的API接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {
    
    /**
     * 是否需要验证
     * @return 是否需要验证
     */
    boolean required() default true;
}