package com.commsys.repository;

import com.commsys.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动数据访问接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Repository
public interface ActivityRepository extends BaseRepository<Activity> {

    /**
     * 根据社团ID查找活动列表
     * 
     * @param clubId 社团ID
     * @return 活动列表
     */
    @Query("SELECT a FROM Activity a WHERE a.clubId = :clubId AND a.isDeleted = false")
    List<Activity> findByClubId(@Param("clubId") Long clubId);

    /**
     * 根据发起者ID查找活动列表
     * 
     * @param creatorId 发起者ID
     * @return 活动列表
     */
    @Query("SELECT a FROM Activity a WHERE a.creatorId = :creatorId AND a.isDeleted = false")
    List<Activity> findByCreatorId(@Param("creatorId") Long creatorId);

    /**
     * 根据状态查找活动列表
     * 
     * @param status 状态
     * @return 活动列表
     */
    @Query("SELECT a FROM Activity a WHERE a.status = :status AND a.isDeleted = false")
    List<Activity> findByStatus(@Param("status") Integer status);

    /**
     * 根据社团ID和状态查找活动列表
     * 
     * @param clubId 社团ID
     * @param status 状态
     * @return 活动列表
     */
    @Query("SELECT a FROM Activity a WHERE a.clubId = :clubId AND a.status = :status AND a.isDeleted = false")
    List<Activity> findByClubIdAndStatus(@Param("clubId") Long clubId, @Param("status") Integer status);

    /**
     * 根据时间范围查找活动列表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 活动列表
     */
    @Query("SELECT a FROM Activity a WHERE a.startTime >= :startTime AND a.endTime <= :endTime AND a.isDeleted = false")
    List<Activity> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据活动标题模糊查询活动列表
     * 
     * @param title 活动标题关键字
     * @param pageable 分页参数
     * @return 分页活动列表
     */
    @Query("SELECT a FROM Activity a WHERE a.title LIKE %:title% AND a.isDeleted = false")
    Page<Activity> findByTitleContaining(@Param("title") String title, Pageable pageable);

    /**
     * 查找正在进行的活动
     * 
     * @param now 当前时间
     * @return 活动列表
     */
    @Query("SELECT a FROM Activity a WHERE a.status = 1 AND a.startTime <= :now AND a.endTime >= :now AND a.isDeleted = false")
    List<Activity> findOngoingActivities(@Param("now") LocalDateTime now);

    /**
     * 查找已结束的活动
     * 
     * @param now 当前时间
     * @return 活动列表
     */
    @Query("SELECT a FROM Activity a WHERE a.status = 1 AND a.endTime < :now AND a.isDeleted = false")
    List<Activity> findEndedActivities(@Param("now") LocalDateTime now);
}
