package com.commsys.repository;

import com.commsys.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 基础仓储接口
 * 提供通用的数据访问方法
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    /**
     * 根据ID查找实体（包含已删除的）
     * 
     * @param id 实体ID
     * @return 实体对象
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id")
    Optional<T> findByIdIncludeDeleted(@Param("id") Long id);

    /**
     * 查找所有实体（包含已删除的）
     * 
     * @return 实体列表
     */
    @Query("SELECT e FROM #{#entityName} e")
    List<T> findAllIncludeDeleted();

    /**
     * 分页查找所有实体（包含已删除的）
     * 
     * @param pageable 分页参数
     * @return 分页实体列表
     */
    @Query("SELECT e FROM #{#entityName} e")
    Page<T> findAllIncludeDeleted(Pageable pageable);

    /**
     * 查找未删除的实体
     * 
     * @return 实体列表
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.isDeleted = false")
    List<T> findAllActive();

    /**
     * 分页查找未删除的实体
     * 
     * @param pageable 分页参数
     * @return 分页实体列表
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.isDeleted = false")
    Page<T> findAllActive(Pageable pageable);

    /**
     * 根据ID查找未删除的实体
     * 
     * @param id 实体ID
     * @return 实体对象
     */
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.isDeleted = false")
    Optional<T> findActiveById(@Param("id") Long id);

    /**
     * 逻辑删除实体
     * 
     * @param id 实体ID
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = true WHERE e.id = :id")
    void softDeleteById(@Param("id") Long id);

    /**
     * 批量逻辑删除实体
     * 
     * @param ids 实体ID列表
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = true WHERE e.id IN :ids")
    void softDeleteByIds(@Param("ids") List<Long> ids);

    /**
     * 恢复已删除的实体
     * 
     * @param id 实体ID
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = false WHERE e.id = :id")
    void restoreById(@Param("id") Long id);

    /**
     * 统计未删除的实体数量
     * 
     * @return 实体数量
     */
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.isDeleted = false")
    long countActive();
}