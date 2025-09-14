# 社团管理系统后端服务

基于Spring Boot的社团管理系统后端服务，支持用户管理、社团管理、活动管理等功能。

## 功能特性

- ✅ 用户管理（注册、登录、权限管理）
- ✅ 社团管理（创建、加入、退出、禁用/启用）
- ✅ 活动管理（创建、编辑、删除、提前结束）
- ✅ Token认证机制
- ✅ 权限控制（基于身份ID的权限管理）
- ✅ 动态配置管理
- ✅ 支持MySQL和SQLite数据库
- ✅ 统一异常处理和响应封装
- ✅ 分页查询支持
- ✅ 数据验证和参数校验
- ✅ 逻辑删除支持
- ✅ 生产环境部署脚本

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0 / SQLite 3.44
- **ORM**: Spring Data JPA + Hibernate
- **连接池**: HikariCP
- **构建工具**: Maven
- **Java版本**: 21

## 项目结构

```
src/
├── main/
│   ├── java/com/commsys/
│   │   ├── CommSysApplication.java          # 主启动类
│   │   ├── common/                          # 通用组件
│   │   │   ├── Result.java                  # 统一响应封装
│   │   │   ├── ResultCode.java             # 响应码枚举
│   │   │   └── PageResult.java             # 分页结果封装
│   │   ├── config/                          # 配置类
│   │   │   ├── AppConfig.java              # 应用配置
│   │   │   ├── StartupConfig.java          # 启动配置
│   │   │   └── WebConfig.java              # Web配置
│   │   ├── controller/                      # 控制器层
│   │   │   ├── UserController.java         # 用户控制器
│   │   │   ├── ClubController.java         # 社团控制器
│   │   │   ├── ActivityController.java     # 活动控制器
│   │   │   ├── ConfigController.java       # 配置控制器
│   │   │   └── SystemController.java       # 系统控制器
│   │   ├── entity/                          # 实体类
│   │   │   ├── BaseEntity.java             # 基础实体
│   │   │   ├── User.java                   # 用户实体
│   │   │   ├── Club.java                   # 社团实体
│   │   │   ├── Activity.java               # 活动实体
│   │   │   ├── ClubMember.java             # 社团成员实体
│   │   │   ├── Token.java                  # Token实体
│   │   │   └── Config.java                 # 配置实体
│   │   ├── exception/                       # 异常处理
│   │   │   ├── BusinessException.java      # 业务异常
│   │   │   └── GlobalExceptionHandler.java # 全局异常处理器
│   │   ├── repository/                      # 数据访问层
│   │   │   ├── BaseRepository.java         # 基础仓储接口
│   │   │   ├── UserRepository.java         # 用户仓储
│   │   │   ├── ClubRepository.java         # 社团仓储
│   │   │   ├── ActivityRepository.java     # 活动仓储
│   │   │   ├── ClubMemberRepository.java   # 社团成员仓储
│   │   │   ├── TokenRepository.java        # Token仓储
│   │   │   └── ConfigRepository.java       # 配置仓储
│   │   └── service/                         # 业务逻辑层
│   │       ├── UserService.java            # 用户服务
│   │       ├── ClubService.java            # 社团服务
│   │       ├── ActivityService.java        # 活动服务
│   │       ├── TokenService.java           # Token服务
│   │       └── ConfigService.java          # 配置服务
│   └── resources/
│       ├── application.yml                 # 主配置文件
│       ├── application-prod.yml            # 生产环境配置
│       └── application-test.yml            # 测试环境配置
├── test/                                    # 测试代码
└── scripts/
    └── start.sh                            # Linux生产环境启动脚本
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+
- MySQL 8.0+ (可选)
- SQLite 3.44+ (可选)

### 1. 克隆项目

```bash
git clone <repository-url>
cd comm-sys
```

### 2. 配置数据库

#### 使用SQLite（默认）

直接启动应用，无需额外配置：

```bash
mvn spring-boot:run
```

#### 使用MySQL

1. 创建数据库：

```sql
CREATE DATABASE comm_sys CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/comm_sys?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      dialect: org.hibernate.dialect.MySQL8Dialect
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

3. 启动应用：

```bash
mvn spring-boot:run
```

### 3. 验证安装

访问以下URL验证系统是否正常运行：

- 系统信息: http://localhost:8080/api/system/info
- 健康检查: http://localhost:8080/api/system/health

## 生产环境部署

1. 运行JAR包：

```bash
java -jar target/comm-sys-1.0.0.jar --spring.profiles.active=prod
```

## API接口

### 用户管理接口

| 方法   | 路径                             | 描述          |
|------|--------------------------------|-------------|
| POST | `/api/usr/login`               | 用户登录        |
| POST | `/api/usr/register`            | 用户注册        |
| POST | `/api/usr/reg_plus`            | 高级注册        |
| POST | `/api/usr/logout`              | 注销Token     |
| GET  | `/api/usr/re_token`            | 刷新Token     |
| POST | `/api/usr/change`              | 修改个人资料      |
| POST | `/api/usr/del`                 | 删除用户        |
| POST | `/api/usr/change_passw`        | 修改密码        |
| POST | `/api/usr/permiss`             | 用户提权/降权     |
| GET  | `/api/usr/all`                 | 分页查询用户      |
| GET  | `/api/usr/list`                | 获取所有用户      |
| GET  | `/api/usr/role/{roleId}`       | 根据身份获取用户    |
| GET  | `/api/usr/club/{parentClubId}` | 根据父社团ID获取用户 |

### 社团管理接口

| 方法   | 路径                                  | 描述          |
|------|-------------------------------------|-------------|
| POST | `/api/club/create`                  | 创建社团        |
| POST | `/api/club/close_open`              | 禁用/启用社团     |
| POST | `/api/club/join`                    | 申请加入社团      |
| POST | `/api/club/exit`                    | 退出社团        |
| GET  | `/api/club/all`                     | 获取所有社团      |
| GET  | `/api/club/{id}`                    | 根据ID获取社团    |
| GET  | `/api/club/status/{status}`         | 根据状态获取社团    |
| GET  | `/api/club/president/{presidentId}` | 根据社长ID获取社团  |
| GET  | `/api/club`                         | 分页查询社团      |
| GET  | `/api/club/{clubId}/members`        | 获取社团成员列表    |
| GET  | `/api/club/user/{userId}`           | 获取用户所在的社团列表 |

### 活动管理接口

| 方法   | 路径                                            | 描述            |
|------|-----------------------------------------------|---------------|
| POST | `/api/activity/create`                        | 创建活动          |
| POST | `/api/activity/del`                           | 删除活动          |
| POST | `/api/activity/change`                        | 编辑活动          |
| POST | `/api/activity/close`                         | 提前结束活动        |
| GET  | `/api/activity/all`                           | 获取所有活动        |
| GET  | `/api/activity/{id}`                          | 根据ID获取活动      |
| GET  | `/api/activity/club/{clubId}`                 | 根据社团ID获取活动    |
| GET  | `/api/activity/creator/{creatorId}`           | 根据发起者ID获取活动   |
| GET  | `/api/activity/status/{status}`               | 根据状态获取活动      |
| GET  | `/api/activity/club/{clubId}/status/{status}` | 根据社团ID和状态获取活动 |
| GET  | `/api/activity/time`                          | 根据时间范围获取活动    |
| GET  | `/api/activity/ongoing`                       | 获取正在进行的活动     |
| GET  | `/api/activity/ended`                         | 获取已结束的活动      |
| GET  | `/api/activity`                               | 分页查询活动        |

### 配置管理接口

| 方法     | 路径                                      | 描述          |
|--------|-----------------------------------------|-------------|
| POST   | `/api/config`                           | 创建配置        |
| GET    | `/api/config/{id}`                      | 根据ID获取配置    |
| GET    | `/api/config/key/{configKey}`           | 根据配置键获取配置   |
| GET    | `/api/config/value/{configKey}`         | 获取配置值       |
| GET    | `/api/config/value/{configKey}/default` | 获取配置值（带默认值） |
| PUT    | `/api/config/{id}`                      | 更新配置        |
| PUT    | `/api/config/value/{configKey}`         | 更新配置值       |
| DELETE | `/api/config/{id}`                      | 删除配置        |
| DELETE | `/api/config/batch`                     | 批量删除配置      |
| GET    | `/api/config`                           | 分页查询配置      |
| GET    | `/api/config/all`                       | 获取所有配置      |
| GET    | `/api/config/group/{configGroup}`       | 根据配置分组获取配置  |
| GET    | `/api/config/type/{configType}`         | 根据配置类型获取配置  |
| GET    | `/api/config/groups`                    | 获取所有配置分组    |
| POST   | `/api/config/init`                      | 初始化默认配置     |

### 系统接口

| 方法   | 路径                                       | 描述            |
|------|------------------------------------------|---------------|
| GET  | `/api/system/info`                       | 获取系统信息        |
| GET  | `/api/system/health`                     | 健康检查          |
| GET  | `/api/system/config/{configKey}`         | 获取动态配置值       |
| GET  | `/api/system/config/{configKey}/default` | 获取动态配置值（带默认值） |
| POST | `/api/system/del_dbdata`                 | 手动删除数据库数据     |

## 用户身份说明

- **0**: 无社团学生
- **1**: 社团成员
- **2**: 社长
- **3**: 老师
- **4**: 管理员
- **5**: 超级管理员

## 权限说明

- **创建社团**: 需要管理员以上权限（身份ID >= 4）
- **创建活动**: 需要社长以上权限（身份ID >= 2）
- **删除活动**: 需要社长以上权限，且只能删除自己社团的活动
- **编辑活动**: 需要社长以上权限，且只能编辑自己社团的活动
- **用户提权/降权**: 根据目标身份等级需要相应权限
- **高级注册**: 根据目标身份等级需要相应权限

## 配置说明

### 数据库配置

系统支持MySQL和SQLite两种数据库，可以通过以下方式切换：

1. **环境变量方式**：

```bash
export DB_TYPE=mysql  # 或 sqlite
mvn spring-boot:run
```

2. **配置文件方式**：
   修改 `application.yml` 中的 `app.database.type` 配置

3. **启动参数方式**：

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--app.database.type=mysql"
```

### 动态配置管理

系统支持运行时动态修改配置，配置信息存储在数据库中：

- 配置类型：STRING（字符串）、NUMBER（数字）、BOOLEAN（布尔值）、JSON（JSON对象）
- 配置分组：SYSTEM（系统）、DATABASE（数据库）、PAGE（分页）、UPLOAD（上传）、TOKEN（Token）等
- 可修改性：支持设置配置是否可修改

## 测试

### 运行测试

```bash
mvn test
```

### API测试

使用 `api-test.http` 文件测试所有API接口，或使用Postman等工具。

## 开发指南

### 添加新的实体

1. 继承 `BaseEntity` 类
2. 添加JPA注解
3. 创建对应的Repository接口
4. 创建Service类
5. 创建Controller类

### 添加新的配置

1. 在 `ConfigService.initDefaultConfigs()` 方法中添加默认配置
2. 通过API接口动态管理配置

### 异常处理

- 使用 `BusinessException` 抛出业务异常
- 全局异常处理器会自动捕获并返回统一格式的错误响应

## 部署

### 打包应用

```bash
mvn clean package
```

### 运行JAR包

```bash
java -jar target/comm-sys-1.0.0.jar
```

### Docker部署

```dockerfile
FROM openjdk:21-jre-slim
COPY target/comm-sys-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 许可证

仓库代码暂时仅展示用。