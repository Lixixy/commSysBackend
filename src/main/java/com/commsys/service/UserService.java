package com.commsys.service;

import com.commsys.common.PageResult;
import com.commsys.entity.Token;
import com.commsys.entity.User;
import com.commsys.exception.BusinessException;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 用户服务类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param passwordHash 密码hash
     * @return Token对象
     */
    @Transactional
    public Token login(String username, String passwordHash) {
        log.info("用户登录: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        
        if (!user.getPasswordHash().equals(passwordHash)) {
            throw new BusinessException("用户名或密码错误");
        }
        
        if (user.getStatus() != 1) {
            throw new BusinessException("用户已被禁用");
        }
        
        return tokenService.generateToken(user);
    }

    /**
     * 用户注册
     * 
     * @param username 用户名
     * @param passwordHash 密码hash
     * @param gender 性别（可选）
     * @return Token对象
     */
    @Transactional
    public Token register(String username, String passwordHash, Integer gender) {
        log.info("用户注册: {}", username);
        
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setGender(gender != null ? gender : 0);
        user.setPoints(0);
        user.setParentClubId(-1L);
        user.setRoleId(0);
        user.setStatus(1);
        
        user = userRepository.save(user);
        
        // 生成Token
        return tokenService.generateToken(user);
    }

    /**
     * 高级注册
     * 
     * @param username 用户名
     * @param passwordHash 密码hash
     * @param gender 性别（可选）
     * @param roleId 身份ID（可选）
     * @param operatorUserId 操作者用户ID
     * @return Token对象
     */
    @Transactional
    public Token registerPlus(String username, String passwordHash, Integer gender, Integer roleId, Long operatorUserId) {
        log.info("高级注册: {}, 身份ID: {}", username, roleId);
        
        // 检查操作者权限
        User operator = getUserById(operatorUserId);
        if (roleId != null && roleId >= 3) {
            if (operator.getRoleId() < 4) {
                throw new BusinessException("权限不足，无法创建此身份的用户");
            }
        }
        
        if (roleId != null && roleId == 5) {
            throw new BusinessException("不能创建超级管理员账号");
        }
        
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setGender(gender != null ? gender : 0);
        user.setPoints(0);
        user.setParentClubId(-1L);
        user.setRoleId(roleId != null ? roleId : 0);
        user.setStatus(1);
        
        user = userRepository.save(user);
        
        // 生成Token
        return tokenService.generateToken(user);
    }

    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    public User getUserById(Long id) {
        log.info("根据ID获取用户: {}", id);
        return userRepository.findActiveById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    /**
     * 根据用户名获取用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    public User getUserByUsername(String username) {
        log.info("根据用户名获取用户: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    /**
     * 修改个人资料
     * 
     * @param userId 用户ID
     * @param user 用户信息
     * @return 更新后的用户
     */
    @Transactional
    public User changeProfile(Long userId, User user) {
        log.info("修改个人资料: {}", userId);
        
        User existingUser = getUserById(userId);
        
        // 更新可修改的字段
        if (StringUtils.hasText(user.getEmail())) {
            existingUser.setEmail(user.getEmail());
        }
        if (StringUtils.hasText(user.getPhone())) {
            existingUser.setPhone(user.getPhone());
        }
        if (StringUtils.hasText(user.getRealName())) {
            existingUser.setRealName(user.getRealName());
        }
        if (user.getGender() != null) {
            existingUser.setGender(user.getGender());
        }
        if (StringUtils.hasText(user.getRemark())) {
            existingUser.setRemark(user.getRemark());
        }
        
        return userRepository.save(existingUser);
    }

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param oldPasswordHash 旧密码hash
     * @param newPasswordHash 新密码hash
     * @param operatorUserId 操作者用户ID
     */
    @Transactional
    public void changePassword(Long userId, String oldPasswordHash, String newPasswordHash, Long operatorUserId) {
        log.info("修改密码: {}", userId);
        
        User user = getUserById(userId);
        User operator = getUserById(operatorUserId);
        
        // 检查权限
        if (!operatorUserId.equals(userId) && operator.getRoleId() < 4) {
            throw new BusinessException("权限不足");
        }
        
        // 验证旧密码
        if (!user.getPasswordHash().equals(oldPasswordHash)) {
            throw new BusinessException("旧密码错误");
        }
        
        // 更新密码
        user.setPasswordHash(newPasswordHash);
        userRepository.save(user);
        
        // 使该用户的所有Token过期
        tokenService.logoutToken(userId.toString());
    }

    /**
     * 用户提权/降权
     * 
     * @param targetUserId 目标用户ID
     * @param operatorUserId 操作者用户ID
     * @param targetRoleId 目标身份ID
     */
    @Transactional
    public void changePermission(Long targetUserId, Long operatorUserId, Integer targetRoleId) {
        log.info("用户提权/降权: 目标用户={}, 操作者={}, 目标身份={}", targetUserId, operatorUserId, targetRoleId);
        
        User targetUser = getUserById(targetUserId);
        User operator = getUserById(operatorUserId);
        
        // 检查权限
        if (targetRoleId == 1 || targetRoleId == 2) {
            if (operator.getRoleId() < 3) {
                throw new BusinessException("权限不足");
            }
        } else if (targetRoleId == 3) {
            if (operator.getRoleId() < 4) {
                throw new BusinessException("权限不足");
            }
        } else if (targetRoleId == 4) {
            if (operator.getRoleId() < 5) {
                throw new BusinessException("权限不足");
            }
        } else if (targetRoleId == 5) {
            throw new BusinessException("不能设置为超级管理员");
        }
        
        // 更新身份
        targetUser.setRoleId(targetRoleId);
        userRepository.save(targetUser);
    }

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @param operatorUserId 操作者用户ID
     */
    @Transactional
    public void deleteUser(Long userId, Long operatorUserId) {
        log.info("删除用户: {}, 操作者: {}", userId, operatorUserId);
        
        User user = getUserById(userId);
        User operator = getUserById(operatorUserId);
        
        // 检查权限
        if (!operatorUserId.equals(userId) && operator.getRoleId() < 4) {
            throw new BusinessException("权限不足");
        }
        
        userRepository.softDeleteById(userId);
    }

    /**
     * 分页查询用户
     * 
     * @param page 页码
     * @param size 每页大小
     * @param username 用户名关键字
     * @param realName 真实姓名关键字
     * @param roleId 身份ID
     * @param status 状态
     * @return 分页用户列表
     */
    public PageResult<User> getUsers(Integer page, Integer size, String username, String realName, 
                                   Integer roleId, Integer status) {
        log.info("分页查询用户: page={}, size={}, username={}, realName={}, roleId={}, status={}", 
                page, size, username, realName, roleId, status);
        
        // 创建分页参数
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<User> userPage;
        
        if (StringUtils.hasText(username)) {
            userPage = userRepository.findByUsernameContaining(username, pageable);
        } else if (StringUtils.hasText(realName)) {
            userPage = userRepository.findByRealNameContaining(realName, pageable);
        } else if (roleId != null && status != null) {
            List<User> users = userRepository.findByRoleIdAndStatus(roleId, status);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), users.size());
            List<User> pageContent = users.subList(start, end);
            userPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, users.size());
        } else if (roleId != null) {
            List<User> users = userRepository.findByRoleId(roleId);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), users.size());
            List<User> pageContent = users.subList(start, end);
            userPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, users.size());
        } else if (status != null) {
            List<User> users = userRepository.findByStatus(status);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), users.size());
            List<User> pageContent = users.subList(start, end);
            userPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, users.size());
        } else {
            userPage = userRepository.findAllActive(pageable);
        }
        
        return PageResult.of(userPage);
    }

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        log.info("获取所有用户");
        return userRepository.findAllActive();
    }

    /**
     * 根据身份ID获取用户列表
     * 
     * @param roleId 身份ID
     * @return 用户列表
     */
    public List<User> getUsersByRoleId(Integer roleId) {
        log.info("根据身份ID获取用户列表: {}", roleId);
        return userRepository.findByRoleId(roleId);
    }

    /**
     * 根据父社团ID获取用户列表
     * 
     * @param parentClubId 父社团ID
     * @return 用户列表
     */
    public List<User> getUsersByParentClubId(Long parentClubId) {
        log.info("根据父社团ID获取用户列表: {}", parentClubId);
        return userRepository.findByParentClubId(parentClubId);
    }

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 是否存在
     */
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * 生成密码hash
     * 
     * @param password 原始密码
     * @return 密码hash
     */
    public String generatePasswordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("密码加密失败");
        }
    }

    /**
     * 用户登出
     * 
     * @param token 用户Token
     */
    @Transactional
    public void logout(String token) {
        log.info("用户登出: {}", token);
        tokenService.logoutToken(token);
    }

    /**
     * 刷新Token
     * 
     * @param oldToken 旧Token
     * @return 新Token
     */
    @Transactional
    public Token refreshToken(String oldToken) {
        log.info("刷新Token: {}", oldToken);
        return tokenService.refreshToken(oldToken);
    }
}