package com.finance.commonframework.exception;

import com.finance.commonframework.enums.ResultCodeEnum;

/**
 * 业务异常类 - 用于业务逻辑异常
 * <p>
 * 用于表示业务逻辑层面的异常情况
 * </p>
 *
 * @author qingfeng
 * @since 2026/01/17
 */
public class BusinessException extends BaseException {

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     *
     * @param resultCodeEnum 错误码枚举
     */
    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum);
    }

    /**
     * 构造函数
     *
     * @param resultCodeEnum 错误码枚举
     * @param customMessage  自定义错误消息
     */
    public BusinessException(ResultCodeEnum resultCodeEnum, String customMessage) {
        super(resultCodeEnum, customMessage);
    }

    /**
     * 快速创建业务异常
     */
    /**
     * 创建业务异常实例
     *
     * @param resultCodeEnum 错误码枚举
     * @return 业务异常实例
     */
    public static BusinessException of(ResultCodeEnum resultCodeEnum) {
        return new BusinessException(resultCodeEnum);
    }

    /**
     * 快速创建业务异常（自定义消息）
     */
    /**
     * 创建业务异常实例
     *
     * @param resultCodeEnum 错误码枚举
     * @param customMessage  自定义错误消息
     * @return 业务异常实例
     */
    public static BusinessException of(ResultCodeEnum resultCodeEnum, String customMessage) {
        return new BusinessException(resultCodeEnum, customMessage);
    }

}