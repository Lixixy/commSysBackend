package com.commsys.controller;

import com.commsys.common.PageResult;
import com.commsys.common.Result;
import com.commsys.entity.Token;
import com.commsys.entity.User;
import com.commsys.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 提供用户相关的REST API接口
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Slf4j
@RestController
@RequestMapping("/usr")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     * 
     * @param request 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<Token> login(@Valid @RequestBody LoginRequest request) {
        log.info("用户登录请求: {}", request.getUsername());
        Token token = userService.login(request.getUsername(), request.getPasswordHash());
        return Result.success("登录成功", token);
    }

    /**
     * 用户注册
     * 
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Token> register(@Valid @RequestBody RegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());
        Token token = userService.register(request.getUsername(), request.getPasswordHash(), request.getGender());
        return Result.success("注册成功", token);
    }

    /**
     * 高级注册
     * 
     * @param request 高级注册请求
     * @return 注册结果
     */
    @PostMapping("/reg_plus")
    public Result<Token> registerPlus(@Valid @RequestBody RegisterPlusRequest request) {
        log.info("高级注册请求: {}, 身份ID: {}", request.getUsername(), request.getRoleId());
        Token token = userService.registerPlus(request.getUsername(), request.getPasswordHash(), 
                request.getGender(), request.getRoleId(), request.getOperatorUserId());
        return Result.success("高级注册成功", token);
    }

    /**
     * 注销Token
     * 
     * @param request 注销请求
     * @return 注销结果
     */
    @PostMapping("/logout")
    public Result<Void> logout(@Valid @RequestBody LogoutRequest request) {
        log.info("用户注销请求: {}", request.getToken());
        userService.logout(request.getToken());
        return Result.success("注销成功");
    }

    /**
     * 重新获取Token
     * 
     * @param token 旧Token
     * @return 新Token
     */
    @GetMapping("/re_token")
    public Result<Token> refreshToken(@RequestParam String token) {
        log.info("刷新Token请求: {}", token);
        Token newToken = userService.refreshToken(token);
        return Result.success("Token刷新成功", newToken);
    }

    /**
     * 修改个人资料
     * 
     * @param userId 用户ID
     * @param user 用户信息
     * @return 修改结果
     */
    @PostMapping("/change")
    public Result<User> changeProfile(@RequestParam Long userId, @Valid @RequestBody User user) {
        log.info("修改个人资料请求: {}", userId);
        User updatedUser = userService.changeProfile(userId, user);
        return Result.success("个人资料修改成功", updatedUser);
    }

    /**
     * 删除用户
     * 
     * @param request 删除请求
     * @return 删除结果
     */
    @PostMapping("/del")
    public Result<Void> deleteUser(@Valid @RequestBody DeleteUserRequest request) {
        log.info("删除用户请求: 操作者={}, 目标用户={}", request.getOperatorUserId(), request.getTargetUserId());
        userService.deleteUser(request.getTargetUserId(), request.getOperatorUserId());
        return Result.success("用户删除成功");
    }

    /**
     * 修改密码
     * 
     * @param request 修改密码请求
     * @return 修改结果
     */
    @PostMapping("/change_passw")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        log.info("修改密码请求: 用户ID={}", request.getUserId());
        userService.changePassword(request.getUserId(), request.getOldPasswordHash(), 
                request.getNewPasswordHash(), request.getOperatorUserId());
        return Result.success("密码修改成功");
    }

    /**
     * 用户提权/降权
     * 
     * @param request 权限修改请求
     * @return 修改结果
     */
    @PostMapping("/permiss")
    public Result<Void> changePermission(@Valid @RequestBody ChangePermissionRequest request) {
        log.info("用户提权/降权请求: 目标用户={}, 操作者={}, 目标身份={}", 
                request.getTargetUserId(), request.getOperatorUserId(), request.getTargetRoleId());
        userService.changePermission(request.getTargetUserId(), request.getOperatorUserId(), request.getTargetRoleId());
        return Result.success("权限修改成功");
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
    @GetMapping("/all")
    public Result<PageResult<User>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) Integer status) {
        log.info("分页查询用户请求: page={}, size={}, username={}, realName={}, roleId={}, status={}", 
                page, size, username, realName, roleId, status);
        PageResult<User> pageResult = userService.getUsers(page, size, username, realName, roleId, status);
        return Result.success(pageResult);
    }

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    @GetMapping("/list")
    public Result<List<User>> getAllUsers() {
        log.info("获取所有用户请求");
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    /**
     * 根据身份ID获取用户列表
     * 
     * @param roleId 身份ID
     * @return 用户列表
     */
    @GetMapping("/role/{roleId}")
    public Result<List<User>> getUsersByRoleId(@PathVariable Integer roleId) {
        log.info("根据身份ID获取用户请求: {}", roleId);
        List<User> users = userService.getUsersByRoleId(roleId);
        return Result.success(users);
    }

    /**
     * 根据父社团ID获取用户列表
     * 
     * @param parentClubId 父社团ID
     * @return 用户列表
     */
    @GetMapping("/club/{parentClubId}")
    public Result<List<User>> getUsersByParentClubId(@PathVariable Long parentClubId) {
        log.info("根据父社团ID获取用户请求: {}", parentClubId);
        List<User> users = userService.getUsersByParentClubId(parentClubId);
        return Result.success(users);
    }

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 检查结果
     */
    @GetMapping("/check/username/{username}")
    public Result<Boolean> checkUsername(@PathVariable String username) {
        log.info("检查用户名请求: {}", username);
        boolean exists = userService.existsByUsername(username);
        return Result.success(exists);
    }

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 检查结果
     */
    @GetMapping("/check/email/{email}")
    public Result<Boolean> checkEmail(@PathVariable String email) {
        log.info("检查邮箱请求: {}", email);
        boolean exists = userService.existsByEmail(email);
        return Result.success(exists);
    }

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @return 检查结果
     */
    @GetMapping("/check/phone/{phone}")
    public Result<Boolean> checkPhone(@PathVariable String phone) {
        log.info("检查手机号请求: {}", phone);
        boolean exists = userService.existsByPhone(phone);
        return Result.success(exists);
    }

    // 内部类：请求对象
    @Setter
    @Getter
    public static class LoginRequest {
        private String username;
        private String passwordHash;

    }

    @Setter
    @Getter
    public static class RegisterRequest {
        private String username;
        private String passwordHash;
        private Integer gender;

    }

    @Setter
    @Getter
    public static class RegisterPlusRequest {
        private String username;
        private String passwordHash;
        private Integer gender;
        private Integer roleId;
        private Long operatorUserId;

    }

    @Setter
    @Getter
    public static class LogoutRequest {
        private String token;

    }

    @Setter
    @Getter
    public static class DeleteUserRequest {
        private Long operatorUserId;
        private Long targetUserId;

    }

    @Setter
    @Getter
    public static class ChangePasswordRequest {
        private Long userId;
        private String oldPasswordHash;
        private String newPasswordHash;
        private Long operatorUserId;

    }

    @Setter
    @Getter
    public static class ChangePermissionRequest {
        private Long targetUserId;
        private Long operatorUserId;
        private Integer targetRoleId;

    }
}