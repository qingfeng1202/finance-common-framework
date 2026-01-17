package com.tigercub.commonframework.exception;

import com.tigercub.commonframework.model.enums.ResultCodeEnum;

/**
 * <p>
 * 业务异常类 - 用于业务逻辑异常
 * </p>
 *
 * @author qingfeng
 * @since 2026/01/17
 */
public class BusinessException extends BaseException {

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum);
    }

    public BusinessException(ResultCodeEnum resultCodeEnum, String customMessage) {
        super(resultCodeEnum, customMessage);
    }

    /**
     * 快速创建业务异常
     */
    public static BusinessException of(ResultCodeEnum resultCodeEnum) {
        return new BusinessException(resultCodeEnum);
    }

    /**
     * 快速创建业务异常（自定义消息）
     */
    public static BusinessException of(ResultCodeEnum resultCodeEnum, String customMessage) {
        return new BusinessException(resultCodeEnum, customMessage);
    }

}