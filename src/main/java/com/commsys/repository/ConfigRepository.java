package com.commsys.repository;

import com.commsys.entity.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 配置数据访问接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Repository
public interface ConfigRepository extends BaseRepository<Config> {

    /**
     * 根据配置键查找配置
     * 
     * @param configKey 配置键
     * @return 配置对象
     */
    @Query("SELECT c FROM Config c WHERE c.configKey = :configKey AND c.isDeleted = false")
    Optional<Config> findByConfigKey(@Param("configKey") String configKey);

    /**
     * 根据配置分组查找配置列表
     * 
     * @param configGroup 配置分组
     * @return 配置列表
     */
    @Query("SELECT c FROM Config c WHERE c.configGroup = :configGroup AND c.isDeleted = false ORDER BY c.configKey")
    List<Config> findByConfigGroup(@Param("configGroup") String configGroup);

    /**
     * 根据配置类型查找配置列表
     * 
     * @param configType 配置类型
     * @return 配置列表
     */
    @Query("SELECT c FROM Config c WHERE c.configType = :configType AND c.isDeleted = false ORDER BY c.configKey")
    List<Config> findByConfigType(@Param("configType") Config.ConfigType configType);

    /**
     * 根据是否可修改查找配置列表
     * 
     * @param isModifiable 是否可修改
     * @return 配置列表
     */
    @Query("SELECT c FROM Config c WHERE c.isModifiable = :isModifiable AND c.isDeleted = false ORDER BY c.configKey")
    List<Config> findByIsModifiable(@Param("isModifiable") Boolean isModifiable);

    /**
     * 根据配置键模糊查询配置列表
     * 
     * @param configKey 配置键关键字
     * @param pageable 分页参数
     * @return 分页配置列表
     */
    @Query("SELECT c FROM Config c WHERE c.configKey LIKE %:configKey% AND c.isDeleted = false")
    Page<Config> findByConfigKeyContaining(@Param("configKey") String configKey, Pageable pageable);

    /**
     * 根据配置分组和配置键模糊查询配置列表
     * 
     * @param configGroup 配置分组
     * @param configKey 配置键关键字
     * @param pageable 分页参数
     * @return 分页配置列表
     */
    @Query("SELECT c FROM Config c WHERE c.configGroup = :configGroup AND c.configKey LIKE %:configKey% AND c.isDeleted = false")
    Page<Config> findByConfigGroupAndConfigKeyContaining(@Param("configGroup") String configGroup, 
                                                         @Param("configKey") String configKey, 
                                                         Pageable pageable);

    /**
     * 检查配置键是否存在
     * 
     * @param configKey 配置键
     * @return 是否存在
     */
    @Query("SELECT COUNT(c) > 0 FROM Config c WHERE c.configKey = :configKey AND c.isDeleted = false")
    boolean existsByConfigKey(@Param("configKey") String configKey);

    /**
     * 获取所有配置分组
     * 
     * @return 配置分组列表
     */
    @Query("SELECT DISTINCT c.configGroup FROM Config c WHERE c.isDeleted = false ORDER BY c.configGroup")
    List<String> findAllConfigGroups();

    /**
     * 根据配置键获取配置值
     * 
     * @param configKey 配置键
     * @return 配置值
     */
    @Query("SELECT c.configValue FROM Config c WHERE c.configKey = :configKey AND c.isDeleted = false")
    Optional<String> findConfigValueByKey(@Param("configKey") String configKey);
}