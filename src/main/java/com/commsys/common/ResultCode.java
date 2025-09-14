package com.commsys.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应结果码枚举
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "OK"),

    /**
     * 失败
     */
    ERROR(500, "Internal Server Error"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "Bad Request"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "Forbidden"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * 方法不允许
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /**
     * 冲突
     */
    CONFLICT(409, "Conflict"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    /**
     * 数据库错误
     */
    DATABASE_ERROR(1001, "Database Error"),

    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(1002, "Data Not Found"),

    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTS(1003, "Data Already Exists"),

    /**
     * 数据验证失败
     */
    DATA_VALIDATION_ERROR(1004, "Data Validation Error"),

    /**
     * 业务逻辑错误
     */
    BUSINESS_ERROR(2001, "Business Error"),

    /**
     * Token过期
     */
    TOKEN_EXPIRED(3001, "Token Expired"),

    /**
     * Token无效
     */
    TOKEN_INVALID(3002, "Token Invalid"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(3003, "Permission Denied"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(3004, "User Not Found"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR(3005, "Password Error"),

    /**
     * 用户已存在
     */
    USER_ALREADY_EXISTS(3006, "User Already Exists"),

    /**
     * 社团不存在
     */
    CLUB_NOT_FOUND(4001, "Club Not Found"),

    /**
     * 活动不存在
     */
    ACTIVITY_NOT_FOUND(4002, "Activity Not Found"),

    /**
     * 配置错误
     */
    CONFIG_ERROR(5001, "Config Error"),

    /**
     * 配置不存在
     */
    CONFIG_NOT_FOUND(5002, "Config Not Found"),

    /**
     * 配置已存在
     */
    CONFIG_ALREADY_EXISTS(5003, "Config Already Exists"),

    /**
     * 配置不可修改
     */
    CONFIG_NOT_MODIFIABLE(5004, "Config Not Modifiable");

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;
}