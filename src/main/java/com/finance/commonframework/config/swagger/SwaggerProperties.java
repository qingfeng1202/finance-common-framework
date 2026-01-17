package com.finance.commonframework.config.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * SwaggerProperties
 * </p>
 *
 * @author qingfeng
 * @since 2025/10/11
 */
@ConfigurationProperties(prefix = "framework.swagger")
@Data
public class SwaggerProperties {

    private String title = "API文档";
    private String description;
    private String version = "1.0.0";
    private String contextPath;

}
