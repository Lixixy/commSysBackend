# 活动管理API

## 创建活动

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/create`

**请求方法**：`POST`

**请求参数**：

```json
{
  "clubId": 1,              // 社团ID
  "creatorId": 2,           // 发起者用户ID
  "title": "string",       // 活动标题
  "description": "string", // 活动描述
  "startTime": "2025-01-01T10:00:00", // 开始时间
  "endTime": "2025-01-01T12:00:00"     // 结束时间
}
```

**响应数据**：

```json
{
  "id": 1,                  // 活动ID
  "clubId": 1,              // 社团ID
  "creatorId": 2,           // 发起者用户ID
  "title": "string",       // 活动标题
  "description": "string", // 活动描述
  "startTime": "2025-01-01T10:00:00", // 开始时间
  "endTime": "2025-01-01T12:00:00",   // 结束时间
  "status": 1,              // 活动状态：0-已取消，1-进行中，2-已结束
  "closeReason": "string", // 提前结束原因
  "actualEndTime": "2025-01-01T11:30:00" // 实际结束时间
}
```

## 删除活动

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/del`

**请求方法**：`POST`

**请求参数**：

```json
{
  "clubId": 1,              // 社团ID
  "activityId": 2,          // 活动ID
  "operatorId": 1           // 操作者用户ID
}
```

**响应数据**：无

## 编辑活动

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/change`

**请求方法**：`POST`

**请求参数**：

```json
{
  "activityId": 1,          // 活动ID
  "operatorId": 2,          // 操作者用户ID
  "clubId": 1,              // 社团ID
  "title": "string",       // 活动标题
  "description": "string", // 活动描述
  "startTime": "2025-01-01T10:00:00", // 开始时间
  "endTime": "2025-01-01T12:00:00"     // 结束时间
}
```

**响应数据**：更新后的活动信息

## 提前结束活动

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/close`

**请求方法**：`POST`

**请求参数**：

```json
{
  "clubId": 1,              // 社团ID
  "activityId": 2,          // 活动ID
  "operatorId": 1,          // 操作者用户ID
  "closeReason": "string"   // 结束原因
}
```

**响应数据**：无

## 获取所有活动

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/all`

**请求方法**：`GET`

**请求参数**：无

**响应数据**：活动列表

## 根据ID获取活动

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/{id}`

**请求方法**：`GET`

**请求参数**：

- `id`: 活动ID（Path参数）

**响应数据**：活动详情

## 根据社团ID获取活动列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/club/{clubId}`

**请求方法**：`GET`

**请求参数**：

- `clubId`: 社团ID（Path参数）

**响应数据**：活动列表

## 根据发起者ID获取活动列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/creator/{creatorId}`

**请求方法**：`GET`

**请求参数**：

- `creatorId`: 发起者ID（Path参数）

**响应数据**：活动列表

## 根据状态获取活动列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/status/{status}`

**请求方法**：`GET`

**请求参数**：

- `status`: 状态（Path参数）

**响应数据**：活动列表

## 根据社团ID和状态获取活动列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/club/{clubId}/status/{status}`

**请求方法**：`GET`

**请求参数**：

- `clubId`: 社团ID（Path参数）
- `status`: 状态（Path参数）

**响应数据**：活动列表

## 根据时间范围获取活动列表

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/time`

**请求方法**：`GET`

**请求参数**：

- `startTime`: 开始时间（Query参数）
- `endTime`: 结束时间（Query参数）

**响应数据**：活动列表

## 获取正在进行的活动

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/ongoing`

**请求方法**：`GET`

**请求参数**：无

**响应数据**：正在进行的活动列表

## 获取已结束的活动

> [!important]
> 此接口需要进行Token认证。请在请求头中添加 `Authorization: Bearer {tokenValue}`。

**请求URL**：`/activity/ended`

**请求方法**：`GET`

**请求参数**：无

**响应数据**：已结束的活动列表
