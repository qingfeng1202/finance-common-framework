package com.finance.commonframework.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分页请求对象
 * <p>
 * 用于接收分页查询参数的请求
 * </p>
 *
 * @author qingfeng
 * @since 2025/11/29
 */
@Data
public class PageRequest {

    @Schema(description = "当前页码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "当前页码不能为空")
    @Min(value = 1, message = "当前页码不能小于1")
    private Integer page = 1;

    @Schema(description = "每页数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量不能小于1")
    private Integer size = 10;

}
