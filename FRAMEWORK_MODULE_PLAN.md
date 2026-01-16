# Finance Common Framework 框架模块创建建议

## 1. 框架模块内容界定

建议将以下真正通用的组件迁移到框架模块：
- 工具类（日期时间、密码加密、并发控制等）
- 常量定义
- 通用枚举（与业务无关的）
- 验证相关组件（注解、验证器）
- 通用过滤器（如MDC日志上下文过滤器）
- 上下文管理（用户上下文等）
- 通用DTO和转换器（与业务无关的）

**不应迁移到框架模块的组件：**
- 业务相关的过滤器（如JWT认证过滤器，因为它依赖业务实体）
- 业务实体类
- 业务相关的配置类
- 与具体业务逻辑耦合的组件

## 2. 框架模块结构

```
finance-common-framework/
├── pom.xml
├── README.md
└── src/main/java/com/finance/common/
    ├── auth/                 # 通用认证工具（不含业务逻辑）
    ├── context/              # 上下文管理
    ├── constant/             # 通用常量
    ├── dto/                  # 通用DTO
    │   ├── convert/          # DTO转换器
    │   └── context/          # 上下文DTO
    ├── enums/                # 通用枚举
    ├── exception/            # 通用异常
    ├── filter/               # 通用过滤器
    ├── interceptor/          # 通用拦截器
    ├── util/                 # 通用工具类
    │   ├── crypto/           # 加密工具
    │   ├── datetime/         # 日期时间工具
    │   └── concurrent/       # 并发工具
    ├── validation/           # 通用验证
    │   ├── annotation/       # 验证注解
    │   └── validator/        # 验证器
    └── config/               # 通用配置
```

## 3. 实施步骤

1. **创建框架模块项目**：创建独立的Maven项目
2. **迁移通用组件**：将上述界定的通用组件迁移到框架模块
3. **更新包名**：确保框架模块中的类使用新的包名
4. **发布框架模块**：将框架模块打包并发布到Maven仓库
5. **更新原项目**：在原项目的pom.xml中添加对框架模块的依赖
6. **更新导入语句**：将原项目中对通用组件的引用指向框架模块

## 4. 注意事项

- 确保框架模块中的组件与具体业务逻辑解耦
- 灵活配置：提供可配置的选项以适应不同项目的需求
- 向后兼容：在升级框架模块时保持向后兼容性
- 文档完善：提供详细的使用文档和示例

## 5. 原项目依赖更新

在原项目的pom.xml中添加依赖：
```xml
<dependency>
    <groupId>com.finance</groupId>
    <artifactId>finance-common-framework</artifactId>
    <version>1.0.0</version>
</dependency>
```

然后更新原项目中的导入语句，将对通用组件的引用指向框架模块。

这样的架构可以提高代码复用性，降低项目间的耦合度，并便于统一维护和升级通用功能。