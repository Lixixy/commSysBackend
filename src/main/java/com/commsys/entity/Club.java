package com.commsys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 社团实体类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Data
@Entity
@Table(name = "clubs")
@EqualsAndHashCode(callSuper = true)
public class Club extends BaseEntity {


    /**
     * 社团标题
     */
    @NotBlank(message = "社团标题不能为空")
    @Size(max = 100, message = "社团标题长度不能超过100个字符")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 社团简介
     */
    @Size(max = 1000, message = "社团简介长度不能超过1000个字符")
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 社长用户ID
     */
    @NotNull(message = "社长不能为空")
    @Column(name = "president_id", nullable = false)
    private Long presidentId;

    /**
     * 社团指导老师ID
     */
    @Column(name = "teacher_id")
    private Long teacherId;

    /**
     * 社团状态：0-禁用，1-启用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 禁用原因
     */
    @Size(max = 500, message = "禁用原因长度不能超过500个字符")
    @Column(name = "disable_reason", length = 500)
    private String disableReason;

    /**
     * 社团成员列表（不存储在数据库中，通过关联查询获取）
     */
    @Transient
    private List<Long> memberIds;
}
