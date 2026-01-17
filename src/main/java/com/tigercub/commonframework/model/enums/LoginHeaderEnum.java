package com.tigercub.commonframework.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * LoginHeaderEnum
 * </p>
 *
 * @author qingfeng
 * @since 2025/12/2
 */
@AllArgsConstructor
@Getter
public enum LoginHeaderEnum {

    AUTHORIZATION("Authorization"),
    TOKEN("token"),
    TRACE_ID("X-Trace-Id"),
    ;

    private final String code;

}
