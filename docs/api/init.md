# 社团管理系统API文档

## 接口概述

本API文档描述了社团管理系统的所有后端接口，包括用户管理、社团管理和活动管理等功能。

### 基础URL

所有API请求的基础URL为：`http://your-domain/api`

### 统一响应格式

系统所有API接口的响应都采用统一的格式：

```json
{
  "code": 200,
  "message": "成功信息",
  "data": {}, // 具体的响应数据
  "timestamp": 1634567890000
}
```

#### 响应码说明

| 响应码 | 描述 |
|-------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 405 | 方法不允许 |
| 500 | 内部服务器错误 |

### 分页响应格式

对于分页查询接口，响应数据格式为：

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "records": [], // 数据列表
    "total": 0,    // 总记录数
    "page": 1,     // 当前页码
    "size": 10,    // 每页大小
    "pages": 1     // 总页数
  },
  "timestamp": 1634567890000
}
```

### Token认证机制

> [!important]
> 除了用户登录(`/usr/login`)、用户注册(`/usr/register`)、接口检查(`/check`)和系统初始化(`/init`)等少数接口外，**所有其他API接口都需要进行Token认证**。

#### Token获取方式

用户可以通过登录或注册接口获取Token：

1. [用户登录接口](user.md#用户登录)
2. [用户注册接口](user.md#用户注册)

成功获取Token后，响应数据中会包含以下信息：

```json
{
  "tokenValue": "string", // Token值
  "userId": 1,            // 用户ID
  "expiresAt": "2025-12-31T23:59:59", // 过期时间
  "status": 1             // Token状态：0-已过期，1-有效
}
```

#### Token使用方式

所有需要认证的API请求，都需要在请求头中添加`Authorization`字段，格式为：

```http
Authorization: Bearer {tokenValue}
```

例如：

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Token相关错误码

| 响应码 | 描述 | 说明 |
|-------|------|------|
| 401 | 未授权 | 未提供Token或Token无效 |
| 3001 | Token Expired | Token已过期 |
| 3002 | Token Invalid | Token无效或已被注销 |

## API目录

- [用户管理API](user.md)
- [社团管理API](club.md)
- [活动管理API](activity.md)
