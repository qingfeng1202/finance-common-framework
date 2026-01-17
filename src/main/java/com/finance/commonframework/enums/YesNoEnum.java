package com.finance.commonframework.enums;

import com.finance.commonframework.validation.EnumCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * YesNoEnum
 * </p>
 *
 * @author qingfeng
 * @since 2025/12/11
 */
@AllArgsConstructor
@Getter
public enum YesNoEnum implements EnumCode<Integer> {

    /**
     * 否
     */
    NO(0, "否"),

    /**
     * 是
     */
    YES(1, "是");

    private final Integer code;

    /**
     * 描述
     */
    private final String description;

}
