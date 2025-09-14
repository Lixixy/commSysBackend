package com.commsys.service;

import com.commsys.common.PageResult;
import com.commsys.entity.Club;
import com.commsys.entity.ClubMember;
import com.commsys.entity.User;
import com.commsys.exception.BusinessException;
import com.commsys.repository.ClubMemberRepository;
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
 * 社团服务类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final UserRepository userRepository;

    /**
     * 创建社团
     * 
     * @param title 社团标题
     * @param description 社团简介
     * @param presidentId 社长ID
     * @param teacherId 指导老师ID
     * @param memberIds 社团成员ID列表
     * @param operatorUserId 操作者用户ID
     * @return 创建的社团
     */
    @Transactional
    public Club createClub(String title, String description, Long presidentId, Long teacherId, 
                          List<Long> memberIds, Long operatorUserId) {
        log.info("创建社团: {}, 社长: {}", title, presidentId);
        
        // 检查操作者权限
        User operator = userRepository.findActiveById(operatorUserId)
                .orElseThrow(() -> new BusinessException("操作者不存在"));
        if (operator.getRoleId() < 4) {
            throw new BusinessException("权限不足，只有管理员以上才能创建社团");
        }
        
        // 检查社长是否存在
        User president = userRepository.findActiveById(presidentId)
                .orElseThrow(() -> new BusinessException("社长不存在"));
        
        // 检查指导老师是否存在
        if (teacherId != null) {
            User teacher = userRepository.findActiveById(teacherId)
                    .orElseThrow(() -> new BusinessException("指导老师不存在"));
            if (teacher.getRoleId() < 3) {
                throw new BusinessException("指导老师身份不正确");
            }
        }
        
        // 检查社团标题是否已存在
        if (clubRepository.existsByTitle(title)) {
            throw new BusinessException("社团标题已存在");
        }
        
        // 创建社团
        Club club = new Club();
        club.setTitle(title);
        club.setDescription(description);
        club.setPresidentId(presidentId);
        club.setTeacherId(teacherId);
        club.setStatus(1);
        
        club = clubRepository.save(club);
        
        // 添加社长为成员
        ClubMember presidentMember = new ClubMember();
        presidentMember.setClubId(club.getId());
        presidentMember.setUserId(presidentId);
        presidentMember.setJoinTime(LocalDateTime.now());
        presidentMember.setStatus(1);
        clubMemberRepository.save(presidentMember);
        
        // 更新社长的父社团ID
        president.setParentClubId(club.getId());
        president.setRoleId(2); // 设置为社长
        userRepository.save(president);
        
        // 添加其他成员
        if (memberIds != null && !memberIds.isEmpty()) {
            for (Long memberId : memberIds) {
                if (!memberId.equals(presidentId)) {
                    User member = userRepository.findActiveById(memberId)
                            .orElseThrow(() -> new BusinessException("成员不存在: " + memberId));
                    
                    ClubMember clubMember = new ClubMember();
                    clubMember.setClubId(club.getId());
                    clubMember.setUserId(memberId);
                    clubMember.setJoinTime(LocalDateTime.now());
                    clubMember.setStatus(1);
                    clubMemberRepository.save(clubMember);
                    
                    // 更新成员的父社团ID和身份
                    member.setParentClubId(club.getId());
                    member.setRoleId(1); // 设置为社团成员
                    userRepository.save(member);
                }
            }
        }
        
        return club;
    }

    /**
     * 禁用/启用社团
     * 
     * @param isEnabled 是否启用
     * @param operatorUserId 操作者用户ID
     * @param clubId 社团ID
     * @param disableReason 禁用原因（可选）
     */
    @Transactional
    public void closeOpenClub(Boolean isEnabled, Long operatorUserId, Long clubId, String disableReason) {
        log.info("禁用/启用社团: 社团ID={}, 启用={}, 操作者={}", clubId, isEnabled, operatorUserId);
        
        // 检查操作者权限
        User operator = userRepository.findActiveById(operatorUserId)
                .orElseThrow(() -> new BusinessException("操作者不存在"));
        if (operator.getRoleId() < 4) {
            throw new BusinessException("权限不足，只有管理员以上才能禁用/启用社团");
        }
        
        // 检查社团是否存在
        Club club = clubRepository.findActiveById(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));
        
        club.setStatus(isEnabled ? 1 : 0);
        if (!isEnabled && StringUtils.hasText(disableReason)) {
            club.setDisableReason(disableReason);
        }
        
        clubRepository.save(club);
    }

    /**
     * 申请加入社团
     * 
     * @param userId 用户ID
     * @param clubId 社团ID
     */
    @Transactional
    public void joinClub(Long userId, Long clubId) {
        log.info("申请加入社团: 用户ID={}, 社团ID={}", userId, clubId);
        
        // 检查用户是否存在且身份为0
        User user = userRepository.findActiveById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (user.getRoleId() != 0) {
            throw new BusinessException("只有无社团学生才能申请加入社团");
        }
        
        // 检查社团是否存在且启用
        Club club = clubRepository.findActiveById(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));
        if (club.getStatus() != 1) {
            throw new BusinessException("社团已被禁用");
        }
        
        // 检查是否已经是成员
        if (clubMemberRepository.existsByClubIdAndUserId(clubId, userId)) {
            throw new BusinessException("已经是该社团的成员");
        }
        
        // 添加成员关系
        ClubMember clubMember = new ClubMember();
        clubMember.setClubId(clubId);
        clubMember.setUserId(userId);
        clubMember.setJoinTime(LocalDateTime.now());
        clubMember.setStatus(1);
        clubMemberRepository.save(clubMember);
        
        // 更新用户的父社团ID和身份
        user.setParentClubId(clubId);
        user.setRoleId(1); // 设置为社团成员
        userRepository.save(user);
    }

    /**
     * 退出社团
     * 
     * @param userId 用户ID
     */
    @Transactional
    public void exitClub(Long userId) {
        log.info("退出社团: 用户ID={}", userId);
        
        // 检查用户是否存在且身份为1
        User user = userRepository.findActiveById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        if (user.getRoleId() != 1) {
            throw new BusinessException("只有社团成员才能退出社团");
        }
        
        Long clubId = user.getParentClubId();
        if (clubId == null || clubId == -1) {
            throw new BusinessException("用户不在任何社团中");
        }
        
        // 检查社团是否存在
        Club club = clubRepository.findActiveById(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));
        
        // 如果是社长，不能退出
        if (club.getPresidentId().equals(userId)) {
            throw new BusinessException("社长不能退出社团");
        }
        
        // 更新成员关系状态
        ClubMember clubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId)
                .orElseThrow(() -> new BusinessException("成员关系不存在"));
        clubMember.setStatus(0);
        clubMemberRepository.save(clubMember);
        
        // 更新用户的父社团ID和身份
        user.setParentClubId(-1L);
        user.setRoleId(0); // 设置为无社团学生
        userRepository.save(user);
    }

    /**
     * 根据ID获取社团
     * 
     * @param id 社团ID
     * @return 社团信息
     */
    public Club getClubById(Long id) {
        log.info("根据ID获取社团: {}", id);
        return clubRepository.findActiveById(id)
                .orElseThrow(() -> new BusinessException("社团不存在"));
    }

    /**
     * 获取所有社团
     * 
     * @return 社团列表
     */
    public List<Club> getAllClubs() {
        log.info("获取所有社团");
        return clubRepository.findAllActive();
    }

    /**
     * 根据状态获取社团列表
     * 
     * @param status 状态
     * @return 社团列表
     */
    public List<Club> getClubsByStatus(Integer status) {
        log.info("根据状态获取社团列表: {}", status);
        return clubRepository.findByStatus(status);
    }

    /**
     * 根据社长ID获取社团列表
     * 
     * @param presidentId 社长ID
     * @return 社团列表
     */
    public List<Club> getClubsByPresidentId(Long presidentId) {
        log.info("根据社长ID获取社团列表: {}", presidentId);
        return clubRepository.findByPresidentId(presidentId);
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
    public PageResult<Club> getClubs(Integer page, Integer size, String title, Integer status) {
        log.info("分页查询社团: page={}, size={}, title={}, status={}", page, size, title, status);
        
        // 创建分页参数
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<Club> clubPage;
        
        if (StringUtils.hasText(title)) {
            clubPage = clubRepository.findByTitleContaining(title, pageable);
        } else if (status != null) {
            List<Club> clubs = clubRepository.findByStatus(status);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), clubs.size());
            List<Club> pageContent = clubs.subList(start, end);
            clubPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, clubs.size());
        } else {
            clubPage = clubRepository.findAllActive(pageable);
        }
        
        return PageResult.of(clubPage);
    }

    /**
     * 获取社团成员列表
     * 
     * @param clubId 社团ID
     * @return 成员列表
     */
    public List<ClubMember> getClubMembers(Long clubId) {
        log.info("获取社团成员列表: {}", clubId);
        return clubMemberRepository.findByClubId(clubId);
    }

    /**
     * 获取用户所在的社团列表
     * 
     * @param userId 用户ID
     * @return 社团列表
     */
    public List<ClubMember> getUserClubs(Long userId) {
        log.info("获取用户所在的社团列表: {}", userId);
        return clubMemberRepository.findByUserId(userId);
    }

    /**
     * 检查用户是否在社团中
     * 
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 是否在社团中
     */
    public boolean isUserInClub(Long clubId, Long userId) {
        return clubMemberRepository.existsByClubIdAndUserId(clubId, userId);
    }

    /**
     * 统计社团成员数量
     * 
     * @param clubId 社团ID
     * @return 成员数量
     */
    public long countClubMembers(Long clubId) {
        return clubMemberRepository.countByClubId(clubId);
    }
}
