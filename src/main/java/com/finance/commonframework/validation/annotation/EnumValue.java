package com.finance.commonframework.validation.annotation;

import com.finance.commonframework.validation.EnumCode;
import com.finance.commonframework.validation.validator.EnumValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举值校验注解
 * <p>
 * 使用示例：
 * <pre>
 * // 基本使用 - 允许所有枚举值
 * {@code @EnumValue(enumClass = StatusEnum.class, message = "状态不合法")}
 * private String status;
 *
 * // 排除指定枚举值
 * {@code @EnumValue(
 *     enumClass = EntryMergeModeEnum.class,
 *     excludeValues = {"NONE"},  // 不允许 NONE
 *     message = "分录合并模式只能是BY_SUBJECT或BY_SUBJECT_AND_SUMMARY"
 * )}
 * private String entryMergeMode;
 *
 * // 只允许指定枚举值
 * {@code @EnumValue(
 *     enumClass = StatusEnum.class,
 *     allowValues = {"ACTIVE", "PENDING"},  // 只允许 ACTIVE 和 PENDING
 *     message = "状态只能是ACTIVE或PENDING"
 * )}
 * private String status;
 * </pre>
 * <p>
 * <b>注意事项：</b>
 * <ul>
 *   <li>excludeValues 和 allowValues 填写的都是枚举常量的 name，不是 code 值</li>
 *   <li>excludeValues 和 allowValues 不能同时使用，只能二选一</li>
 *   <li>如果填写的枚举名称不存在，会在运行时记录警告日志</li>
 *   <li>建议在 IDE 中通过查看枚举类来确认可用的枚举名称</li>
 * </ul>
 *
 * @author qingfeng
 * @since 2025/11/25
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
public @interface EnumValue {

    /**
     * 目标枚举类（必须实现 EnumCode 接口）
     */
    Class<? extends EnumCode<?>> enumClass();

    /**
     * 是否必填（为false时，null值会通过校验）
     */
    boolean required() default false;

    /**
     * 只允许的枚举值（填写枚举常量的 name）
     * <p>
     * <b>重要：</b>
     * <ul>
     *   <li>填写枚举常量的名称（name），不是 code 值</li>
     *   <li>只有在此列表中的枚举值才允许使用</li>
     *   <li>不能与 excludeValues 同时使用</li>
     * </ul>
     * <p>
     * 示例：
     * <pre>
     * // 枚举定义
     * public enum StatusEnum {
     *     ACTIVE("ACTIVE", "激活"),
     *     INACTIVE("INACTIVE", "未激活"),
     *     PENDING("PENDING", "待处理"),
     *     DELETED("DELETED", "已删除")
     * }
     *
     * // 使用 - 只允许 ACTIVE 和 PENDING
     * allowValues = {"ACTIVE", "PENDING"}
     * </pre>
     */
    String[] allowValues() default {};

    /**
     * 排除的枚举值（填写枚举常量的 name）
     * <p>
     * <b>重要：</b>
     * <ul>
     *   <li>填写枚举常量的名称（name），不是 code 值</li>
     *   <li>列表中的枚举值不允许使用</li>
     *   <li>不能与 allowValues 同时使用</li>
     * </ul>
     * <p>
     * 示例：
     * <pre>
     * // 枚举定义
     * public enum EntryMergeModeEnum {
     *     NONE("NONE", "不合并"),
     *     BY_SUBJECT("BY_SUBJECT", "科目相同"),
     *     BY_SUBJECT_AND_SUMMARY("BY_SUBJECT_AND_SUMMARY", "科目相同且摘要相同")
     * }
     *
     * // 使用 - 排除 NONE
     * excludeValues = {"NONE"}
     * </pre>
     */
    String[] excludeValues() default {};

    /**
     * 错误提示信息
     */
    String message() default "枚举值不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
