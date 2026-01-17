# Finance Common Framework 项目说明

## 项目概述

Finance Common Framework 是一个通用业务框架，旨在为金融相关项目提供基础配置、工具类、通用组件等，以促进代码复用、降低项目间耦合度，并便于统一维护和升级通用功能。该项目基于 Spring Boot 3.5.9 构建，采用 Java 21 开发。

## 技术栈

- **核心框架**: Spring Boot 3.5.9, Spring Cloud Alibaba
- **编程语言**: Java 21
- **数据库**: MySQL, MyBatis Plus
- **安全**: JWT, Argon2 密码加密
- **工具库**: Apache Commons, FastJSON2, Lombok, MapStruct
- **API文档**: SpringDoc OpenAPI, Knife4j
- **监控**: Micrometer + Prometheus
- **注册/配置中心**: Nacos

## 项目结构

```
finance-common-framework/
├── pom.xml
├── FRAMEWORK_MODULE_PLAN.md
├── README.md
├── src/main/java/com/tigercub/commonframework/
│   ├── autoconfigure/          # 自动装配配置
│   │   ├── CommonFrameworkAutoConfiguration.java  # 自动配置主类
│   │   └── CommonFrameworkProperties.java         # 配置属性类
│   ├── config/                 # 通用配置
│   │   ├── mybatis/            # MyBatis相关配置
│   │   ├── swagger/            # OpenAPI文档配置
│   │   └── web/                # Web相关配置
│   ├── constant/               # 通用常量
│   ├── context/                # 上下文管理
│   ├── enums/                  # 通用枚举
│   ├── exception/              # 通用异常处理
│   ├── filter/                 # 通用过滤器
│   ├── model/                  # 数据模型（DTO等）
│   ├── util/                   # 通用工具类
│   │   ├── DateTimeUtils.java  # 日期时间工具
│   │   ├── JwtTokenUtil.java   # JWT工具
│   │   ├── PasswordUtil.java   # 密码工具
│   │   └── StringLocks.java    # 字符串锁工具
│   └── validation/             # 通用验证
└── src/main/resources/
    ├── application-default.properties    # 框架默认配置 (Properties格式)
    ├── application-framework.yml         # 框架默认配置 (YAML格式)
    ├── examples/                       # 配置示例
    │   ├── application-example.properties  # Properties格式示例
    │   └── application-example.yml         # YAML格式示例
    └── META-INF/
        └── spring.factories            # Spring Boot自动装配配置
```

## 主要功能模块

### 1. 通用工具类 (util)
- **DateTimeUtils**: 提供丰富的日期时间操作方法，包括格式化、计算、解析等功能。
- **JwtTokenUtil**: JWT令牌生成与验证工具。
- **PasswordUtil**: 基于Argon2算法的密码加密与验证工具。
- **StringLocks**: 提供基于字符串的并发锁机制。

### 2. 通用常量 (constant)
- **DateFormatConstant**: 定义常用的日期时间格式常量。

### 3. 通用配置 (config)
- **MyBatis配置**: 包括分页插件、性能分析等配置。
- **Swagger/Knife4j配置**: 自动生成API文档。
- **Web配置**: 包括跨域、消息转换器等配置。

### 4. 通用组件
- **异常处理**: 统一异常处理机制。
- **验证**: 通用验证注解和验证器。
- **上下文管理**: 用户上下文等管理。

### 5. 自动装配 (autoconfigure)
- **CommonFrameworkAutoConfiguration**: Spring Boot自动配置类，负责自动装配框架中的通用组件。
- **CommonFrameworkProperties**: 配置属性类，支持通过application.properties或application.yml进行配置。
- **spring.factories**: Spring Boot自动装配配置文件，声明自动配置类。

## 构建与运行

### 构建命令
```bash
mvn clean package
```

### 运行命令
```bash
mvn spring-boot:run
```

### 测试命令
```bash
mvn test
```

## 发布配置

项目已配置发布到私有Maven仓库：
- **Release仓库**: `http://192.168.1.181:18082/repository/maven-releases/`
- **Snapshot仓库**: `http://192.168.1.181:18082/repository/maven-snapshots/`

## 开发约定

1. **包名规范**: 所有框架内组件使用 `com.tigercub.commonframework` 包名。
2. **通用性原则**: 框架内组件应与具体业务逻辑解耦，确保通用性。
3. **配置灵活性**: 提供可配置的选项以适应不同项目的需求。
4. **向后兼容**: 升级时需保持向后兼容性。
5. **文档完善**: 为新添加的功能提供详细文档和使用示例。

## 使用方式

在需要使用此框架的项目中，在其 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.tigercub</groupId>
    <artifactId>finance-common-framework</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 配置选项

框架提供了丰富的配置选项，可在 `application.properties` 或 `application.yml` 中设置。完整的配置示例可以在 `src/main/resources/examples/` 目录中找到。

#### 通用配置
```properties
# 启用框架自动配置
framework.common.enabled=true
# 启用全局异常处理器
framework.common.enable-global-exception-handler=true
# 启用MyBatis相关配置
framework.common.enable-mybatis=true
# 启用Swagger相关配置
framework.common.enable-swagger=true
# 启用安全相关配置
framework.common.enable-security=true
# 启用缓存相关配置
framework.common.enable-cache=true
# 启用监控相关配置
framework.common.enable-monitoring=true
```

#### SQL日志配置
```properties
# 启用SQL执行日志
framework.common.sql-logger.enabled=true
# 是否显示参数替换后的SQL
framework.common.sql-logger.show-parameters=true
# SQL最大显示长度
framework.common.sql-logger.max-sql-length=1000
# 慢SQL阈值（毫秒）
framework.common.sql-logger.warn-threshold=500
```

#### 安全配置
```properties
# JWT密钥
framework.common.security.jwt.secret=yourSecretKey
# JWT过期时间（分钟）
framework.common.security.jwt.expiration=1440
# Argon2内存成本参数
framework.common.security.password.argon2.memory=65536
# Argon2时间成本参数
framework.common.security.password.argon2.time=2
```

#### 缓存配置
```properties
# 是否启用本地缓存
framework.common.cache.local-enabled=true
# 本地缓存过期时间（秒）
framework.common.cache.local-expire-after-write=3600
# 是否启用Redis缓存
framework.common.cache.redis-enabled=false
```

#### 监控配置
```properties
# 是否启用指标收集
framework.common.monitoring.metrics-enabled=true
# 是否启用健康检查
framework.common.monitoring.health-check-enabled=true
# 慢操作阈值（毫秒）
framework.common.monitoring.slow-operation-threshold=1000
```

#### Swagger配置
```properties
# 启用Swagger
framework.swagger.enabled=true
# 文档标题
framework.swagger.title=API文档
# 文档描述
framework.swagger.description=API接口文档
# 文档版本
framework.swagger.version=1.0.0
# 上下文路径
framework.swagger.context-path=http://localhost:8080
```

#### 配置文件示例
框架提供了两种格式的完整配置示例：
- `application-example.properties` - Properties格式示例
- `application-example.yml` - YAML格式示例

这些示例文件位于 `src/main/resources/examples/` 目录中，展示了所有可用的配置选项及其说明。

## 框架模块规划

根据 `FRAMEWORK_MODULE_PLAN.md` 文件，框架模块的内容界定如下：

### 应包含的组件
- 工具类（日期时间、密码加密、并发控制等）
- 常量定义
- 通用枚举（与业务无关的）
- 验证相关组件（注解、验证器）
- 通用过滤器（如MDC日志上下文过滤器）
- 上下文管理（用户上下文等）
- 通用DTO和转换器（与业务无关的）

### 不应包含的组件
- 业务相关的过滤器（如JWT认证过滤器，因为它依赖业务实体）
- 业务实体类
- 业务相关的配置类
- 与具体业务逻辑耦合的组件