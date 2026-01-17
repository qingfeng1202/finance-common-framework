package com.tigercub.commonframework.exception;

import com.tigercub.commonframework.model.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 基础异常类
 * <p>
 * 所有自定义异常的基类
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

    public BaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public BaseException(ResultCodeEnum resultCodeEnum, String customMessage) {
        super(customMessage);
        this.code = resultCodeEnum.getCode();
        this.message = customMessage;
    }

    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

}