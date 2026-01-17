package com.finance.commonframework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 统一响应结果状态码枚举
 * </p>
 *
 * @author qingfeng
 * @since 2025/11/25
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {


    /* ------------------------ 成功 ------------------------ */
    SUCCESS(200, "操作成功"),

    /* ------------------------ 客户端错误 ------------------------ */
    PARAM_ERROR(9529, "参数错误"),
    NO_PERMISSION(9531, "无权限访问"),
    REQUEST_TOO_FREQUENT(9530, "请求过于频繁，请稍后再试"),

    /* ------------------------ 服务端错误 ------------------------ */
    SYSTEM_ERROR(9527, "系统异常，请稍后重试"),
    TRY_AGAIN(9528, "请求超时，请重试"),

    // ==================== 验证码相关 100xx ====================
    CAPTCHA_ERROR(10001, "验证码错误"),
    CAPTCHA_EXPIRED(10002, "验证码已过期"),
    CAPTCHA_USED(10003, "验证码已使用"),
    CAPTCHA_NOT_FOUND(10004, "验证码不存在"),
    CAPTCHA_SCENE_MISMATCH(10005, "验证码场景不匹配"),
    CAPTCHA_GENERATE_FAILED(10006, "验证码生成失败，请稍后重试"),

    /*------------------------auth 登录 101xx------------------------*/
    LOGIN_USERNAME_OR_PASSWORD_ERROR(10101, "用户名或密码错误"),
    LOGIN_ACCOUNT_LOCKED(10102, "账号已被锁定，请稍后再试"),
    LOGIN_ATTEMPTS_EXCEEDED(10103, "登录失败次数过多，请稍后再试"),

//    LOGIN_USER_NOT_EXIST(10104, "用户不存在"),

    /* ------------------------ 业务错误 - 用户模块 ------------------------ */
    USER_CODE_EXISTS(11001, "用户编码已存在"),
    LOGIN_ACCOUNT_EXISTS(11002, "登录账号已存在"),
    USER_NOT_EXISTS(11003, "用户不存在"),
    ORG_NOT_EXISTS(11004, "组织不存在"),
    ROLE_NOT_EXISTS(11005, "角色不存在"),

    /* ------------------------ 业务错误 - 角色模块 ------------------------ */
    ROLE_CODE_EXISTS(11101, "角色编码已存在"),
    ROLE_HAS_USERS(11102, "角色下有用户，请先删除用户"),

    /* ------------------------ 业务错误 - 组织模块 ------------------------ */
    ORG_CODE_EXISTS(11201, "组织编码已存在"),
    ORG_PARENT_NOT_EXISTS(11202, "上级组织不存在"),
    ORG_CANNOT_BE_SELF_PARENT(11203, "不能将组织自身设为上级"),
    ORG_CANNOT_MOVE_TO_CHILD(11204, "不能将组织移动到自己的子组织下"),
    ORG_HAS_CHILDREN(11205, "组织下有子组织，请先删除子组织"),
    ORG_HAS_USERS(11206, "组织下有用户，请先删除用户"),


    /* ------------------------ 业务错误 - 业财路由模块 ------------------------ */
    BUSINESS_FINANCE_MODEL_NOT_EXISTS(12001, "业财模型不存在"),
    BUSINESS_FINANCE_MODEL_PUBLISHED(12002, "已发布的模型不能删除"),

    ;

    /**
     * 业务状态码（非 HTTP 状态码）
     */
    private final Integer code;

    /**
     * 返回给前端的提示消息
     */
    private final String message;

}
