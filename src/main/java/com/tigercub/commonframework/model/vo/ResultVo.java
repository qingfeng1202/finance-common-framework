package com.tigercub.commonframework.model.vo;

import com.tigercub.commonframework.model.enums.ResultCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 统一返回对象
 * </p>
 *
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

    public static <T> ResultVo<T> buildSuccess() {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        return result;
    }

    public static <T> ResultVo<T> buildSuccess(T data) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    public static <T> ResultVo<T> fail(ResultCodeEnum resultEnum) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMessage());
        return result;
    }

    public static <T> ResultVo<T> fail(ResultCodeEnum resultEnum, String msg) {
        return fail(resultEnum.getCode(), msg);
    }

    public static <T> ResultVo<T> fail(Integer code, String msg) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> ResultVo<T> buildParamError() {
        return fail(ResultCodeEnum.PARAM_ERROR);
    }

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
