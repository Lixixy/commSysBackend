package com.commsys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Token实体类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Data
@Entity
@Table(name = "tokens")
@EqualsAndHashCode(callSuper = true)
public class Token extends BaseEntity {


    /**
     * Token值
     */
    @NotBlank(message = "Token不能为空")
    @Column(name = "token_value", unique = true, nullable = false, length = 255)
    private String tokenValue;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空")
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Token状态：0-已过期，1-有效
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 是否可参考：0-不可参考，1-可参考
     */
    @Column(name = "is_reference", nullable = false)
    private Integer isReference = 1;
}
