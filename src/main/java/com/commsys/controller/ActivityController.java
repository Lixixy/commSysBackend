package com.commsys.controller;

import com.commsys.annotation.AuthRequired;
import com.commsys.common.PageResult;
import com.commsys.common.Result;
import com.commsys.entity.Activity;
import com.commsys.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动控制器
 * 提供活动相关的REST API接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    /**
     * 创建活动
     * 
     * @param request 创建活动请求
     * @return 创建结果
     */
    @AuthRequired
    @PostMapping("/create")
    public Result<Activity> createActivity(@Valid @RequestBody CreateActivityRequest request) {
        log.info("创建活动请求: {}, 社团ID: {}, 发起者: {}", 
                request.getTitle(), request.getClubId(), request.getCreatorId());
        Activity activity = activityService.createActivity(request.getClubId(), request.getCreatorId(),
                request.getTitle(), request.getDescription(), request.getStartTime(), request.getEndTime());
        return Result.success("活动创建成功", activity);
    }

    /**
     * 删除活动
     * 
     * @param request 删除活动请求
     * @return 删除结果
     */
    @AuthRequired
    @PostMapping("/del")
    public Result<Void> deleteActivity(@Valid @RequestBody DeleteActivityRequest request) {
        log.info("删除活动请求: 活动ID={}, 社团ID={}, 操作者={}", 
                request.getActivityId(), request.getClubId(), request.getOperatorId());
        activityService.deleteActivity(request.getClubId(), request.getActivityId(), request.getOperatorId());
        return Result.success("活动删除成功");
    }

    /**
     * 编辑活动
     * 
     * @param request 编辑活动请求
     * @return 编辑结果
     */
    @AuthRequired
    @PostMapping("/change")
    public Result<Activity> editActivity(@Valid @RequestBody EditActivityRequest request) {
        log.info("编辑活动请求: 活动ID={}, 操作者={}, 社团ID={}", 
                request.getActivityId(), request.getOperatorId(), request.getClubId());
        Activity activity = activityService.editActivity(request.getActivityId(), request.getOperatorId(),
                request.getClubId(), request.getTitle(), request.getDescription(),
                request.getStartTime(), request.getEndTime());
        return Result.success("活动编辑成功", activity);
    }

    /**
     * 提前结束活动
     * 
     * @param request 结束活动请求
     * @return 结束结果
     */
    @AuthRequired
    @PostMapping("/close")
    public Result<Void> closeActivity(@Valid @RequestBody CloseActivityRequest request) {
        log.info("提前结束活动请求: 活动ID={}, 社团ID={}, 操作者={}", 
                request.getActivityId(), request.getClubId(), request.getOperatorId());
        activityService.closeActivity(request.getClubId(), request.getActivityId(),
                request.getOperatorId(), request.getCloseReason());
        return Result.success("活动结束成功");
    }

    /**
     * 获取所有活动
     * 
     * @return 活动列表
     */
    @AuthRequired
    @GetMapping("/all")
    public Result<List<Activity>> getAllActivities() {
        log.info("获取所有活动请求");
        List<Activity> activities = activityService.getAllActivities();
        return Result.success(activities);
    }

    /**
     * 根据ID获取活动
     * 
     * @param id 活动ID
     * @return 活动信息
     */
    @AuthRequired
    @GetMapping("/{id}")
    public Result<Activity> getActivityById(@PathVariable Long id) {
        log.info("根据ID获取活动请求: {}", id);
        Activity activity = activityService.getActivityById(id);
        return Result.success(activity);
    }

    /**
     * 根据社团ID获取活动列表
     * 
     * @param clubId 社团ID
     * @return 活动列表
     */
    @AuthRequired
    @GetMapping("/club/{clubId}")
    public Result<List<Activity>> getActivitiesByClubId(@PathVariable Long clubId) {
        log.info("根据社团ID获取活动请求: {}", clubId);
        List<Activity> activities = activityService.getActivitiesByClubId(clubId);
        return Result.success(activities);
    }

    /**
     * 根据发起者ID获取活动列表
     * 
     * @param creatorId 发起者ID
     * @return 活动列表
     */
    @AuthRequired
    @GetMapping("/creator/{creatorId}")
    public Result<List<Activity>> getActivitiesByCreatorId(@PathVariable Long creatorId) {
        log.info("根据发起者ID获取活动请求: {}", creatorId);
        List<Activity> activities = activityService.getActivitiesByCreatorId(creatorId);
        return Result.success(activities);
    }

    /**
     * 根据状态获取活动列表
     * 
     * @param status 状态
     * @return 活动列表
     */
    @AuthRequired
    @GetMapping("/status/{status}")
    public Result<List<Activity>> getActivitiesByStatus(@PathVariable Integer status) {
        log.info("根据状态获取活动请求: {}", status);
        List<Activity> activities = activityService.getActivitiesByStatus(status);
        return Result.success(activities);
    }

    /**
     * 根据社团ID和状态获取活动列表
     * 
     * @param clubId 社团ID
     * @param status 状态
     * @return 活动列表
     */
    @AuthRequired
    @GetMapping("/club/{clubId}/status/{status}")
    public Result<List<Activity>> getActivitiesByClubIdAndStatus(@PathVariable Long clubId, 
                                                                @PathVariable Integer status) {
        log.info("根据社团ID和状态获取活动请求: 社团ID={}, 状态={}", clubId, status);
        List<Activity> activities = activityService.getActivitiesByClubIdAndStatus(clubId, status);
        return Result.success(activities);
    }

    /**
     * 根据时间范围获取活动列表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 活动列表
     */
    @AuthRequired
    @GetMapping("/time")
    public Result<List<Activity>> getActivitiesByTimeRange(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        log.info("根据时间范围获取活动请求: {} - {}", startTime, endTime);
        List<Activity> activities = activityService.getActivitiesByTimeRange(startTime, endTime);
        return Result.success(activities);
    }

    /**
     * 获取正在进行的活动
     * 
     * @return 活动列表
     */
    @AuthRequired
    @GetMapping("/ongoing")
    public Result<List<Activity>> getOngoingActivities() {
        log.info("获取正在进行的活动请求");
        List<Activity> activities = activityService.getOngoingActivities();
        return Result.success(activities);
    }

    /**
     * 获取已结束的活动
     * 
     * @return 活动列表
     */
    @AuthRequired
    @GetMapping("/ended")
    public Result<List<Activity>> getEndedActivities() {
        log.info("获取已结束的活动请求");
        List<Activity> activities = activityService.getEndedActivities();
        return Result.success(activities);
    }

    /**
     * 分页查询活动
     * 
     * @param page 页码
     * @param size 每页大小
     * @param title 活动标题关键字
     * @param clubId 社团ID
     * @param status 状态
     * @return 分页活动列表
     */
    @AuthRequired
    @GetMapping
    public Result<PageResult<Activity>> getActivities(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) Integer status) {
        log.info("分页查询活动请求: page={}, size={}, title={}, clubId={}, status={}", 
                page, size, title, clubId, status);
        PageResult<Activity> pageResult = activityService.getActivities(page, size, title, clubId, status);
        return Result.success(pageResult);
    }

    // 内部类：请求对象
    public static class CreateActivityRequest {
        private Long clubId;
        private Long creatorId;
        private String title;
        private String description;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public Long getClubId() { return clubId; }
        public void setClubId(Long clubId) { this.clubId = clubId; }
        public Long getCreatorId() { return creatorId; }
        public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    }

    public static class DeleteActivityRequest {
        private Long clubId;
        private Long activityId;
        private Long operatorId;

        public Long getClubId() { return clubId; }
        public void setClubId(Long clubId) { this.clubId = clubId; }
        public Long getActivityId() { return activityId; }
        public void setActivityId(Long activityId) { this.activityId = activityId; }
        public Long getOperatorId() { return operatorId; }
        public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
    }

    public static class EditActivityRequest {
        private Long activityId;
        private Long operatorId;
        private Long clubId;
        private String title;
        private String description;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public Long getActivityId() { return activityId; }
        public void setActivityId(Long activityId) { this.activityId = activityId; }
        public Long getOperatorId() { return operatorId; }
        public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
        public Long getClubId() { return clubId; }
        public void setClubId(Long clubId) { this.clubId = clubId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDateTime getStartTime() { return startTime; }
        public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    }

    public static class CloseActivityRequest {
        private Long clubId;
        private Long activityId;
        private Long operatorId;
        private String closeReason;

        public Long getClubId() { return clubId; }
        public void setClubId(Long clubId) { this.clubId = clubId; }
        public Long getActivityId() { return activityId; }
        public void setActivityId(Long activityId) { this.activityId = activityId; }
        public Long getOperatorId() { return operatorId; }
        public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
        public String getCloseReason() { return closeReason; }
        public void setCloseReason(String closeReason) { this.closeReason = closeReason; }
    }
}
