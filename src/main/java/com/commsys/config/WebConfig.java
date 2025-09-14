package com.commsys.config;

import com.commsys.config.TokenInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web配置类
 * 配置CORS、JSON序列化、Token拦截器等Web相关功能
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private TokenInterceptor tokenInterceptor;

    /**
     * 配置CORS跨域
     * 
     * @param registry CORS注册器
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置JSON序列化
     * 
     * @return ObjectMapper实例
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * 配置HTTP消息转换器
     * 
     * @param converters 消息转换器列表
     */
    @Override
    public void configureMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        converters.add(converter);
    }
    
    /**
     * 注册拦截器
     * 
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 添加Token拦截器
        registry.addInterceptor(tokenInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除不需要拦截的请求
                .excludePathPatterns(
                        "/usr/login",
                        "/usr/register",
                        "/usr/check/**",
                        "/sys/init"
                );
    }
}