package com.commsys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统配置实体类
 * 用于存储系统配置信息，支持动态修改
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Data
@Entity
@Table(name = "configs")
@EqualsAndHashCode(callSuper = true)
public class Config extends BaseEntity {


    /**
     * 配置键
     */
    @NotBlank(message = "配置键不能为空")
    @Size(max = 100, message = "配置键长度不能超过100个字符")
    @Column(name = "config_key", unique = true, nullable = false, length = 100)
    private String configKey;

    /**
     * 配置值
     */
    @NotBlank(message = "配置值不能为空")
    @Size(max = 1000, message = "配置值长度不能超过1000个字符")
    @Column(name = "config_value", nullable = false, length = 1000)
    private String configValue;

    /**
     * 配置描述
     */
    @Size(max = 500, message = "配置描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 配置类型：STRING-字符串，NUMBER-数字，BOOLEAN-布尔值，JSON-JSON对象
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "config_type", nullable = false)
    private ConfigType configType = ConfigType.STRING;

    /**
     * 配置分组
     */
    @Size(max = 50, message = "配置分组长度不能超过50个字符")
    @Column(name = "config_group", length = 50)
    private String configGroup = "DEFAULT";

    /**
     * 是否可修改：true-可修改，false-不可修改
     */
    @Column(name = "is_modifiable", nullable = false)
    private Boolean isModifiable = true;

    /**
     * 配置类型枚举
     */
    public enum ConfigType {
        STRING, NUMBER, BOOLEAN, JSON
    }
}
