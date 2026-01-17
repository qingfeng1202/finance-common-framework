package com.finance.commonframework.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger自动配置类
 * <p>
 * 提供Swagger和Knife4j的自动配置
 * </p>
 *
 * @author qingfeng
 * @since 2025/10/11
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(
        prefix = "framework.common",
        name = "enable-swagger",
        havingValue = "true",
        matchIfMissing = true
)
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI customOpenAPI(SwaggerProperties properties) {
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .version(properties.getVersion())
                        .description(properties.getDescription()))
                .servers(List.of(new Server()
                        .url(properties.getContextPath())
                        .description("Default Server URL")));
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All") // 分组名称（显示在 UI 中）
                .pathsToMatch("/**") // 匹配路径
                .build();
    }

}
