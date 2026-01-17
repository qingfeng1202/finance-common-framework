package com.finance.commonframework.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 分页返回对象
 * <p>
 * 用于封装分页数据的统一返回格式
 * </p>
 *
 * @param <T> 数据类型
 * @author qingfeng
 * @since 2025/11/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageVo<T> {

    @Schema(description = "数据总条数")
    private Long total;

    @Schema(description = "数据集合")
    private List<T> records;

    /**
     * 创建一个PageVo对象
     *
     * @param total 数据总条数
     * @param data  数据集合
     * @param <T>   数据类型
     * @return PageVo对象
     */
    public static <T> PageVo<T> of(Long total, List<T> data) {
        return PageVo.<T>builder()
                .total(total)
                .records(data)
                .build();
    }

    /**
     * 创建一个空的PageVo对象
     *
     * @param total 数据总条数
     * @param <T>   数据类型
     * @return PageVo对象
     */
    public static <T> PageVo<T> empty(long total) {
        return PageVo.<T>builder()
                .total(total)
                .records(Collections.emptyList())
                .build();
    }

}
