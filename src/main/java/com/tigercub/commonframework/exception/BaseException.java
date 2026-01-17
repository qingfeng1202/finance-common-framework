package com.tigercub.commonframework.exception;

import lombok.Getter;

/**
 * <p>
 * 基础异常类
 * </p>
 *
 * @author qingfeng
 * @since 2026/01/17
 */
@Getter
public class BaseException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

}