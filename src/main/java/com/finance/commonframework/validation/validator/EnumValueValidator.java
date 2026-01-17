package com.finance.commonframework.validation.validator;

import com.finance.commonframework.validation.EnumCode;
import com.finance.commonframework.validation.annotation.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * 枚举值校验器
 * <p>
 * 用于校验枚举值的有效性
 * </p>
 *
 * @author qingfeng
 * @since 2025/11/25
 */
@Slf4j
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private boolean required;
    private Set<Object> validValues;
    private Class<? extends EnumCode<?>> enumClass;
    private ValidationMode validationMode;

    /**
     * 校验模式
     */
    private enum ValidationMode {
        /**
         * 允许所有枚举值
         */
        ALL,
        /**
         * 只允许指定的枚举值
         */
        ALLOW_ONLY,
        /**
         * 排除指定的枚举值
         */
        EXCLUDE
    }

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.required = constraintAnnotation.required();
        this.validValues = new HashSet<>();
        this.enumClass = constraintAnnotation.enumClass();

        // 检查是否是枚举类
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException(
                    String.format("类 %s 不是枚举类型", enumClass.getName())
            );
        }

        // 获取配置
        String[] allowValues = constraintAnnotation.allowValues();
        String[] excludeValues = constraintAnnotation.excludeValues();

        // 校验：allowValues 和 excludeValues 不能同时使用
        if (allowValues.length > 0 && excludeValues.length > 0) {
            throw new IllegalArgumentException(
                    String.format("@EnumValue 配置错误:  allowValues 和 excludeValues 不能同时使用 (枚举类: %s)",
                            enumClass.getSimpleName())
            );
        }

        // 获取所有枚举常量
        EnumCode<?>[] enums = enumClass.getEnumConstants();
        if (enums == null || enums.length == 0) {
            throw new IllegalArgumentException(
                    String.format("枚举类 %s 没有定义任何常量", enumClass.getName())
            );
        }

        // 确定校验模式并构建有效值集合
        if (allowValues.length > 0) {
            // 模式1：只允许指定值
            this.validationMode = ValidationMode.ALLOW_ONLY;
            buildAllowOnlyValues(enums, allowValues);
        } else if (excludeValues.length > 0) {
            // 模式2：排除指定值
            this.validationMode = ValidationMode.EXCLUDE;
            buildExcludeValues(enums, excludeValues);
        } else {
            // 模式3：允许所有值
            this.validationMode = ValidationMode.ALL;
            buildAllValues(enums);
        }

        log.debug("@EnumValue 初始化完成 - 枚举类: {}, 校验模式: {}, 有效值数量: {}",
                enumClass.getSimpleName(), validationMode, validValues.size());
    }

    /**
     * 构建所有有效值（允许所有枚举值）
     */
    private void buildAllValues(EnumCode<?>[] enums) {
        for (EnumCode<?> enumConstant : enums) {
            Object code = enumConstant.getCode();
            if (code != null) {
                validValues.add(code);
            }
        }
    }

    /**
     * 构建只允许的值
     */
    private void buildAllowOnlyValues(EnumCode<?>[] enums, String[] allowEnumNames) {
        // 收集所有有效的枚举名称，用于错误提示
        Set<String> validEnumNames = new HashSet<>();
        for (EnumCode<?> enumConstant : enums) {
            if (enumConstant instanceof Enum) {
                validEnumNames.add(((Enum<?>) enumConstant).name());
            }
        }

        for (String enumName : allowEnumNames) {
            if (enumName == null || enumName.trim().isEmpty()) {
                continue;
            }

            String trimmedName = enumName.trim();
            boolean found = false;

            // 通过枚举名称查找对应的枚举实例
            for (EnumCode<?> enumConstant : enums) {
                if (enumConstant instanceof Enum<?> e) {
                    if (e.name().equals(trimmedName)) {
                        Object code = enumConstant.getCode();
                        if (code != null) {
                            validValues.add(code);
                            found = true;
                        }
                        break;
                    }
                }
            }

            // 如果没找到，记录警告并抛出异常
            if (!found) {
                String errorMsg = String.format(
                        "@EnumValue 配置错误: 枚举 %s 中不存在名为 '%s' 的常量。有效的枚举名称:  %s",
                        enumClass.getSimpleName(), trimmedName, validEnumNames
                );
                log.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        }

        if (validValues.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("@EnumValue 配置错误:  allowValues 不能为空或全部无效 (枚举类: %s)",
                            enumClass.getSimpleName())
            );
        }
    }

    /**
     * 构建排除值后的有效值
     */
    private void buildExcludeValues(EnumCode<?>[] enums, String[] excludeEnumNames) {
        // 先添加所有枚举值
        Set<Object> excludeCodes = new HashSet<>();

        // 收集所有有效的枚举名称
        Set<String> validEnumNames = new HashSet<>();
        for (EnumCode<?> enumConstant : enums) {
            Object code = enumConstant.getCode();
            if (code != null) {
                validValues.add(code);
            }
            if (enumConstant instanceof Enum) {
                validEnumNames.add(((Enum<?>) enumConstant).name());
            }
        }

        // 找出需要排除的code值
        for (String enumName : excludeEnumNames) {
            if (enumName == null || enumName.trim().isEmpty()) {
                continue;
            }

            String trimmedName = enumName.trim();
            boolean found = false;

            for (EnumCode<?> enumConstant : enums) {
                if (enumConstant instanceof Enum<?> e) {
                    if (e.name().equals(trimmedName)) {
                        Object code = enumConstant.getCode();
                        if (code != null) {
                            excludeCodes.add(code);
                            found = true;
                        }
                        break;
                    }
                }
            }

            // 如果没找到，记录警告
            if (!found) {
                log.warn("@EnumValue 配置警告: 枚举 {} 中不存在名为 '{}' 的常量。有效的枚举名称:  {}",
                        enumClass.getSimpleName(), trimmedName, validEnumNames);
            }
        }

        // 从有效值中移除排除的值
        validValues.removeAll(excludeCodes);

        if (validValues.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("@EnumValue 配置错误: 排除所有枚举值后，没有剩余的有效值 (枚举类: %s)",
                            enumClass.getSimpleName())
            );
        }
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        // null值处理
        if (value == null) {
            return !required;
        }

        // 空字符串处理
        if (value instanceof String) {
            String strValue = ((String) value).trim();
            if (strValue.isEmpty()) {
                return !required;
            }
            value = strValue;
        }

        // 校验值是否在有效列表中
        boolean valid = validValues.contains(value);

        if (!valid) {
            log.debug("@EnumValue 校验失败: 值 '{}' 不在枚举 {} 的有效值中 (校验模式: {})",
                    value, enumClass.getSimpleName(), validationMode);
        }

        return valid;
    }

}
