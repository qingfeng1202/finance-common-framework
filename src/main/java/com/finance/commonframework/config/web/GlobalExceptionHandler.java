package com.finance.commonframework.config.web;

import com.finance.commonframework.exception.BaseException;
import com.finance.commonframework.exception.BusinessException;
import com.finance.commonframework.enums.ResultCodeEnum;
import com.finance.commonframework.model.vo.ResultVo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * <p>
 * 统一处理各类异常，返回标准格式的错误响应
 * </p>
 *
 * @author qingfeng
 * @since 2026/1/17
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 统一响应结果
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultVo<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResultVo.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理基础异常
     *
     * @param e 基础异常
     * @return 统一响应结果
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultVo<?> handleBaseException(BaseException e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResultVo.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（@Valid）
     *
     * @param e 方法参数无效异常
     * @return 统一响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultVo<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", errorMsg);
        return ResultVo.buildParamError(errorMsg);
    }

    /**
     * 处理绑定异常
     *
     * @param e 绑定异常
     * @return 统一响应结果
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultVo<?> handleBindException(BindException e) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数绑定失败: {}", errorMsg);
        return ResultVo.buildParamError(errorMsg);
    }

    /**
     * 处理约束违反异常（@Validated）
     *
     * @param e 约束违反异常
     * @return 统一响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultVo<?> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数约束违反: {}", errorMsg);
        return ResultVo.buildParamError(errorMsg);
    }

    /**
     * 处理非法参数异常
     *
     * @param e 非法参数异常
     * @return 统一响应结果
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultVo<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数: {}", e.getMessage());
        return ResultVo.buildParamError(e.getMessage());
    }

    /**
     * 处理空指针异常
     *
     * @param e 空指针异常
     * @return 统一响应结果
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVo<?> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return ResultVo.fail(ResultCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 处理其他未知异常
     *
     * @param e 异常
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVo<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResultVo.fail(ResultCodeEnum.SYSTEM_ERROR);
    }

}