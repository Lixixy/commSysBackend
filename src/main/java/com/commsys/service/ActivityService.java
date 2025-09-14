package com.commsys.service;

import com.commsys.common.PageResult;
import com.commsys.entity.Activity;
import com.commsys.entity.User;
import com.commsys.exception.BusinessException;
import com.commsys.repository.ActivityRepository;
import com.commsys.repository.ClubRepository;
import com.commsys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动服务类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    /**
     * 创建活动
     * 
     * @param clubId 社团ID
     * @param creatorId 发起者ID
     * @param title 活动标题
     * @param description 活动描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 创建的活动
     */
    @Transactional
    public Activity createActivity(Long clubId, Long creatorId, String title, String description,
                                  LocalDateTime startTime, LocalDateTime endTime) {
        log.info("创建活动: {}, 社团ID: {}, 发起者: {}", title, clubId, creatorId);
        
        // 检查发起者权限
        User creator = userRepository.findActiveById(creatorId)
                .orElseThrow(() -> new BusinessException("发起者不存在"));
        if (creator.getRoleId() < 2) {
            throw new BusinessException("权限不足，只有社长以上才能创建活动");
        }
        
        // 检查社团是否存在
        clubRepository.findActiveById(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));
        
        // 检查权限：如果是社长或老师，需要父社团ID匹配
        if (creator.getRoleId() == 2 || creator.getRoleId() == 3) {
            if (!clubId.equals(creator.getParentClubId())) {
                throw new BusinessException("权限不足，只能为自己社团创建活动");
            }
        }
        
        // 检查时间
        if (startTime.isAfter(endTime)) {
            throw new BusinessException("开始时间不能晚于结束时间");
        }
        
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException("开始时间不能早于当前时间");
        }
        
        // 创建活动
        Activity activity = new Activity();
        activity.setClubId(clubId);
        activity.setCreatorId(creatorId);
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setStartTime(startTime);
        activity.setEndTime(endTime);
        activity.setStatus(1); // 进行中
        
        return activityRepository.save(activity);
    }

    /**
     * 删除活动
     * 
     * @param clubId 社团ID
     * @param activityId 活动ID
     * @param operatorId 操作者ID
     */
    @Transactional
    public void deleteActivity(Long clubId, Long activityId, Long operatorId) {
        log.info("删除活动: 活动ID={}, 社团ID={}, 操作者={}", activityId, clubId, operatorId);
        
        // 检查操作者权限
        User operator = userRepository.findActiveById(operatorId)
                .orElseThrow(() -> new BusinessException("操作者不存在"));
        if (operator.getRoleId() < 2) {
            throw new BusinessException("权限不足，只有社长以上才能删除活动");
        }
        
        // 检查活动是否存在
        Activity activity = activityRepository.findActiveById(activityId)
                .orElseThrow(() -> new BusinessException("活动不存在"));
        
        // 检查权限：如果是社长或老师，需要父社团ID匹配
        if (operator.getRoleId() == 2 || operator.getRoleId() == 3) {
            if (!clubId.equals(operator.getParentClubId())) {
                throw new BusinessException("权限不足，只能删除自己社团的活动");
            }
        }
        
        // 检查活动是否属于指定社团
        if (!activity.getClubId().equals(clubId)) {
            throw new BusinessException("活动不属于指定社团");
        }
        
        activityRepository.softDeleteById(activityId);
    }

    /**
     * 编辑活动
     * 
     * @param activityId 活动ID
     * @param operatorId 操作者ID
     * @param clubId 社团ID
     * @param title 活动标题
     * @param description 活动描述
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 更新后的活动
     */
    @Transactional
    public Activity editActivity(Long activityId, Long operatorId, Long clubId, String title,
                               String description, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("编辑活动: 活动ID={}, 操作者={}, 社团ID={}", activityId, operatorId, clubId);
        
        // 检查操作者权限
        User operator = userRepository.findActiveById(operatorId)
                .orElseThrow(() -> new BusinessException("操作者不存在"));
        if (operator.getRoleId() < 2) {
            throw new BusinessException("权限不足，只有社长以上才能编辑活动");
        }
        
        // 检查活动是否存在
        Activity activity = activityRepository.findActiveById(activityId)
                .orElseThrow(() -> new BusinessException("活动不存在"));
        
        // 检查权限：如果是社长或老师，需要父社团ID匹配
        if (operator.getRoleId() == 2 || operator.getRoleId() == 3) {
            if (!clubId.equals(operator.getParentClubId())) {
                throw new BusinessException("权限不足，只能编辑自己社团的活动");
            }
        }
        
        // 检查活动是否属于指定社团
        if (!activity.getClubId().equals(clubId)) {
            throw new BusinessException("活动不属于指定社团");
        }
        
        // 更新活动信息
        if (StringUtils.hasText(title)) {
            activity.setTitle(title);
        }
        if (StringUtils.hasText(description)) {
            activity.setDescription(description);
        }
        if (startTime != null) {
            activity.setStartTime(startTime);
        }
        if (endTime != null) {
            activity.setEndTime(endTime);
        }
        
        return activityRepository.save(activity);
    }

    /**
     * 提前结束活动
     * 
     * @param clubId 社团ID
     * @param activityId 活动ID
     * @param operatorId 操作者ID
     * @param closeReason 结束原因（可选）
     */
    @Transactional
    public void closeActivity(Long clubId, Long activityId, Long operatorId, String closeReason) {
        log.info("提前结束活动: 活动ID={}, 社团ID={}, 操作者={}", activityId, clubId, operatorId);
        
        // 检查操作者权限
        User operator = userRepository.findActiveById(operatorId)
                .orElseThrow(() -> new BusinessException("操作者不存在"));
        if (operator.getRoleId() < 2) {
            throw new BusinessException("权限不足，只有社长以上才能结束活动");
        }
        
        // 检查活动是否存在
        Activity activity = activityRepository.findActiveById(activityId)
                .orElseThrow(() -> new BusinessException("活动不存在"));
        
        // 检查权限：如果是社长或老师，需要父社团ID匹配
        if (operator.getRoleId() == 2 || operator.getRoleId() == 3) {
            if (!clubId.equals(operator.getParentClubId())) {
                throw new BusinessException("权限不足，只能结束自己社团的活动");
            }
        }
        
        // 检查活动是否属于指定社团
        if (!activity.getClubId().equals(clubId)) {
            throw new BusinessException("活动不属于指定社团");
        }
        
        // 检查活动状态
        if (activity.getStatus() == 2) {
            throw new BusinessException("活动已结束，不能提前结束");
        }
        
        // 结束活动
        activity.setStatus(2); // 已结束
        activity.setActualEndTime(LocalDateTime.now());
        if (StringUtils.hasText(closeReason)) {
            activity.setCloseReason(closeReason);
        }
        
        activityRepository.save(activity);
    }

    /**
     * 根据ID获取活动
     * 
     * @param id 活动ID
     * @return 活动信息
     */
    public Activity getActivityById(Long id) {
        log.info("根据ID获取活动: {}", id);
        return activityRepository.findActiveById(id)
                .orElseThrow(() -> new BusinessException("活动不存在"));
    }

    /**
     * 获取所有活动
     * 
     * @return 活动列表
     */
    public List<Activity> getAllActivities() {
        log.info("获取所有活动");
        return activityRepository.findAllActive();
    }

    /**
     * 根据社团ID获取活动列表
     * 
     * @param clubId 社团ID
     * @return 活动列表
     */
    public List<Activity> getActivitiesByClubId(Long clubId) {
        log.info("根据社团ID获取活动列表: {}", clubId);
        return activityRepository.findByClubId(clubId);
    }

    /**
     * 根据发起者ID获取活动列表
     * 
     * @param creatorId 发起者ID
     * @return 活动列表
     */
    public List<Activity> getActivitiesByCreatorId(Long creatorId) {
        log.info("根据发起者ID获取活动列表: {}", creatorId);
        return activityRepository.findByCreatorId(creatorId);
    }

    /**
     * 根据状态获取活动列表
     * 
     * @param status 状态
     * @return 活动列表
     */
    public List<Activity> getActivitiesByStatus(Integer status) {
        log.info("根据状态获取活动列表: {}", status);
        return activityRepository.findByStatus(status);
    }

    /**
     * 根据社团ID和状态获取活动列表
     * 
     * @param clubId 社团ID
     * @param status 状态
     * @return 活动列表
     */
    public List<Activity> getActivitiesByClubIdAndStatus(Long clubId, Integer status) {
        log.info("根据社团ID和状态获取活动列表: 社团ID={}, 状态={}", clubId, status);
        return activityRepository.findByClubIdAndStatus(clubId, status);
    }

    /**
     * 根据时间范围获取活动列表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 活动列表
     */
    public List<Activity> getActivitiesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("根据时间范围获取活动列表: {} - {}", startTime, endTime);
        return activityRepository.findByTimeRange(startTime, endTime);
    }

    /**
     * 获取正在进行的活动
     * 
     * @return 活动列表
     */
    public List<Activity> getOngoingActivities() {
        log.info("获取正在进行的活动");
        return activityRepository.findOngoingActivities(LocalDateTime.now());
    }

    /**
     * 获取已结束的活动
     * 
     * @return 活动列表
     */
    public List<Activity> getEndedActivities() {
        log.info("获取已结束的活动");
        return activityRepository.findEndedActivities(LocalDateTime.now());
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
    public PageResult<Activity> getActivities(Integer page, Integer size, String title, 
                                            Long clubId, Integer status) {
        log.info("分页查询活动: page={}, size={}, title={}, clubId={}, status={}", 
                page, size, title, clubId, status);
        
        // 创建分页参数
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<Activity> activityPage;
        
        if (StringUtils.hasText(title)) {
            activityPage = activityRepository.findByTitleContaining(title, pageable);
        } else if (clubId != null && status != null) {
            List<Activity> activities = activityRepository.findByClubIdAndStatus(clubId, status);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), activities.size());
            List<Activity> pageContent = activities.subList(start, end);
            activityPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, activities.size());
        } else if (clubId != null) {
            List<Activity> activities = activityRepository.findByClubId(clubId);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), activities.size());
            List<Activity> pageContent = activities.subList(start, end);
            activityPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, activities.size());
        } else if (status != null) {
            List<Activity> activities = activityRepository.findByStatus(status);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), activities.size());
            List<Activity> pageContent = activities.subList(start, end);
            activityPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, activities.size());
        } else {
            activityPage = activityRepository.findAllActive(pageable);
        }
        
        return PageResult.of(activityPage);
    }
}
