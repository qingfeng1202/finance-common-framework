package com.tigercub.commonframework.filter;

import com.tigercub.commonframework.model.constant.MDCConstant;
import com.tigercub.commonframework.model.enums.LoginHeaderEnum;
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
 * <p>
 * RequestMDCFilter
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
