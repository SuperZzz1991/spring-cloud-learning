package com.donghao.cloud.hystrixservice.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @Author: DongHao
 * @Date: 2022/1/12 10:29
 * @Description:
 *  UserService中使用Hystrix缓存时发生异常,需要初始化HystrixRequestContext
 *  解决异常：
 *      java.lang.IllegalStateException: Request caching is not available.
 *      Maybe you need to initialize the HystrixRequestContext?
 */
@Component
@WebFilter(urlPatterns = "/*", asyncSupported = true)
public class HystrixRequestContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try{
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            context.close();
        }
    }
}
