package com.finance.commonframework.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.finance.commonframework.context.UserContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 自定义租户处理器
 * <p>
 * 用于实现多租户数据隔离功能
 * </p>
 *
 * @author qingfeng
 * @since 2026/1/15
 */
public class CustomTenantHandler implements TenantLineHandler {
    @Override
    public Expression getTenantId() {
        Integer tenantId = UserContext.getCurrentTenantId();
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return "tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        if ("bfe_user_token".equals(tableName)) {
            return true;
        }
        if("bfe_user_login_fail".equals(tableName)) {
            return true;
        }


        return false;
    }
}
