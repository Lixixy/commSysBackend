package com.commsys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {


    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 密码hash
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 32, max = 128, message = "密码hash长度必须在32-128个字符之间")
    @Column(name = "password_hash", nullable = false, length = 128)
    private String passwordHash;

    /**
     * 性别：0（未填写）、1（男）、2（女）
     */
    @NotNull(message = "性别不能为空")
    @Column(name = "gender", nullable = false)
    private Integer gender = 0;

    /**
     * 积分
     */
    @Column(name = "points", nullable = false)
    private Integer points = 0;

    /**
     * 父社团ID（-1表示无社团）
     */
    @Column(name = "parent_club_id", nullable = false)
    private Long parentClubId = -1L;

    /**
     * 身份ID：0（无社团学生）、1（社团成员）、2（社长）、3（老师）、4（管理员）、5（超级管理员）
     */
    @NotNull(message = "身份不能为空")
    @Column(name = "role_id", nullable = false)
    private Integer roleId = 0;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Column(name = "email", unique = true, length = 100)
    private String email;

    /**
     * 手机号
     */
    @Size(max = 20, message = "手机号长度不能超过20个字符")
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 真实姓名
     */
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    @Column(name = "real_name", length = 50)
    private String realName;

    /**
     * 用户状态：0-禁用，1-启用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Column(name = "remark", length = 500)
    private String remark;
}