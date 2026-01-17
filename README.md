# 🚀 finance-common-framework

> 财务系统通用框架 - 提供基础配置、工具类、异常处理等通用功能

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## 📦 功能特性

### 核心功能

- ✅ **MyBatis-Plus 配置**
    - 分页插件（支持 MySQL、PostgreSQL 等）
    - 乐观锁插件
    - 防全表更新/删除插件
    - 多租户支持
    - SQL 执行日志拦截器（支持慢SQL告警）

- ✅ **Swagger/OpenAPI 文档**
    - Swagger UI 自动配置
    - 接口文档自动生成
    - 支持自定义配置

- ✅ **全局异常处理**
    - 统一异常处理
    - 标准错误响应格式
    - 参数校验异常捕获
    - 业务异常封装

- ✅ **通用工具类**
    - 日期时间工具（DateTimeUtils）
    - JWT Token 工具
    - 密码加密工具
    - 字符串锁工具

- ✅ **数据模型**
    - 统一响应对象（ResultVo）
    - 分页对象（PageVo）
    - 枚举校验注解（@EnumValue）

- ✅ **常量和枚举**
    - 日期格式常量
    - 请求头常量
    - MDC 常量
    - 业务结果码枚举
    - 状态枚举

---

## 🔧 快速开始

### 1. 添加依赖

在您的项目 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>com.tigercub</groupId>
    <artifactId>finance-common-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 启用框架配置

在 `application.yml` 中引入框架配置：

```yaml
spring:
  profiles:
    include:
      - framework  # 启用框架默认配置
```

### 3. 开始使用

框架会自动装配所有配置，无需额外设置！

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // ✅ 自动注入 MyBatis Mapper
    @Autowired
    private UserMapper userMapper;
    
    // ✅ 使用统一响应对象
    @GetMapping("/{id}")
    public ResultVo<User> getUser(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        return ResultVo.buildSuccess(user);
    }
    
    // ✅ 抛出业务异常会自动处理
    @PostMapping
    public ResultVo<Void> createUser(@RequestBody @Valid UserRequest request) {
        if (userMapper.existsByUsername(request.getUsername())) {
            throw new BusinessException(ResultCodeEnum.LOGIN_ACCOUNT_EXISTS);
        }
        // 业务逻辑... 
        return ResultVo.buildSuccess();
    }
}
```

---

## 📖 详细配置

### MyBatis-Plus 配置

```yaml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true  # 驼峰命名转换
    log-impl:  org.apache.ibatis.logging.slf4j.Slf4jImpl
  global-config:
    db-config:
      logic-delete-field: deleted       # 逻辑删除字段
      logic-delete-value: 1
      logic-not-delete-value: 0
      id-type: ASSIGN_ID                # 雪花算法ID
```

### SQL 日志配置

```yaml
mybatis:
  sql-logger: 
    enabled: true                       # 启用SQL日志
    show-parameters: true               # 显示参数
    max-sql-length: 1000               # 最大长度
    warn-threshold: 500                # 慢SQL阈值(ms)
```

### Swagger 配置

```yaml
springdoc:
  api-docs: 
    enabled: true                       # 启用API文档
    path: /v3/api-docs
  swagger-ui:
    enabled: true                       # 启用Swagger UI
    path: /swagger-ui.html
```

访问 Swagger UI：`http://localhost:8080/swagger-ui.html`

---

## 🎯 使用示例

### 1. 统一响应对象

```java
// 成功响应
return ResultVo.buildSuccess();
return ResultVo.buildSuccess(data);

// 失败响应
return ResultVo.fail(ResultCodeEnum. PARAM_ERROR);
return ResultVo.fail(ResultCodeEnum. PARAM_ERROR, "用户名不能为空");
```

### 2. 异常处理

```java
// 抛出业务异常
throw new BusinessException(ResultCodeEnum. USER_NOT_EXISTS);
throw new BusinessException(9999, "自定义错误信息");

// 框架会自动捕获并返回统一格式
{
  "code": 11003,
  "msg": "用户不存在",
  "data": null
}
```

### 3. 枚举校验

```java
public class UserRequest {
    
    // 校验枚举值
    @EnumValue(enumClass = StatusEnum.class, message = "状态值不合法")
    private Integer status;
    
    // 只允许特定枚举值
    @EnumValue(
        enumClass = StatusEnum.class,
        allowValues = {"NORMAL"},
        message = "只能设置为正常状态"
    )
    private Integer status;
}
```

### 4. 日期时间工具

```java
// 获取当前日期
String date = DateTimeUtils.getCurrentDate(); // "2026-01-17"

// 日期计算
Date future = DateTimeUtils.addDays(new Date(), 7);
Date past = DateTimeUtils. addMonths(new Date(), -1);

// 日期区间
Date yearStart = DateTimeUtils.getYearStartTime(0);
Date yearEnd = DateTimeUtils.getYearEndTime(0);
```

---

## 📂 项目结构

```
src/main/java/com/tigercub/commonframework/
├── config/                  # 配置类
│   ├── mybatis/            # MyBatis 配置
│   ├── swagger/            # Swagger 配置
│   └── web/                # Web 配置（全局异常处理）
├── constant/               # 常量类
├── context/                # 上下文
├── enums/                  # 枚举类
├── exception/              # 异常类
├── filter/                 # 过滤器
├── model/                  # 数据模型
│   ├── dto/               # 数据传输对象
│   ├── request/           # 请求对象
│   └── vo/                # 视图对象
├── util/                   # 工具类
└── validation/             # 参数校验
    ├── annotation/        # 校验注解
    └── validator/         # 校验器

src/main/resources/
└── META-INF/spring/
    └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

---

## 🔗 依赖说明

### 核心依赖

- Spring Boot 3.x
- MyBatis-Plus 3.5.x
- Swagger (SpringDoc OpenAPI)
- Lombok
- JWT (jjwt)

### 可选依赖

- HikariCP（数据库连接池，Spring Boot 默认）
- Jakarta Validation（参数校验）

---

## 📝 更新日志

### v1.0.0 (2026-01-17)

- ✅ 初始版本发布
- ✅ MyBatis-Plus 自动配置
- ✅ Swagger 自动配置
- ✅ 全局异常处理
- ✅ 通用工具类
- ✅ 枚举校验注解

---

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 📧 联系方式

- 作者：qingfeng
- Email：844539509@qq.com
- GitHub：[@qingfeng1202](https://github.com/qingfeng1202)

---

## ⭐ Star History

如果这个项目对您有帮助，请给一个 Star ⭐ 支持一下！