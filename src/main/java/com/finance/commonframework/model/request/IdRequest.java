package com.finance.commonframework.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ID请求对象
 * <p>
 * 用于接收单个ID参数的请求
 * </p>
 *
 * @author qingfeng
 * @since 2025/11/29
 */
@Data
public class IdRequest {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID不能为空")
    private Integer id;

}
