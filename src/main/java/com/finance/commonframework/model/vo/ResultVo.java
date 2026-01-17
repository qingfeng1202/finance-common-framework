package com.finance.commonframework.model.vo;

import com.finance.commonframework.enums.ResultCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回对象
 * <p>
 * 用于封装API响应数据的统一格式
 * </p>
 *
 * @param <T> 数据类型
 * @author qingfeng
 * @since 2025/11/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultVo<T> {

    @Schema(description = "返回码，200为成功")
    private Integer code;

    @Schema(description = "错误信息")
    private String msg;

    @Schema(description = "返回数据")
    private T data;

    /**
     * 构建成功响应
     *
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ResultVo<T> buildSuccess() {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    /**
     * 构建成功响应
     *
     * @param <T>  数据类型
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> ResultVo<T> buildSuccess(T data) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    /**
     * 构建失败响应
     *
     * @param <T>       数据类型
     * @param resultEnum 错误码枚举
     * @return 失败响应对象
     */
    public static <T> ResultVo<T> fail(ResultCodeEnum resultEnum) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMessage());
        return result;
    }

    /**
     * 构建失败响应
     *
     * @param <T>        数据类型
     * @param resultEnum  错误码枚举
     * @param msg       自定义错误消息
     * @return 失败响应对象
     */
    public static <T> ResultVo<T> fail(ResultCodeEnum resultEnum, String msg) {
        return fail(resultEnum.getCode(), msg);
    }

    /**
     * 构建失败响应
     *
     * @param <T>  数据类型
     * @param code 错误码
     * @param msg  错误消息
     * @return 失败响应对象
     */
    public static <T> ResultVo<T> fail(Integer code, String msg) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 构建参数错误响应
     *
     * @param <T> 数据类型
     * @return 参数错误响应对象
     */
    public static <T> ResultVo<T> buildParamError() {
        return fail(ResultCodeEnum.PARAM_ERROR);
    }

    /**
     * 构建参数错误响应
     *
     * @param <T> 数据类型
     * @param msg 错误消息
     * @return 参数错误响应对象
     */
    public static <T> ResultVo<T> buildParamError(String msg) {
        return fail(ResultCodeEnum.PARAM_ERROR.getCode(), ResultCodeEnum.PARAM_ERROR.getMessage() + ": " + msg);
    }

    public boolean isSuccess() {
        return this.code.equals(ResultCodeEnum.SUCCESS.getCode());
    }

    public boolean isFail() {
        return !isSuccess();
    }

}
