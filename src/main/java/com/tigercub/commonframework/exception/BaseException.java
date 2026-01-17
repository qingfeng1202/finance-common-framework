package com.tigercub.commonframework.exception;

import com.tigercub.commonframework.enums.ResultCodeEnum;
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

    /**
     * 构造基础异常
     *
     * @param code 错误码
     * @param message 错误信息
     */
    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 根据结果码枚举构造基础异常
     *
     * @param resultCodeEnum 结果码枚举
     */
    public BaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    /**
     * 根据结果码枚举和自定义消息构造基础异常
     *
     * @param resultCodeEnum 结果码枚举
     * @param message 自定义错误信息
     */
    public BaseException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.code = resultCodeEnum.getCode();
        this.message = message;
    }

}