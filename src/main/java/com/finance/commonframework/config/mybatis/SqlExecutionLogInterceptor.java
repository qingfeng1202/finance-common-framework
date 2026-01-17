package com.finance.commonframework.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * SQL执行日志拦截器
 * <p>
 * 专门用于在日志中打印SQL执行时间和完整SQL语句，便于开发调试和性能分析。
 * <p>
 * 与HikariCP的配合：
 * - HikariCP：提供高性能的数据库连接池管理
 * - 本拦截器：提供详细的SQL执行日志和性能监控
 * <p>
 * 功能特性：
 * 1. 打印完整的SQL执行时间（毫秒级精度）
 * 2. 显示参数替换后的完整SQL语句
 * 3. 支持多种参数类型的格式化（包括日期、集合、数组等）
 * 4. 可配置是否启用
 * 5. 轻量级设计，性能开销最小
 * 6. 支持慢SQL自动告警（超过阈值使用WARN级别）
 * 配置说明：
 * mybatis:
 * sql-logger:
 * enabled: true              # 是否启用SQL日志（默认true）
 * show-parameters: true      # 是否显示参数替换后的SQL（默认true）
 * max-sql-length: 1000      # SQL最大显示长度（默认1000）
 * warn-threshold: 500       # 慢SQL阈值，单位毫秒（默认500ms）
 * <p>
 *
 * @author qingfeng
 * @since 2025/9/28
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class SqlExecutionLogInterceptor implements Interceptor {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Value("${mybatis.sql-logger.enabled:true}")
    private boolean enabled;

    @Value("${mybatis.sql-logger.show-parameters:true}")
    private boolean showParameters;

    @Value("${mybatis.sql-logger.max-sql-length:1000}")
    private int maxSqlLength;

    @Value("${mybatis.sql-logger.warn-threshold:500}")
    private long warnThreshold;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!enabled) {
            return invocation.proceed();
        }

        long startTime = System.currentTimeMillis();

        try {
            return invocation.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            try {
                logSqlExecution(invocation, executionTime);
            } catch (Exception e) {
                log.warn("SQL日志记录失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 记录SQL执行日志
     */
    private void logSqlExecution(Invocation invocation, long executionTime) {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();

        String sql = showParameters ?
                getFormattedSql(boundSql, statementHandler) :
                boundSql.getSql().replaceAll("\\s+", " ").trim();

        // 截断过长的SQL
        if (sql.length() > maxSqlLength) {
            sql = sql.substring(0, maxSqlLength) + "... [truncated]";
        }

        // 根据执行时间选择日志级别
        if (executionTime >= warnThreshold) {
            log.warn("慢SQL警告 | 耗时: {}ms | SQL: {}", executionTime, sql);
        } else {
            log.info("SQL执行 | 耗时: {}ms | SQL: {}", executionTime, sql);
        }
    }

    /**
     * 获取格式化后的SQL（参数已替换）
     */
    private String getFormattedSql(BoundSql boundSql, StatementHandler statementHandler) {
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ").trim();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        if (parameterMappings == null || parameterMappings.isEmpty() || parameterObject == null) {
            return sql;
        }

        try {
            Configuration configuration = getConfiguration(statementHandler);
            if (configuration == null) {
                return sql;
            }

            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = configuration.newMetaObject(parameterObject);

            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                Object value = null;

                if (boundSql.hasAdditionalParameter(propertyName)) {
                    value = boundSql.getAdditionalParameter(propertyName);
                } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    value = parameterObject;
                } else if (metaObject.hasGetter(propertyName)) {
                    value = metaObject.getValue(propertyName);
                }

                sql = sql.replaceFirst("\\?", formatParameterValue(value));
            }
        } catch (Exception e) {
            log.debug("SQL参数格式化失败: {}", e.getMessage());
        }

        return sql;
    }

    /**
     * 获取MyBatis配置对象
     */
    private Configuration getConfiguration(StatementHandler statementHandler) {
        try {
            MetaObject metaObject = org.apache.ibatis.reflection.SystemMetaObject.forObject(statementHandler);
            Object delegate = metaObject.getValue("delegate");
            if (delegate != null) {
                MetaObject delegateMetaObject = org.apache.ibatis.reflection.SystemMetaObject.forObject(delegate);
                return (Configuration) delegateMetaObject.getValue("configuration");
            }
        } catch (Exception e) {
            log.debug("获取Configuration失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 格式化参数值
     */
    private String formatParameterValue(Object value) {
        switch (value) {
            case null -> {
                return "NULL";
            }
            case String s -> {
                return "'" + s + "'";
            }
            case Date date -> {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                return "'" + sdf.format(date) + "'";
            }
            case Collection<?> collection -> {
                return formatCollection(collection);
            }
            default -> {
            }
        }

        if (value.getClass().isArray()) {
            return formatArray(value);
        }

        return String.valueOf(value);
    }

    /**
     * 格式化集合参数
     */
    private String formatCollection(Collection<?> collection) {
        StringJoiner joiner = new StringJoiner(", ", "(", ")");
        for (Object item : collection) {
            joiner.add(formatParameterValue(item));
        }
        return joiner.toString();
    }

    /**
     * 格式化数组参数
     */
    private String formatArray(Object array) {
        if (array instanceof Object[]) {
            return formatCollection(Arrays.asList((Object[]) array));
        }
        return String.valueOf(array);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以通过Properties设置配置项
        if (properties != null) {
            String enabledProp = properties.getProperty("enabled");
            if (enabledProp != null) {
                this.enabled = Boolean.parseBoolean(enabledProp);
            }

            String warnThresholdProp = properties.getProperty("warnThreshold");
            if (warnThresholdProp != null) {
                this.warnThreshold = Long.parseLong(warnThresholdProp);
            }
        }
    }
}