package com.commsys.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置属性类
 * 用于管理应用级别的配置参数
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    /**
     * 数据库配置
     */
    private Database database = new Database();

    /**
     * 分页配置
     */
    private Page page = new Page();

    /**
     * 文件上传配置
     */
    private Upload upload = new Upload();

    @Data
    public static class Database {
        /**
         * 数据库类型：mysql 或 sqlite
         */
        private String type = "sqlite";
    }

    @Data
    public static class Page {
        /**
         * 默认分页大小
         */
        private Integer defaultSize = 10;

        /**
         * 最大分页大小
         */
        private Integer maxSize = 100;
    }

    @Data
    public static class Upload {
        /**
         * 文件上传路径
         */
        private String path = "./uploads";

        /**
         * 最大文件大小
         */
        private String maxSize = "10MB";
    }
}