package com.tigercub.commonframework.context;

import com.tigercub.commonframework.model.dto.CurrentUserDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户上下文 - 用于在整个请求生命周期中传递用户信息
 * 使用 ThreadLocal 确保线程安全
 * </p>
 *
 * @author qingfeng
 * @since 2025/11/29
 */
@Slf4j
public class UserContext {

    private static final ThreadLocal<CurrentUserDTO> CONTEXT = new ThreadLocal<>();

    /**
     * 设置当前用户信息
     */
    public static void setCurrentUser(CurrentUserDTO currentUser) {
        if (currentUser == null) {
            log.warn("尝试设置空的用户信息到 UserContext");
            return;
        }
        CONTEXT.set(currentUser);
        log.debug("用户信息已设置到 UserContext: userId={}", currentUser.getUserId());
    }

    /**
     * 获取当前用户信息
     */
    public static CurrentUserDTO getCurrentUser() {
        return CONTEXT.get();
    }

    /**
     * 获取当前用户ID
     */
    public static Integer getCurrentUserId() {
        CurrentUserDTO currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getUserId() : null;
    }

    /**
     * 获取当前租户ID
     */
    public static Integer getCurrentTenantId() {
//        CurrentUserDTO currentUser = getCurrentUser();
//        return currentUser != null ? currentUser.getTenantId() : null;

        return 0;
    }

    /**
     * 检查是否已设置用户信息
     */
    public static boolean isAuthenticated() {
        return CONTEXT.get() != null;
    }

    /**
     * 清除当前线程的用户信息
     */
    public static void clear() {
        Integer userId = getCurrentUserId();
        CONTEXT.remove();
        if (userId != null) {
            log.debug("UserContext 已清除: userId={}", userId);
        }
    }

}
