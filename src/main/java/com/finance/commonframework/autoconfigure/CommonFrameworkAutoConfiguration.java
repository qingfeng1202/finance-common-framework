package com.finance.commonframework.autoconfigure;

import com.finance.commonframework.config.mybatis.MybatisAutoConfiguration;
import com.finance.commonframework.config.swagger.SwaggerAutoConfiguration;
import com.finance.commonframework.config.web.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 通用框架自动配置类
 * <p>
 * 该配置类负责自动装配框架中的通用组件，包括：
 * 1. MyBatis配置
 * 2. Swagger配置
 * 3. 全局异常处理器
 * <p>
 * 使用条件注解确保只有在满足特定条件时才进行自动装配
 *
 * @author qingfeng
 * @since 2026/1/17
 */
@Configuration
@EnableConfigurationProperties(CommonFrameworkProperties.class)
public class CommonFrameworkAutoConfiguration {

    /**
     * 自动配置全局异常处理器
     * <p>
     * 仅当DispatcherServlet类存在且容器中不存在相同类型的Bean时才创建
     *
     * @return 全局异常处理器实例
     */
    @Bean
    @ConditionalOnClass(DispatcherServlet.class)
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    @ConditionalOnProperty(
            prefix = "framework.common",
            name = "enable-global-exception-handler",
            havingValue = "true",
            matchIfMissing = true
    )
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 自动配置MyBatis相关组件
     * <p>
     * 仅当MyBatis Plus相关类存在时才进行配置
     * 注意：MybatisAutoConfiguration类本身带有条件注解，控制其是否生效
     *
     * @return MyBatis自动配置实例
     */
    @Bean
    @ConditionalOnClass(name = "com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor")
    @ConditionalOnMissingBean(MybatisAutoConfiguration.class)
    public MybatisAutoConfiguration mybatisAutoConfiguration() {
        return new MybatisAutoConfiguration();
    }

    /**
     * 自动配置Swagger相关组件
     * <p>
     * 仅当Swagger相关类存在时才进行配置
     * 注意：SwaggerAutoConfiguration类本身带有条件注解，控制其是否生效
     *
     * @return Swagger自动配置实例
     */
    @Bean
    @ConditionalOnClass(name = {"io.swagger.v3.oas.models.OpenAPI", "org.springdoc.core.models.GroupedOpenApi"})
    @ConditionalOnMissingBean(SwaggerAutoConfiguration.class)
    public SwaggerAutoConfiguration swaggerAutoConfiguration() {
        return new SwaggerAutoConfiguration();
    }
}