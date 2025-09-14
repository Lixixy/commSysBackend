package com.commsys.repository;

import com.commsys.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Repository
public interface UserRepository extends BaseRepository<User> {

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.isDeleted = false")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 用户对象
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * 根据手机号查找用户
     * 
     * @param phone 手机号
     * @return 用户对象
     */
    @Query("SELECT u FROM User u WHERE u.phone = :phone AND u.isDeleted = false")
    Optional<User> findByPhone(@Param("phone") String phone);

    /**
     * 根据身份ID查找用户列表
     * 
     * @param roleId 身份ID
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE u.roleId = :roleId AND u.isDeleted = false")
    List<User> findByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据父社团ID查找用户列表
     * 
     * @param parentClubId 父社团ID
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE u.parentClubId = :parentClubId AND u.isDeleted = false")
    List<User> findByParentClubId(@Param("parentClubId") Long parentClubId);

    /**
     * 根据状态查找用户列表
     * 
     * @param status 状态
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE u.status = :status AND u.isDeleted = false")
    List<User> findByStatus(@Param("status") Integer status);

    /**
     * 根据身份ID和状态查找用户列表
     * 
     * @param roleId 身份ID
     * @param status 状态
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE u.roleId = :roleId AND u.status = :status AND u.isDeleted = false")
    List<User> findByRoleIdAndStatus(@Param("roleId") Integer roleId, @Param("status") Integer status);

    /**
     * 根据用户名模糊查询用户列表
     * 
     * @param username 用户名关键字
     * @param pageable 分页参数
     * @return 分页用户列表
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %:username% AND u.isDeleted = false")
    Page<User> findByUsernameContaining(@Param("username") String username, Pageable pageable);

    /**
     * 根据真实姓名模糊查询用户列表
     * 
     * @param realName 真实姓名关键字
     * @param pageable 分页参数
     * @return 分页用户列表
     */
    @Query("SELECT u FROM User u WHERE u.realName LIKE %:realName% AND u.isDeleted = false")
    Page<User> findByRealNameContaining(@Param("realName") String realName, Pageable pageable);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.isDeleted = false")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.isDeleted = false")
    boolean existsByEmail(@Param("email") String email);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 是否存在
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.phone = :phone AND u.isDeleted = false")
    boolean existsByPhone(@Param("phone") String phone);
}