package com.commsys.repository;

import com.commsys.entity.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Token数据访问接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Repository
public interface TokenRepository extends BaseRepository<Token> {

    /**
     * 根据Token值查找Token
     * 
     * @param tokenValue Token值
     * @return Token对象
     */
    @Query("SELECT t FROM Token t WHERE t.tokenValue = :tokenValue AND t.isDeleted = false")
    Optional<Token> findByTokenValue(@Param("tokenValue") String tokenValue);

    /**
     * 根据用户ID查找有效Token列表
     * 
     * @param userId 用户ID
     * @return Token列表
     */
    @Query("SELECT t FROM Token t WHERE t.userId = :userId AND t.status = 1 AND t.expiresAt > :now AND t.isDeleted = false")
    List<Token> findValidByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    /**
     * 根据用户ID查找所有Token列表
     * 
     * @param userId 用户ID
     * @return Token列表
     */
    @Query("SELECT t FROM Token t WHERE t.userId = :userId AND t.isDeleted = false")
    List<Token> findByUserId(@Param("userId") Long userId);

    /**
     * 查找过期的Token列表
     * 
     * @param now 当前时间
     * @return Token列表
     */
    @Query("SELECT t FROM Token t WHERE t.expiresAt <= :now AND t.isDeleted = false")
    List<Token> findExpiredTokens(@Param("now") LocalDateTime now);

    /**
     * 检查Token是否存在且有效
     * 
     * @param tokenValue Token值
     * @param now 当前时间
     * @return 是否存在且有效
     */
    @Query("SELECT COUNT(t) > 0 FROM Token t WHERE t.tokenValue = :tokenValue AND t.status = 1 AND t.expiresAt > :now AND t.isDeleted = false")
    boolean existsValidToken(@Param("tokenValue") String tokenValue, @Param("now") LocalDateTime now);

    /**
     * 使Token过期
     * 
     * @param tokenValue Token值
     */
    @Query("UPDATE Token t SET t.status = 0 WHERE t.tokenValue = :tokenValue")
    void expireToken(@Param("tokenValue") String tokenValue);

    /**
     * 使用户的所有Token过期
     * 
     * @param userId 用户ID
     */
    @Query("UPDATE Token t SET t.status = 0 WHERE t.userId = :userId")
    void expireAllUserTokens(@Param("userId") Long userId);
}
