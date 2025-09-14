package com.commsys.controller;

import com.commsys.common.PageResult;
import com.commsys.common.Result;
import com.commsys.entity.Config;
import com.commsys.service.ConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置控制器
 * 提供系统配置管理的REST API接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    /**
     * 创建配置
     * 
     * @param config 配置信息
     * @return 创建结果
     */
    @PostMapping
    public Result<Config> createConfig(@Valid @RequestBody Config config) {
        log.info("创建配置请求: {}", config.getConfigKey());
        Config createdConfig = configService.createConfig(config);
        return Result.success("配置创建成功", createdConfig);
    }

    /**
     * 根据ID获取配置
     * 
     * @param id 配置ID
     * @return 配置信息
     */
    @GetMapping("/{id}")
    public Result<Config> getConfigById(@PathVariable Long id) {
        log.info("获取配置请求: {}", id);
        Config config = configService.getConfigById(id);
        return Result.success(config);
    }

    /**
     * 根据配置键获取配置
     * 
     * @param configKey 配置键
     * @return 配置信息
     */
    @GetMapping("/key/{configKey}")
    public Result<Config> getConfigByKey(@PathVariable String configKey) {
        log.info("根据配置键获取配置请求: {}", configKey);
        Config config = configService.getConfigByKey(configKey);
        return Result.success(config);
    }

    /**
     * 根据配置键获取配置值
     * 
     * @param configKey 配置键
     * @return 配置值
     */
    @GetMapping("/value/{configKey}")
    public Result<String> getConfigValue(@PathVariable String configKey) {
        log.info("获取配置值请求: {}", configKey);
        String value = configService.getConfigValue(configKey);
        return Result.success(value);
    }

    /**
     * 根据配置键获取配置值（带默认值）
     * 
     * @param configKey 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    @GetMapping("/value/{configKey}/default")
    public Result<String> getConfigValueWithDefault(@PathVariable String configKey, 
                                                   @RequestParam String defaultValue) {
        log.info("获取配置值（带默认值）请求: {}, 默认值: {}", configKey, defaultValue);
        String value = configService.getConfigValue(configKey, defaultValue);
        return Result.success(value);
    }

    /**
     * 更新配置
     * 
     * @param id 配置ID
     * @param config 配置信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Config> updateConfig(@PathVariable Long id, @Valid @RequestBody Config config) {
        log.info("更新配置请求: {}", id);
        config.setId(id);
        Config updatedConfig = configService.updateConfig(config);
        return Result.success("配置更新成功", updatedConfig);
    }

    /**
     * 更新配置值
     * 
     * @param configKey 配置键
     * @param configValue 配置值
     * @return 更新结果
     */
    @PutMapping("/value/{configKey}")
    public Result<Config> updateConfigValue(@PathVariable String configKey, 
                                          @RequestParam String configValue) {
        log.info("更新配置值请求: {} = {}", configKey, configValue);
        Config config = configService.updateConfigValue(configKey, configValue);
        return Result.success("配置值更新成功", config);
    }

    /**
     * 删除配置
     * 
     * @param id 配置ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteConfig(@PathVariable Long id) {
        log.info("删除配置请求: {}", id);
        configService.deleteConfig(id);
        return Result.success("配置删除成功");
    }

    /**
     * 批量删除配置
     * 
     * @param ids 配置ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public Result<Void> deleteConfigs(@RequestBody List<Long> ids) {
        log.info("批量删除配置请求: {}", ids);
        configService.deleteConfigs(ids);
        return Result.success("配置批量删除成功");
    }

    /**
     * 分页查询配置
     * 
     * @param page 页码
     * @param size 每页大小
     * @param configKey 配置键关键字
     * @param configGroup 配置分组
     * @param configType 配置类型
     * @return 分页配置列表
     */
    @GetMapping
    public Result<PageResult<Config>> getConfigs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String configKey,
            @RequestParam(required = false) String configGroup,
            @RequestParam(required = false) Config.ConfigType configType) {
        log.info("分页查询配置请求: page={}, size={}, configKey={}, configGroup={}, configType={}", 
                page, size, configKey, configGroup, configType);
        PageResult<Config> pageResult = configService.getConfigs(page, size, configKey, configGroup, configType);
        return Result.success(pageResult);
    }

    /**
     * 获取所有配置
     * 
     * @return 配置列表
     */
    @GetMapping("/all")
    public Result<List<Config>> getAllConfigs() {
        log.info("获取所有配置请求");
        List<Config> configs = configService.getAllConfigs();
        return Result.success(configs);
    }

    /**
     * 根据配置分组获取配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    @GetMapping("/group/{configGroup}")
    public Result<List<Config>> getConfigsByGroup(@PathVariable String configGroup) {
        log.info("根据配置分组获取配置请求: {}", configGroup);
        List<Config> configs = configService.getConfigsByGroup(configGroup);
        return Result.success(configs);
    }

    /**
     * 根据配置类型获取配置列表
     * 
     * @param configType 配置类型
     * @return 配置列表
     */
    @GetMapping("/type/{configType}")
    public Result<List<Config>> getConfigsByType(@PathVariable Config.ConfigType configType) {
        log.info("根据配置类型获取配置请求: {}", configType);
        List<Config> configs = configService.getConfigsByType(configType);
        return Result.success(configs);
    }

    /**
     * 获取所有配置分组
     * 
     * @return 配置分组列表
     */
    @GetMapping("/groups")
    public Result<List<String>> getAllConfigGroups() {
        log.info("获取所有配置分组请求");
        List<String> groups = configService.getAllConfigGroups();
        return Result.success(groups);
    }

    /**
     * 检查配置键是否存在
     * 
     * @param configKey 配置键
     * @return 检查结果
     */
    @GetMapping("/check/key/{configKey}")
    public Result<Boolean> checkConfigKey(@PathVariable String configKey) {
        log.info("检查配置键请求: {}", configKey);
        boolean exists = configService.existsByConfigKey(configKey);
        return Result.success(exists);
    }

    /**
     * 初始化默认配置
     * 
     * @return 初始化结果
     */
    @PostMapping("/init")
    public Result<Void> initDefaultConfigs() {
        log.info("初始化默认配置请求");
        configService.initDefaultConfigs();
        return Result.success("默认配置初始化成功");
    }
}