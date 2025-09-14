package com.commsys.repository;

import com.commsys.entity.ClubMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 社团成员关系数据访问接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Repository
public interface ClubMemberRepository extends BaseRepository<ClubMember> {

    /**
     * 根据社团ID查找成员列表
     * 
     * @param clubId 社团ID
     * @return 成员列表
     */
    @Query("SELECT cm FROM ClubMember cm WHERE cm.clubId = :clubId AND cm.isDeleted = false")
    List<ClubMember> findByClubId(@Param("clubId") Long clubId);

    /**
     * 根据用户ID查找社团列表
     * 
     * @param userId 用户ID
     * @return 社团列表
     */
    @Query("SELECT cm FROM ClubMember cm WHERE cm.userId = :userId AND cm.isDeleted = false")
    List<ClubMember> findByUserId(@Param("userId") Long userId);

    /**
     * 根据社团ID和用户ID查找成员关系
     * 
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 成员关系
     */
    @Query("SELECT cm FROM ClubMember cm WHERE cm.clubId = :clubId AND cm.userId = :userId AND cm.isDeleted = false")
    Optional<ClubMember> findByClubIdAndUserId(@Param("clubId") Long clubId, @Param("userId") Long userId);

    /**
     * 根据状态查找成员列表
     * 
     * @param status 状态
     * @return 成员列表
     */
    @Query("SELECT cm FROM ClubMember cm WHERE cm.status = :status AND cm.isDeleted = false")
    List<ClubMember> findByStatus(@Param("status") Integer status);

    /**
     * 检查用户是否在社团中
     * 
     * @param clubId 社团ID
     * @param userId 用户ID
     * @return 是否在社团中
     */
    @Query("SELECT COUNT(cm) > 0 FROM ClubMember cm WHERE cm.clubId = :clubId AND cm.userId = :userId AND cm.status = 1 AND cm.isDeleted = false")
    boolean existsByClubIdAndUserId(@Param("clubId") Long clubId, @Param("userId") Long userId);

    /**
     * 统计社团成员数量
     * 
     * @param clubId 社团ID
     * @return 成员数量
     */
    @Query("SELECT COUNT(cm) FROM ClubMember cm WHERE cm.clubId = :clubId AND cm.status = 1 AND cm.isDeleted = false")
    long countByClubId(@Param("clubId") Long clubId);
}
