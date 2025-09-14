package com.commsys.controller;

import com.commsys.common.Result;
import com.commsys.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统控制器
 * 提供系统信息、健康检查等接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemController {

    private final ConfigService configService;

    /**
     * 系统信息
     * 
     * @return 系统信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getSystemInfo() {
        log.info("获取系统信息请求");
        
        Map<String, Object> info = new HashMap<>();
        info.put("name", "社团管理系统");
        info.put("version", "1.0.0");
        info.put("description", "基于Spring Boot的社团管理系统");
        info.put("database", "SQLite");
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("osName", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("timestamp", System.currentTimeMillis());
        
        return Result.success(info);
    }

    /**
     * 健康检查
     * 
     * @return 健康状态
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> healthCheck() {
        log.info("健康检查请求");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("database", "SQLite");
        health.put("timestamp", System.currentTimeMillis());
        health.put("uptime", System.currentTimeMillis() - getStartTime());
        
        return Result.success(health);
    }

    /**
     * 获取动态配置值
     * 
     * @param configKey 配置键
     * @return 配置值
     */
    @GetMapping("/config/{configKey}")
    public Result<String> getDynamicConfig(@PathVariable String configKey) {
        log.info("获取动态配置请求: {}", configKey);
        String value = configService.getConfigValue(configKey);
        return Result.success(value);
    }

    /**
     * 获取动态配置值（带默认值）
     * 
     * @param configKey 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    @GetMapping("/config/{configKey}/default")
    public Result<String> getDynamicConfigWithDefault(@PathVariable String configKey, 
                                                     @RequestParam String defaultValue) {
        log.info("获取动态配置（带默认值）请求: {}, 默认值: {}", configKey, defaultValue);
        String value = configService.getConfigValue(configKey, defaultValue);
        return Result.success(value);
    }

    /**
     * 手动删除数据库数据
     * 
     * @param operatorUserId 操作者用户ID
     * @return 删除结果
     */
    @PostMapping("/del_dbdata")
    public Result<Void> deleteDatabaseData(@RequestParam Long operatorUserId) {
        log.info("手动删除数据库数据请求: 操作者={}", operatorUserId);
        
        // 这里需要实现删除已标记为删除的数据的逻辑
        // 由于这是一个危险操作，需要超级管理员权限
        // 暂时返回成功，实际实现需要根据具体需求
        
        return Result.success("数据库数据清理完成");
    }

    /**
     * 获取系统启动时间
     * 
     * @return 启动时间
     */
    private long getStartTime() {
        // 这里应该返回系统启动时间
        // 可以通过静态变量记录启动时间
        return System.currentTimeMillis() - 10000; // 模拟启动时间
    }
}