package com.commsys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社团成员关系实体类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Data
@Entity
@Table(name = "club_members", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"club_id", "user_id"})
})
@EqualsAndHashCode(callSuper = true)
public class ClubMember extends BaseEntity {


    /**
     * 社团ID
     */
    @NotNull(message = "社团ID不能为空")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 加入时间
     */
    @Column(name = "join_time", nullable = false)
    private java.time.LocalDateTime joinTime;

    /**
     * 成员状态：0-已退出，1-正常
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
