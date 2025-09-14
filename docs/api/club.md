# 社团管理API

## 创建社团

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/club/create`

**请求方法**：`POST`

**请求参数**：

```json
{
  "title": "string",       // 社团标题
  "description": "string", // 社团简介
  "presidentId": 1,         // 社长用户ID
  "teacherId": 2,           // 社团指导老师ID（可选）
  "memberIds": [3, 4, 5],   // 社团成员ID列表
  "operatorUserId": 1       // 操作者用户ID
}
```

**响应数据**：

```json
{
  "id": 1,                 // 社团ID
  "title": "string",      // 社团标题
  "description": "string",// 社团简介
  "presidentId": 1,        // 社长用户ID
  "teacherId": 2,          // 社团指导老师ID
  "status": 1,             // 社团状态：0-禁用，1-启用
  "disableReason": "string",// 禁用原因
  "memberIds": [3, 4, 5]   // 社团成员ID列表
}
```

## 禁用/启用社团

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`.

**请求URL**：`/club/close_open`

**请求方法**：`POST`

**请求参数**：

```json
{
  "isEnabled": true,       // 是否启用
  "operatorUserId": 1,     // 操作者用户ID
  "clubId": 1,             // 社团ID
  "disableReason": "string" // 禁用原因（当isEnabled为false时必填）
}
```

**响应数据**：无

## 申请加入社团

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/join`

**请求方法**：`POST`

**请求参数**：

```json
{
  "userId": 1,             // 用户ID
  "clubId": 2              // 社团ID
}
```

**响应数据**：无

## 退出社团

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/exit`

**请求方法**：`POST`

**请求参数**：

```json
{
  "userId": 1              // 用户ID
}
```

**响应数据**：无

## 获取所有社团

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/all`

**请求方法**：`GET`

**请求参数**：无

**响应数据**：社团列表

## 根据ID获取社团

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/{id}`

**请求方法**：`GET`

**请求参数**：

- `id`: 社团ID（Path参数）

**响应数据**：社团详情

## 根据状态获取社团列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/status/{status}`

**请求方法**：`GET`

**请求参数**：

- `status`: 状态（Path参数）

**响应数据**：社团列表

## 根据社长ID获取社团列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/president/{presidentId}`

**请求方法**：`GET`

**请求参数**：

- `presidentId`: 社长ID（Path参数）

**响应数据**：社团列表

## 分页查询社团

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club`

**请求方法**：`GET`

**请求参数**：

- `page`: 页码，默认1（Query参数）
- `size`: 每页大小，默认10（Query参数）
- `title`: 社团标题关键字（Query参数，可选）
- `status`: 状态（Query参数，可选）

**响应数据**：分页社团列表

## 获取社团成员列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/{clubId}/members`

**请求方法**：`GET`

**请求参数**：

- `clubId`: 社团ID（Path参数）

**响应数据**：社团成员列表

## 获取用户所在的社团列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/user/{userId}`

**请求方法**：`GET`

**请求参数**：

- `userId`: 用户ID（Path参数）

**响应数据**：用户所在社团列表

## 检查用户是否在社团中

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`

**请求URL**：`/club/{clubId}/check/{userId}`

**请求方法**：`GET`

**请求参数**：

- `clubId`: 社团ID（Path参数）
- `userId`: 用户ID（Path参数）

**响应数据**：布尔值，表示用户是否在社团中
