package com.tigercub.commonframework.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 当前用户信息 DTO - 用于在 UserContext 中传递
 * </p>
 *
 * @author qingfeng
 * @since 2025/11/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDTO {

    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 登录账号
     */
    private String loginAccount;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 组织ID
     */
    private Integer orgId;

    /**
     * 角色ID
     */
    private Integer roleId;

}
