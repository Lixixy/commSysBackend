# 用户管理API

## 用户登录

**请求URL**：`/usr/login`

**请求方法**：`POST`

**请求参数**：

```json
{
  "username": "string", // 用户名
  "passwordHash": "string" // 密码哈希值
}
```

**响应数据**：

```json
{
  "tokenValue": "string", // Token值
  "userId": 1,            // 用户ID
  "expiresAt": "2025-12-31T23:59:59", // 过期时间
  "status": 1             // Token状态：0-已过期，1-有效
}
```

## 用户注册

**请求URL**：`/usr/register`

**请求方法**：`POST`

**请求参数**：

```json
{
  "username": "string", // 用户名
  "passwordHash": "string", // 密码哈希值
  "gender": 1            // 性别：0（未填写）、1（男）、2（女）
}
```

**响应数据**：

```json
{
  "tokenValue": "string", // Token值
  "userId": 1,            // 用户ID
  "expiresAt": "2025-12-31T23:59:59", // 过期时间
  "status": 1             // Token状态：0-已过期，1-有效
}
```

## 高级注册

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/usr/reg_plus`

**请求方法**：`POST`

**请求参数**：

```json
{
  "username": "string", // 用户名
  "passwordHash": "string", // 密码哈希值
  "gender": 1,            // 性别
  "roleId": 2,            // 身份ID：0（无社团学生）、1（社团成员）、2（社长）、3（老师）、4（管理员）、5（超级管理员）
  "operatorUserId": 1     // 操作者用户ID
}
```

**响应数据**：

```json
{
  "tokenValue": "string", // Token值
  "userId": 1,            // 用户ID
  "expiresAt": "2025-12-31T23:59:59", // 过期时间
  "status": 1             // Token状态：0-已过期，1-有效
}
```

## 注销Token

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/logout`

**请求方法**：`POST`

**请求参数**：

```json
{
  "token": "string" // Token值
}
```

**响应数据**：无

## 重新获取Token

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/re_token`

**请求方法**：`GET`

**请求参数**：

- `token`: 旧Token值（Query参数）

**响应数据**：

```json
{
  "tokenValue": "string", // 新Token值
  "userId": 1,            // 用户ID
  "expiresAt": "2025-12-31T23:59:59", // 过期时间
  "status": 1             // Token状态：0-已过期，1-有效
}
```

## 修改个人资料

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/change`

**请求方法**：`POST`

**请求参数**：

- `userId`: 用户ID（Query参数）

请求体：

```json
{
  "username": "string", // 用户名
  "realName": "string", // 真实姓名
  "gender": 1,            // 性别
  "email": "string",    // 邮箱
  "phone": "string"     // 手机号
}
```

**响应数据**：

```json
{
  "id": 1,                // 用户ID
  "username": "string", // 用户名
  "realName": "string", // 真实姓名
  "gender": 1,            // 性别
  "points": 0,            // 积分
  "parentClubId": -1,     // 父社团ID（-1表示无社团）
  "roleId": 1,            // 身份ID
  "email": "string",    // 邮箱
  "phone": "string",    // 手机号
  "status": 1,            // 用户状态：0-禁用，1-启用
  "remark": "string"     // 备注
}
```

## 删除用户

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/del`

**请求方法**：`POST`

**请求参数**：

```json
{
  "operatorUserId": 1,    // 操作者用户ID
  "targetUserId": 2       // 目标用户ID
}
```

**响应数据**：无

## 修改密码

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/change_passw`

**请求方法**：`POST`

**请求参数**：

```json
{
  "userId": 1,            // 用户ID
  "oldPasswordHash": "string", // 旧密码哈希值
  "newPasswordHash": "string", // 新密码哈希值
  "operatorUserId": 1     // 操作者用户ID（可以与userId相同）
}
```

**响应数据**：无

## 用户提权/降权

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/permiss`

**请求方法**：`POST`

**请求参数**：

```json
{
  "targetUserId": 2,      // 目标用户ID
  "operatorUserId": 1,    // 操作者用户ID
  "targetRoleId": 2       // 目标身份ID
}
```

**响应数据**：无

## 分页查询用户

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/all`

**请求方法**：`GET`

**请求参数**：

- `page`: 页码，默认1（Query参数）
- `size`: 每页大小，默认10（Query参数）
- `username`: 用户名关键字（Query参数，可选）
- `realName`: 真实姓名关键字（Query参数，可选）
- `roleId`: 身份ID（Query参数，可选）
- `status`: 状态（Query参数，可选）

**响应数据**：分页用户列表

## 获取所有用户

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/list`

**请求方法**：`GET`

**请求参数**：无

**响应数据**：用户列表

## 根据身份ID获取用户列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/usr/role/{roleId}`

**请求方法**：`GET`

**请求参数**：

- `roleId`: 身份ID（Path参数）

**响应数据**：用户列表
