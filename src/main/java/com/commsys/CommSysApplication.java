package com.commsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 社团系统主启动类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties
public class CommSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommSysApplication.class, args);
    }
}
