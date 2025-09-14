package com.commsys.controller;

import com.commsys.common.PageResult;
import com.commsys.common.Result;
import com.commsys.entity.Club;
import com.commsys.entity.ClubMember;
import com.commsys.service.ClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社团控制器
 * 提供社团相关的REST API接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    /**
     * 创建社团
     * 
     * @param request 创建社团请求
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result<Club> createClub(@Valid @RequestBody CreateClubRequest request) {
        log.info("创建社团请求: {}, 社长: {}", request.getTitle(), request.getPresidentId());
        Club club = clubService.createClub(request.getTitle(), request.getDescription(), 
                request.getPresidentId(), request.getTeacherId(), request.getMemberIds(), 
                request.getOperatorUserId());
        return Result.success("社团创建成功", club);
    }

    /**
     * 禁用/启用社团
     * 
     * @param request 禁用/启用请求
     * @return 操作结果
     */
    @PostMapping("/close_open")
    public Result<Void> closeOpenClub(@Valid @RequestBody CloseOpenClubRequest request) {
        log.info("禁用/启用社团请求: 社团ID={}, 启用={}, 操作者={}", 
                request.getClubId(), request.getIsEnabled(), request.getOperatorUserId());
        clubService.closeOpenClub(request.getIsEnabled(), request.getOperatorUserId(), 
                request.getClubId(), request.getDisableReason());
        return Result.success("社团状态修改成功");
    }

    /**
     * 申请加入社团
     * 
     * @param request 加入社团请求
     * @return 操作结果
     */
    @PostMapping("/join")
    public Result<Void> joinClub(@Valid @RequestBody JoinClubRequest request) {
        log.info("申请加入社团请求: 用户ID={}, 社团ID={}", request.getUserId(), request.getClubId());
        clubService.joinClub(request.getUserId(), request.getClubId());
        return Result.success("申请加入社团成功");
    }

    /**
     * 退出社团
     * 
     * @param request 退出社团请求
     * @return 操作结果
     */
    @PostMapping("/exit")
    public Result<Void> exitClub(@Valid @RequestBody ExitClubRequest request) {
        log.info("退出社团请求: 用户ID={}", request.getUserId());
        clubService.exitClub(request.getUserId());
        return Result.success("退出社团成功");
    }

    /**
     * 获取所有社团
     * 
     * @return 社团列表
     */
    @GetMapping("/all")
    public Result<List<Club>> getAllClubs() {
        log.info("获取所有社团请求");
        List<Club> clubs = clubService.getAllClubs();
        return Result.success(clubs);
    }

    /**
     * 根据ID获取社团
     * 
     * @param id 社团ID
     * @return 社团信息
     */
    @GetMapping("/{id}")
    public Result<Club> getClubById(@PathVariable Long id) {
        log.info("根据ID获取社团请求: {}", id);
        Club club = clubService.getClubById(id);
        return Result.success(club);
    }

    /**
     * 根据状态获取社团列表
     * 
     * @param status 状态
     * @return 社团列表
     */
    @GetMapping("/status/{status}")
    public Result<List<Club>> getClubsByStatus(@PathVariable Integer status) {
        log.info("根据状态获取社团请求: {}", status);
        List<Club> clubs = clubService.getClubsByStatus(status);
        return Result.success(clubs);
    }

    /**
     * 根据社长ID获取社团列表
     * 
     * @param presidentId 社长ID
     * @return 社团列表
     */
    @GetMapping("/president/{presidentId}")
    public Result<List<Club>> getClubsByPresidentId(@PathVariable Long presidentId) {
        log.info("根据社长ID获取社团请求: {}", presidentId);
        List<Club> clubs = clubService.getClubsByPresidentId(presidentId);
        return Result.success(clubs);
    }

    /**
     * 分页查询社团
     * 
     * @param page 页码
     * @param size 每页大小
     * @param title 社团标题关键字
     * @param status 状态
     * @return 分页社团列表
     */
    @GetMapping
    public Result<PageResult<Club>> getClubs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status) {
        log.info("分页查询社团请求: page={}, size={}, title={}, status={}", page, size, title, status);
        PageResult<Club> pageResult = clubService.getClubs(page, size, title, status);
        return Result.success(pageResult);
    }

    /**
     * 获取社团成员列表
     * 
     * @param clubId 社团ID
     * @return 成员列表
     */
    @GetMapping("/{clubId}/members")
    public Result<List<ClubMember>> getClubMembers(@PathVariable Long clubId) {
        log.info("获取社团成员列表请求: {}", clubId);
        List<ClubMember> members = clubService.getClubMembers(clubId);
        return Result.success(members);
    }

    /**
     * 获取用户所在的社团列表
     * 
     * @param userId 用户ID
     * @return 社团列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<ClubMember>> getUserClubs(@PathVariable Long userId) {
        log.info("获取用户所在的社团列表请求: {}", userId);
        List<ClubMember> clubs = clubService.getUserClubs(userId);
        return Result.success(clubs);
    }

    /**
     * 检查用户是否在社团中
     * 
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 检查结果
     */
    @GetMapping("/{clubId}/check/{userId}")
    public Result<Boolean> isUserInClub(@PathVariable Long clubId, @PathVariable Long userId) {
        log.info("检查用户是否在社团中请求: 社团ID={}, 用户ID={}", clubId, userId);
        boolean isInClub = clubService.isUserInClub(clubId, userId);
        return Result.success(isInClub);
    }

    /**
     * 统计社团成员数量
     * 
     * @param clubId 社团ID
     * @return 成员数量
     */
    @GetMapping("/{clubId}/count")
    public Result<Long> countClubMembers(@PathVariable Long clubId) {
        log.info("统计社团成员数量请求: {}", clubId);
        long count = clubService.countClubMembers(clubId);
        return Result.success(count);
    }

    // 内部类：请求对象
    public static class CreateClubRequest {
        private String title;
        private String description;
        private Long presidentId;
        private Long teacherId;
        private List<Long> memberIds;
        private Long operatorUserId;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Long getPresidentId() { return presidentId; }
        public void setPresidentId(Long presidentId) { this.presidentId = presidentId; }
        public Long getTeacherId() { return teacherId; }
        public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
        public List<Long> getMemberIds() { return memberIds; }
        public void setMemberIds(List<Long> memberIds) { this.memberIds = memberIds; }
        public Long getOperatorUserId() { return operatorUserId; }
        public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
    }

    public static class CloseOpenClubRequest {
        private Boolean isEnabled;
        private Long operatorUserId;
        private Long clubId;
        private String disableReason;

        public Boolean getIsEnabled() { return isEnabled; }
        public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
        public Long getOperatorUserId() { return operatorUserId; }
        public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
        public Long getClubId() { return clubId; }
        public void setClubId(Long clubId) { this.clubId = clubId; }
        public String getDisableReason() { return disableReason; }
        public void setDisableReason(String disableReason) { this.disableReason = disableReason; }
    }

    public static class JoinClubRequest {
        private Long userId;
        private Long clubId;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getClubId() { return clubId; }
        public void setClubId(Long clubId) { this.clubId = clubId; }
    }

    public static class ExitClubRequest {
        private Long userId;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
}
