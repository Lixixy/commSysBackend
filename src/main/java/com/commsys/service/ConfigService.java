package com.commsys.service;

import com.commsys.common.PageResult;
import com.commsys.entity.Config;
import com.commsys.exception.BusinessException;
import com.commsys.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 配置服务类
 * 提供系统配置的动态管理功能
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;

    /**
     * 创建配置
     * 
     * @param config 配置信息
     * @return 创建的配置
     */
    @Transactional
    public Config createConfig(Config config) {
        log.info("创建配置: {}", config.getConfigKey());
        
        // 检查配置键是否已存在
        if (configRepository.existsByConfigKey(config.getConfigKey())) {
            throw new BusinessException("配置键已存在");
        }
        
        return configRepository.save(config);
    }

    /**
     * 根据ID获取配置
     * 
     * @param id 配置ID
     * @return 配置信息
     */
    public Config getConfigById(Long id) {
        log.info("根据ID获取配置: {}", id);
        return configRepository.findActiveById(id)
                .orElseThrow(() -> new BusinessException("配置不存在"));
    }

    /**
     * 根据配置键获取配置
     * 
     * @param configKey 配置键
     * @return 配置信息
     */
    public Config getConfigByKey(String configKey) {
        log.info("根据配置键获取配置: {}", configKey);
        return configRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new BusinessException("配置不存在"));
    }

    /**
     * 根据配置键获取配置值
     * 
     * @param configKey 配置键
     * @return 配置值
     */
    public String getConfigValue(String configKey) {
        log.info("获取配置值: {}", configKey);
        return configRepository.findConfigValueByKey(configKey)
                .orElseThrow(() -> new BusinessException("配置不存在"));
    }

    /**
     * 根据配置键获取配置值（带默认值）
     * 
     * @param configKey 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String getConfigValue(String configKey, String defaultValue) {
        log.info("获取配置值（带默认值）: {}, 默认值: {}", configKey, defaultValue);
        return configRepository.findConfigValueByKey(configKey)
                .orElse(defaultValue);
    }

    /**
     * 更新配置
     * 
     * @param config 配置信息
     * @return 更新后的配置
     */
    @Transactional
    public Config updateConfig(Config config) {
        log.info("更新配置: {}", config.getConfigKey());
        
        Config existingConfig = getConfigById(config.getId());
        
        // 检查配置是否可修改
        if (!existingConfig.getIsModifiable()) {
            throw new BusinessException("配置不可修改");
        }
        
        // 检查配置键是否被其他配置使用
        if (!existingConfig.getConfigKey().equals(config.getConfigKey()) && 
            configRepository.existsByConfigKey(config.getConfigKey())) {
            throw new BusinessException("配置键已被其他配置使用");
        }
        
        // 更新配置信息
        existingConfig.setConfigKey(config.getConfigKey());
        existingConfig.setConfigValue(config.getConfigValue());
        existingConfig.setDescription(config.getDescription());
        existingConfig.setConfigType(config.getConfigType());
        existingConfig.setConfigGroup(config.getConfigGroup());
        
        return configRepository.save(existingConfig);
    }

    /**
     * 更新配置值
     * 
     * @param configKey 配置键
     * @param configValue 配置值
     * @return 更新后的配置
     */
    @Transactional
    public Config updateConfigValue(String configKey, String configValue) {
        log.info("更新配置值: {} = {}", configKey, configValue);
        
        Config config = getConfigByKey(configKey);
        
        // 检查配置是否可修改
        if (!config.getIsModifiable()) {
            throw new BusinessException("配置不可修改");
        }
        
        config.setConfigValue(configValue);
        return configRepository.save(config);
    }

    /**
     * 删除配置（逻辑删除）
     * 
     * @param id 配置ID
     */
    @Transactional
    public void deleteConfig(Long id) {
        log.info("删除配置: {}", id);
        Config config = getConfigById(id);
        
        // 检查配置是否可修改
        if (!config.getIsModifiable()) {
            throw new BusinessException("配置不可删除");
        }
        
        configRepository.softDeleteById(id);
    }

    /**
     * 批量删除配置
     * 
     * @param ids 配置ID列表
     */
    @Transactional
    public void deleteConfigs(List<Long> ids) {
        log.info("批量删除配置: {}", ids);
        
        // 检查所有配置是否可删除
        for (Long id : ids) {
            Config config = getConfigById(id);
            if (!config.getIsModifiable()) {
                throw new BusinessException("配置 " + config.getConfigKey() + " 不可删除");
            }
        }
        
        configRepository.softDeleteByIds(ids);
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
    public PageResult<Config> getConfigs(Integer page, Integer size, String configKey, 
                                       String configGroup, Config.ConfigType configType) {
        log.info("分页查询配置: page={}, size={}, configKey={}, configGroup={}, configType={}", 
                page, size, configKey, configGroup, configType);
        
        // 创建分页参数
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "configKey"));
        
        Page<Config> configPage;
        
        if (StringUtils.hasText(configKey) && StringUtils.hasText(configGroup)) {
            configPage = configRepository.findByConfigGroupAndConfigKeyContaining(configGroup, configKey, pageable);
        } else if (StringUtils.hasText(configKey)) {
            configPage = configRepository.findByConfigKeyContaining(configKey, pageable);
        } else if (StringUtils.hasText(configGroup)) {
            List<Config> configs = configRepository.findByConfigGroup(configGroup);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), configs.size());
            List<Config> pageContent = configs.subList(start, end);
            configPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, configs.size());
        } else if (configType != null) {
            List<Config> configs = configRepository.findByConfigType(configType);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), configs.size());
            List<Config> pageContent = configs.subList(start, end);
            configPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, configs.size());
        } else {
            configPage = configRepository.findAllActive(pageable);
        }
        
        return PageResult.of(configPage);
    }

    /**
     * 获取所有配置
     * 
     * @return 配置列表
     */
    public List<Config> getAllConfigs() {
        log.info("获取所有配置");
        return configRepository.findAllActive();
    }

    /**
     * 根据配置分组获取配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    public List<Config> getConfigsByGroup(String configGroup) {
        log.info("根据配置分组获取配置列表: {}", configGroup);
        return configRepository.findByConfigGroup(configGroup);
    }

    /**
     * 根据配置类型获取配置列表
     * 
     * @param configType 配置类型
     * @return 配置列表
     */
    public List<Config> getConfigsByType(Config.ConfigType configType) {
        log.info("根据配置类型获取配置列表: {}", configType);
        return configRepository.findByConfigType(configType);
    }

    /**
     * 获取所有配置分组
     * 
     * @return 配置分组列表
     */
    public List<String> getAllConfigGroups() {
        log.info("获取所有配置分组");
        return configRepository.findAllConfigGroups();
    }

    /**
     * 检查配置键是否存在
     * 
     * @param configKey 配置键
     * @return 是否存在
     */
    public boolean existsByConfigKey(String configKey) {
        return configRepository.existsByConfigKey(configKey);
    }

    /**
     * 初始化默认配置
     * 在系统启动时调用，创建必要的默认配置
     */
    public void initDefaultConfigs() {
        log.info("初始化默认配置");
        
        // 系统配置
        createConfigIfNotExists("system.name", "社团管理系统", "系统名称", Config.ConfigType.STRING, "SYSTEM");
        createConfigIfNotExists("system.version", "1.0.0", "系统版本", Config.ConfigType.STRING, "SYSTEM");
        createConfigIfNotExists("system.debug", "false", "调试模式", Config.ConfigType.BOOLEAN, "SYSTEM");
        
        // 数据库配置
        createConfigIfNotExists("database.type", "sqlite", "数据库类型", Config.ConfigType.STRING, "DATABASE");
        createConfigIfNotExists("database.pool.max", "20", "最大连接数", Config.ConfigType.NUMBER, "DATABASE");
        createConfigIfNotExists("database.pool.min", "5", "最小连接数", Config.ConfigType.NUMBER, "DATABASE");
        
        // 分页配置
        createConfigIfNotExists("page.default.size", "10", "默认分页大小", Config.ConfigType.NUMBER, "PAGE");
        createConfigIfNotExists("page.max.size", "100", "最大分页大小", Config.ConfigType.NUMBER, "PAGE");
        
        // 文件上传配置
        createConfigIfNotExists("upload.path", "./uploads", "文件上传路径", Config.ConfigType.STRING, "UPLOAD");
        createConfigIfNotExists("upload.max.size", "10MB", "最大文件大小", Config.ConfigType.STRING, "UPLOAD");
        
        // Token配置
        createConfigIfNotExists("token.expire.hours", "24", "Token过期时间（小时）", Config.ConfigType.NUMBER, "TOKEN");
        createConfigIfNotExists("token.cleanup.interval", "3600", "Token清理间隔（秒）", Config.ConfigType.NUMBER, "TOKEN");
        
        log.info("默认配置初始化成功");
    }

    /**
     * 创建配置（如果不存在）
     * 
     * @param configKey 配置键
     * @param configValue 配置值
     * @param description 描述
     * @param configType 配置类型
     * @param configGroup 配置分组
     */
    @Transactional
    private void createConfigIfNotExists(String configKey, String configValue, String description, 
                                       Config.ConfigType configType, String configGroup) {
        try {
            if (!configRepository.existsByConfigKey(configKey)) {
                Config config = new Config();
                config.setConfigKey(configKey);
                config.setConfigValue(configValue);
                config.setDescription(description);
                config.setConfigType(configType);
                config.setConfigGroup(configGroup);
                config.setIsModifiable(true);
                configRepository.save(config);
                log.info("创建默认配置: {} = {}", configKey, configValue);
            } else {
                log.debug("配置已存在，跳过创建: {}", configKey);
            }
        } catch (Exception e) {
            log.error("创建配置失败: {}, 错误: {}", configKey, e.getMessage());
            // 不抛出异常，继续创建其他配置
        }
    }
}