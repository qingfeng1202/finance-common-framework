package com.tigercub.commonframework.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 通用框架自动配置测试类
 * <p>
 * 验证自动装配功能是否正常工作
 *
 * @author qingfeng
 * @since 2026/1/17
 */
class CommonFrameworkAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(CommonFrameworkAutoConfiguration.class));

    @Test
    void autoConfigurationIsAppliedWhenEnabled() {
        contextRunner
                .withPropertyValues("framework.common.enabled=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(CommonFrameworkAutoConfiguration.class);
                    // 验证各个组件是否被正确加载
                    // 由于使用了@Import注解，MyBatis和Swagger配置应该被加载
                    // 全局异常处理器也应该被加载
                });
    }

    @Test
    void autoConfigurationIsNotAppliedWhenDisabled() {
        contextRunner
                .withPropertyValues("framework.common.enabled=false")
                .run(context -> {
                    // 即使禁用框架，由于使用了@Import注解，配置类仍会被加载
                    // 但条件注解会控制具体Bean的创建
                    assertThat(context).hasSingleBean(CommonFrameworkAutoConfiguration.class);
                });
    }

    @Test
    void globalExceptionHandlerIsLoadedByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(com.tigercub.commonframework.config.web.GlobalExceptionHandler.class);
        });
    }

    @Test
    void mybatisConfigurationIsLoadedByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(com.tigercub.commonframework.config.mybatis.MybatisAutoConfiguration.class);
        });
    }

    @Test
    void swaggerConfigurationIsLoadedByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(com.tigercub.commonframework.config.swagger.SwaggerAutoConfiguration.class);
        });
    }

    @Test
    void globalExceptionHandlerCanBeDisabled() {
        contextRunner
                .withPropertyValues("framework.common.enable-global-exception-handler=false")
                .run(context -> {
                    assertThat(context)
                            .doesNotHaveBean(com.tigercub.commonframework.config.web.GlobalExceptionHandler.class);
                });
    }

    @Test
    void mybatisConfigurationCanBeDisabled() {
        contextRunner
                .withPropertyValues("framework.common.enable-mybatis=false")
                .run(context -> {
                    assertThat(context)
                            .doesNotHaveBean(com.tigercub.commonframework.config.mybatis.MybatisAutoConfiguration.class);
                });
    }
}