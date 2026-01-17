package com.tigercub.commonframework.model.constant;

public interface InternalHeaderConstant {

    /**
     * 网关传递的用户信息请求头
     */
    String INTERNAL_USER_INFO_HEADER = "X-User-Info";

    /**
     * 内部服务标识请求头（用于验证是否来自网关）
     */
    String INTERNAL_TOKEN_HEADER = "X-Internal-Token";

    /**
     * 内部请求标记（单体架构下使用）
     */
    String INTERNAL_REQUEST_FLAG = "X-Internal-Request";

}
