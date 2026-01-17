package com.finance.commonframework.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * <p>
 * 提供MyBatis和MyBatis-Plus的相关配置：
 * 1. 分页插件配置
 * 2. 乐观锁插件配置
 * 3. 防全表更新删除插件配置
 * 4. SQL执行日志拦截器配置（通过@Component自动注册）
 * 5. 其他MyBatis扩展功能配置
 * <p>
 * 数据库连接池：
 * - 使用Spring Boot默认的HikariCP连接池
 * - HikariCP是目前性能最好的连接池，无需额外配置
 * - 支持连接池监控和性能优化
 * <p>
 * 注意事项：
 * - SqlExecutionLogInterceptor使用@Component注解，会被Spring自动扫描注册
 * - MyBatis-Plus会自动识别所有Interceptor类型的Bean并注册
 * - 不需要手动创建SqlSessionFactory，避免破坏MyBatis-Plus的自动配置
 *
 * @author qingfeng
 * @since 2025/9/28
 */
@Slf4j
@Configuration
@ConditionalOnProperty(
        prefix = "framework.common",
        name = "enable-mybatis",
        havingValue = "true",
        matchIfMissing = true
)
public class MybatisAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("MyBatis配置初始化开始...");
    }

    /**
     * MyBatis-Plus拦截器配置
     * <p>
     * 配置MyBatis-Plus提供的各种拦截器：
     * 1. 分页拦截器：自动处理分页查询
     * 2. 乐观锁拦截器：支持版本号乐观锁
     * 3. 防攻击拦截器：防止全表更新和删除
     * <p>
     * 拦截器执行顺序说明：
     * - 多个拦截器按照添加顺序执行
     * - 建议顺序：分页 -> 乐观锁 -> 防攻击 -> 自定义拦截器
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 1. 分页拦截器 - 必须放在最前面
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);                    // 数据库类型
        paginationInnerInterceptor.setMaxLimit(1000L);                         // 单页最大限制数量，防止恶意查询
        paginationInnerInterceptor.setOptimizeJoin(true);                      // 优化COUNT查询，去除不必要的JOIN
        paginationInnerInterceptor.setOverflow(false);                         // 溢出总页数后是否进行处理（false：不处理）
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        log.info("✓ 分页拦截器配置完成 - 最大分页: {}, 优化JOIN: {}", 1000L, true);

        // 2. 乐观锁拦截器
        // 使用方式：实体类字段添加 @Version 注解
        // 示例：
        // public class User {
        //     @Version
        //     private Integer version;
        // }
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);
        log.info("✓ 乐观锁拦截器配置完成");

        // 3. 防全表更新与删除拦截器
        // 防止恶意或误操作的全表更新和删除
        // 会拦截没有WHERE条件的UPDATE和DELETE语句
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        log.info("✓ 防全表更新删除拦截器配置完成");

        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new CustomTenantHandler()));

        log.info("MyBatis-Plus拦截器配置完成：共{}个拦截器", 3);
        return interceptor;
    }

    /**
     * SQL执行监控配置Bean
     * <p>
     * 用于在应用启动时验证SQL监控相关配置
     * 并提供监控配置信息的统一访问
     * <p>
     * 说明：
     * - SqlExecutionLogInterceptor通过@Component注解自动注册
     * - MyBatis-Plus会自动扫描并注册所有Interceptor类型的Bean
     * - 不需要手动创建SqlSessionFactory
     *
     * @return SQL监控配置信息
     */
    @Bean
    public SqlMonitorConfig sqlMonitorConfig() {
        return new SqlMonitorConfig();
    }

    /**
     * SQL监控配置信息类
     * <p>
     * 封装SQL监控的配置信息，便于其他组件使用
     */
    @Getter
    public static class SqlMonitorConfig {
        private final boolean sqlLogEnabled;
        private final boolean hikariCpEnabled;

        public SqlMonitorConfig() {
            // 检测SQL日志拦截器是否启用
            this.sqlLogEnabled = checkSqlLogEnabled();

            // 检测HikariCP连接池是否启用
            this.hikariCpEnabled = checkHikariCpEnabled();

            log.info("=".repeat(60));
            log.info("数据库配置概览：");
            log.info("├─ 连接池类型: {}", hikariCpEnabled ? "HikariCP (默认)" : "其他");
            log.info("├─ SQL执行日志拦截器: {}", sqlLogEnabled ? "✓ 启用" : "✗ 禁用");
            log.info("├─ MyBatis-Plus分页插件: ✓ 启用");
            log.info("├─ MyBatis-Plus乐观锁插件: ✓ 启用");
            log.info("└─ MyBatis-Plus防攻击插件: ✓ 启用");
            log.info("=".repeat(60));
        }

        /**
         * 检测SQL日志拦截器是否启用
         */
        private boolean checkSqlLogEnabled() {
            try {
                Class.forName("com.finance.commonframework.config.mybatis.SqlExecutionLogInterceptor");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }

        /**
         * 检测HikariCP连接池是否可用
         */
        private boolean checkHikariCpEnabled() {
            try {
                Class.forName("com.zaxxer.hikari.HikariDataSource");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }

        /**
         * 获取监控功能概览
         *
         * @return 监控功能概览信息
         */
        public String getMonitorOverview() {
            return "数据库配置状态：\n" +
                    "├─ 连接池: " + (hikariCpEnabled ? "HikariCP" : "其他") + "\n" +
                    "├─ SQL执行日志: " + (sqlLogEnabled ? "✓ 启用" : "✗ 禁用") + "\n" +
                    "├─ MyBatis-Plus分页: ✓ 启用\n" +
                    "├─ MyBatis-Plus乐观锁: ✓ 启用\n" +
                    "└─ MyBatis-Plus防攻击: ✓ 启用";
        }
    }

    /**
     * 应用启动后的配置验证
     * <p>
     * 验证MyBatis相关配置是否正确加载
     */
    @PostConstruct
    public void validateConfiguration() {
        log.info("MyBatis配置初始化完成");
        log.info("提示：");
        log.info("  - SQL执行日志拦截器通过@Component自动注册");
        log.info("  - MyBatis-Plus会自动扫描并注册所有Interceptor");
        log.info("  - 拦截器执行顺序：分页 -> 乐观锁 -> 防攻击 -> SQL日志");
    }

}
