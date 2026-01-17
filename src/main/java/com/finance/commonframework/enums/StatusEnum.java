package com.finance.commonframework.enums;

import com.finance.commonframework.validation.EnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * StatusEnum
 * </p>
 *
 * @author qingfeng
 * @since 2025/11/29
 */
@AllArgsConstructor
@Getter
public enum StatusEnum implements EnumCode<Integer> {

    /**
     * 正常
     */
    NORMAL(1),
    /**
     * 禁用
     */
    DISABLED(0),
    ;

    private final Integer code;

}
