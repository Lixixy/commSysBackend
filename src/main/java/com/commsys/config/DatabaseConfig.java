package com.commsys.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库配置类
 * 支持MySQL和SQLite数据库的动态切换
 * 
 * @author CommonSys
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@Configuration
public class DatabaseConfig {

    @Value("${app.database.type:sqlite}")
    private String databaseType;

    /**
     * 记录数据库类型配置
     */
    public DatabaseConfig() {
        log.info("数据库配置类初始化完成");
    }
}
