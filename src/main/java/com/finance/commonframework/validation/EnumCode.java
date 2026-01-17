package com.finance.commonframework.validation;

/**
 * 统一枚举接口
 * <p>
 * 所有业务枚举实现该接口，便于统一处理（数据库存储、前端展示等）
 * </p>
 *
 * @param <T> 枚举编码类型
 * @author qingfeng
 * @since 2025/11/25
 */
public interface EnumCode<T> {

    /**
     * 获取枚举的code值
     *
     * @return code值
     */
    T getCode();

}
