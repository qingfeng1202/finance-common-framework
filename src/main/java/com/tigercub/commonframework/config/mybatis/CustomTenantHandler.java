package com.tigercub.commonframework.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.finance.bfe.context.UserContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * <p>
 * CustomTenantHandler
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
