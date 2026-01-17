package com.finance.commonframework.autoconfigure;

import com.finance.commonframework.config.mybatis.MybatisAutoConfiguration;
import com.finance.commonframework.config.swagger.SwaggerAutoConfiguration;
import com.finance.commonframework.config.web.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

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
                });
    }

    @Test
    void globalExceptionHandlerIsLoadedByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
        });
    }

    @Test
    void mybatisConfigurationIsLoadedByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MybatisAutoConfiguration.class);
        });
    }

    @Test
    void swaggerConfigurationIsLoadedByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(SwaggerAutoConfiguration.class);
        });
    }

    @Test
    void globalExceptionHandlerCanBeDisabled() {
        contextRunner
                .withPropertyValues("framework.common.enable-global-exception-handler=false")
                .run(context -> {
                    assertThat(context)
                            .doesNotHaveBean(GlobalExceptionHandler.class);
                });
    }

    @Test
    void mybatisConfigurationCanBeDisabled() {
        contextRunner
                .withPropertyValues("framework.common.enable-mybatis=false")
                .run(context -> {
                    // 当禁用mybatis配置时，不应该有MybatisAutoConfiguration Bean
                    // 因为我们为MybatisAutoConfiguration类添加了条件注解
                    assertThat(context)
                            .doesNotHaveBean(MybatisAutoConfiguration.class);
                });
    }
}