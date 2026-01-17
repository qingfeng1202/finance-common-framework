package com.finance.commonframework.filter;

import com.finance.commonframework.constant.MDCConstant;
import com.finance.commonframework.enums.LoginHeaderEnum;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 请求MDC过滤器
 * <p>
 * 用于在请求处理过程中设置MDC上下文，便于日志追踪
 * </p>
 *
 * @author qingfeng
 * @since 2026/1/12
 */
@Slf4j
@Component
@Order(1)
public class RequestMDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            // 放入请求路径
            String requestPath = httpRequest.getRequestURI();
            MDC.put(MDCConstant.REQUEST_PATH, requestPath);

            String traceId = httpRequest.getHeader(LoginHeaderEnum.TRACE_ID.getCode());
            if(traceId != null) {
                MDC.put(MDCConstant.TRACE_ID, traceId);
            }

            // 继续执行过滤器链
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDCConstant.REQUEST_PATH);
            MDC.remove(MDCConstant.TRACE_ID);
        }

    }

}
