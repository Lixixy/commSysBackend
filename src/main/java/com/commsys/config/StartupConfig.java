package com.commsys.config;

import com.commsys.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动配置类
 * 在系统启动时执行初始化操作
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupConfig implements CommandLineRunner {

    private final ConfigService configService;

    /**
     * 系统启动时执行
     * 
     * @param args 启动参数
     */
    @Override
    public void run(String... args) {
        log.info("系统启动中，开始初始化配置...");
        
        try {
            // 等待数据库连接稳定
            Thread.sleep(1000);
            
            // 初始化默认配置
            configService.initDefaultConfigs();
            log.info("默认配置初始化完成");
        } catch (Exception e) {
            log.error("默认配置初始化失败，系统将继续启动", e);
            // 不抛出异常，让系统继续启动
        }
        
        log.info("系统启动完成");
    }
}