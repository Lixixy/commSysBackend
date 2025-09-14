package com.commsys.config;

import com.commsys.annotation.AuthRequired;
import com.commsys.common.Result;
import com.commsys.common.ResultCode;
import com.commsys.entity.Token;
import com.commsys.exception.BusinessException;
import com.commsys.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Token认证拦截器
 * 用于验证请求中的Token是否有效
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {
    
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    
    /**
     * 请求处理前的拦截
     * 验证Token的有效性
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        // 2. 从请求头中获取Token
        String tokenValue = request.getHeader("Authorization");
        if (tokenValue != null && tokenValue.startsWith("Bearer ")) {
            tokenValue = tokenValue.substring(7);
        }
        
        // 3. 获取HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        
        // 4. 检查是否有AuthRequired注解
        if (method.isAnnotationPresent(AuthRequired.class)) {
            AuthRequired authRequired = method.getAnnotation(AuthRequired.class);
            if (authRequired.required()) {
                // 5. 验证Token
                if (tokenValue == null || tokenValue.isEmpty()) {
                    returnResult(response, Result.failure(ResultCode.TOKEN_INVALID, "未提供Token"));
                    return false;
                }
                
                try {
                    // 6. 检查Token是否有效
                    Token token = tokenService.getTokenByValue(tokenValue);
                    if (token == null) {
                        returnResult(response, Result.failure(ResultCode.TOKEN_INVALID, "无效的Token"));
                        return false;
                    }
                    
                    // 7. 检查Token是否已过期
                    if (token.getStatus() == 0 || token.getExpiresAt().isBefore(LocalDateTime.now())) {
                        returnResult(response, Result.failure(ResultCode.TOKEN_EXPIRED, "Token已过期"));
                        return false;
                    }
                    
                    // 8. 将用户ID存入请求属性中，以便后续使用
                    request.setAttribute("userId", token.getUserId());
                    log.debug("Token验证通过，用户ID: {}", token.getUserId());
                } catch (ServiceException e) {
                    log.error("Token验证失败: {}", e.getMessage());
                    returnResult(response, Result.failure(ResultCode.TOKEN_INVALID, e.getMessage()));
                    return false;
                }
            }
        }
        
        // 9. 检查类上是否有AuthRequired注解
        if (handlerMethod.getBeanType().isAnnotationPresent(AuthRequired.class)) {
            AuthRequired authRequired = handlerMethod.getBeanType().getAnnotation(AuthRequired.class);
            if (authRequired.required()) {
                // 10. 验证Token
                if (tokenValue == null || tokenValue.isEmpty()) {
                    returnResult(response, Result.failure(ResultCode.TOKEN_INVALID, "未提供Token"));
                    return false;
                }
                
                try {
                    // 11. 检查Token是否有效
                    Token token = tokenService.getTokenByValue(tokenValue);
                    if (token == null) {
                        returnResult(response, Result.failure(ResultCode.TOKEN_INVALID, "无效的Token"));
                        return false;
                    }
                    
                    // 12. 检查Token是否已过期
                    if (token.getStatus() == 0 || token.getExpiresAt().isBefore(LocalDateTime.now())) {
                        returnResult(response, Result.failure(ResultCode.TOKEN_EXPIRED, "Token已过期"));
                        return false;
                    }
                    
                    // 13. 将用户ID存入请求属性中，以便后续使用
                    request.setAttribute("userId", token.getUserId());
                    log.debug("Token验证通过，用户ID: {}", token.getUserId());
                } catch (ServiceException e) {
                    log.error("Token验证失败: {}", e.getMessage());
                    returnResult(response, Result.failure(ResultCode.TOKEN_INVALID, e.getMessage()));
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 返回错误结果给客户端
     */
    private void returnResult(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}