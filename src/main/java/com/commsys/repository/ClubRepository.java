package com.commsys.repository;

import com.commsys.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社团数据访问接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Repository
public interface ClubRepository extends BaseRepository<Club> {

    /**
     * 根据社长ID查找社团
     * 
     * @param presidentId 社长ID
     * @return 社团列表
     */
    @Query("SELECT c FROM Club c WHERE c.presidentId = :presidentId AND c.isDeleted = false")
    List<Club> findByPresidentId(@Param("presidentId") Long presidentId);

    /**
     * 根据指导老师ID查找社团
     * 
     * @param teacherId 指导老师ID
     * @return 社团列表
     */
    @Query("SELECT c FROM Club c WHERE c.teacherId = :teacherId AND c.isDeleted = false")
    List<Club> findByTeacherId(@Param("teacherId") Long teacherId);

    /**
     * 根据状态查找社团列表
     * 
     * @param status 状态
     * @return 社团列表
     */
    @Query("SELECT c FROM Club c WHERE c.status = :status AND c.isDeleted = false")
    List<Club> findByStatus(@Param("status") Integer status);

    /**
     * 根据社团标题模糊查询社团列表
     * 
     * @param title 社团标题关键字
     * @param pageable 分页参数
     * @return 分页社团列表
     */
    @Query("SELECT c FROM Club c WHERE c.title LIKE %:title% AND c.isDeleted = false")
    Page<Club> findByTitleContaining(@Param("title") String title, Pageable pageable);

    /**
     * 检查社团标题是否存在
     * 
     * @param title 社团标题
     * @return 是否存在
     */
    @Query("SELECT COUNT(c) > 0 FROM Club c WHERE c.title = :title AND c.isDeleted = false")
    boolean existsByTitle(@Param("title") String title);
}
