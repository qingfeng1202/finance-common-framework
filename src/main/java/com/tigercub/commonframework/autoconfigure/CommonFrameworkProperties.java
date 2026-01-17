package com.tigercub.commonframework.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 通用框架配置属性
 * <p>
 * 定义框架的通用配置选项，可以通过application.properties或application.yml进行配置
 *
 * @author qingfeng
 * @since 2026/1/17
 */
@ConfigurationProperties(prefix = "framework.common")
@Data
public class CommonFrameworkProperties {

    /**
     * 是否启用框架自动配置
     */
    private boolean enabled = true;

    /**
     * 是否启用全局异常处理器
     */
    private boolean enableGlobalExceptionHandler = true;

    /**
     * 是否启用MyBatis相关配置
     */
    private boolean enableMybatis = true;

    /**
     * 是否启用Swagger相关配置
     */
    private boolean enableSwagger = true;

    /**
     * 是否启用安全相关配置
     */
    private boolean enableSecurity = true;

    /**
     * 是否启用缓存相关配置
     */
    private boolean enableCache = true;

    /**
     * 是否启用监控相关配置
     */
    private boolean enableMonitoring = true;

    /**
     * SQL日志配置
     */
    private SqlLogger sqlLogger = new SqlLogger();

    /**
     * 安全配置
     */
    private Security security = new Security();

    /**
     * 缓存配置
     */
    private Cache cache = new Cache();

    /**
     * 监控配置
     */
    private Monitoring monitoring = new Monitoring();

    /**
     * SQL日志配置
     */
    @Data
    public static class SqlLogger {
        /**
         * 是否启用SQL日志
         */
        private boolean enabled = true;

        /**
         * 是否显示参数替换后的SQL
         */
        private boolean showParameters = true;

        /**
         * SQL最大显示长度
         */
        private int maxSqlLength = 1000;

        /**
         * 慢SQL阈值（毫秒）
         */
        private long warnThreshold = 500;
    }

    /**
     * 安全配置
     */
    @Data
    public static class Security {
        /**
         * JWT配置
         */
        private Jwt jwt = new Jwt();

        /**
         * 密码加密配置
         */
        private Password password = new Password();
    }

    /**
     * JWT配置
     */
    @Data
    public static class Jwt {
        /**
         * JWT密钥
         */
        private String secret = "defaultSecretKey";

        /**
         * JWT过期时间（分钟）
         */
        private long expiration = 60 * 24; // 默认24小时

        /**
         * JWT刷新时间（分钟）
         */
        private long refreshBefore = 60 * 2; // 提前2小时刷新
    }

    /**
     * 密码加密配置
     */
    @Data
    public static class Password {
        /**
         * Argon2参数配置
         */
        private Argon2 argon2 = new Argon2();
    }

    /**
     * Argon2配置
     */
    @Data
    public static class Argon2 {
        /**
         * 内存成本参数
         */
        private int memory = 1 << 16; // 64 MiB

        /**
         * 时间成本参数
         */
        private int time = 2;

        /**
         * 并行度参数
         */
        private int threads = 1;
    }

    /**
     * 缓存配置
     */
    @Data
    public static class Cache {
        /**
         * 是否启用本地缓存
         */
        private boolean localEnabled = true;

        /**
         * 本地缓存过期时间（秒）
         */
        private long localExpireAfterWrite = 3600;

        /**
         * 是否启用Redis缓存
         */
        private boolean redisEnabled = false;

        /**
         * Redis缓存过期时间（秒）
         */
        private long redisExpireAfterWrite = 3600;
    }

    /**
     * 监控配置
     */
    @Data
    public static class Monitoring {
        /**
         * 是否启用指标收集
         */
        private boolean metricsEnabled = true;

        /**
         * 是否启用健康检查
         */
        private boolean healthCheckEnabled = true;

        /**
         * 是否启用链路追踪
         */
        private boolean traceEnabled = false;

        /**
         * 慢操作阈值（毫秒）
         */
        private long slowOperationThreshold = 1000;
    }
}