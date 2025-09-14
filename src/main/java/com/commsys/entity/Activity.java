package com.commsys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 活动实体类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Data
@Entity
@Table(name = "activities")
@EqualsAndHashCode(callSuper = true)
public class Activity extends BaseEntity {


    /**
     * 社团ID
     */
    @NotNull(message = "社团ID不能为空")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    /**
     * 发起者用户ID
     */
    @NotNull(message = "发起者不能为空")
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    /**
     * 活动标题
     */
    @NotBlank(message = "活动标题不能为空")
    @Size(max = 100, message = "活动标题长度不能超过100个字符")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 活动描述
     */
    @Size(max = 1000, message = "活动描述长度不能超过1000个字符")
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    /**
     * 活动状态：0-已取消，1-进行中，2-已结束
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 提前结束原因
     */
    @Size(max = 500, message = "结束原因长度不能超过500个字符")
    @Column(name = "close_reason", length = 500)
    private String closeReason;

    /**
     * 实际结束时间
     */
    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;
}
