package com.commsys.service;

import com.commsys.entity.Token;
import com.commsys.entity.User;
import com.commsys.exception.BusinessException;
import com.commsys.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Token服务类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    /**
     * 生成Token
     * 
     * @param user 用户
     * @return Token对象
     */
    @Transactional
    public Token generateToken(User user) {
        log.info("为用户生成Token: {}", user.getUsername());
        
        // 使该用户的所有旧Token过期
        tokenRepository.expireAllUserTokens(user.getId());
        
        // 生成新Token
        Token token = new Token();
        token.setTokenValue(UUID.randomUUID().toString().replace("-", ""));
        token.setUserId(user.getId());
        token.setExpiresAt(LocalDateTime.now().plusHours(24)); // 24小时过期
        token.setStatus(1);
        token.setIsReference(1);
        
        return tokenRepository.save(token);
    }

    /**
     * 验证Token
     * 
     * @param tokenValue Token值
     * @return Token对象
     */
    public Token validateToken(String tokenValue) {
        log.info("验证Token: {}", tokenValue);
        
        Token token = tokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new BusinessException("Token不存在"));
        
        if (token.getStatus() != 1) {
            throw new BusinessException("Token已过期");
        }
        
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            // 标记为过期
            token.setStatus(0);
            tokenRepository.save(token);
            throw new BusinessException("Token已过期");
        }
        
        return token;
    }

    /**
     * 刷新Token
     * 
     * @param oldTokenValue 旧Token值
     * @return 新Token对象
     */
    @Transactional
    public Token refreshToken(String oldTokenValue) {
        log.info("刷新Token: {}", oldTokenValue);
        
        Token oldToken = validateToken(oldTokenValue);
        
        // 检查旧Token是否可参考
        if (oldToken.getIsReference() != 1) {
            throw new BusinessException("旧Token不可参考");
        }
        
        // 使旧Token不可参考
        oldToken.setIsReference(0);
        tokenRepository.save(oldToken);
        
        // 生成新Token
        User user = new User();
        user.setId(oldToken.getUserId());
        return generateToken(user);
    }

    /**
     * 注销Token
     * 
     * @param tokenValue Token值
     */
    @Transactional
    public void logoutToken(String tokenValue) {
        log.info("注销Token: {}", tokenValue);
        
        Token token = tokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new BusinessException("Token不存在"));
        
        token.setStatus(0);
        tokenRepository.save(token);
    }

    /**
     * 清理过期Token
     */
    @Transactional
    public void cleanExpiredTokens() {
        log.info("清理过期Token");
        
        List<Token> expiredTokens = tokenRepository.findExpiredTokens(LocalDateTime.now());
        for (Token token : expiredTokens) {
            token.setStatus(0);
            tokenRepository.save(token);
        }
        
        log.info("清理了{}个过期Token", expiredTokens.size());
    }

    /**
     * 获取用户的有效Token列表
     * 
     * @param userId 用户ID
     * @return Token列表
     */
    public List<Token> getUserValidTokens(Long userId) {
        return tokenRepository.findValidByUserId(userId, LocalDateTime.now());
    }

    /**
     * 获取用户的所有Token列表
     * 
     * @param userId 用户ID
     * @return Token列表
     */
    public List<Token> getUserAllTokens(Long userId) {
        return tokenRepository.findByUserId(userId);
    }
}
