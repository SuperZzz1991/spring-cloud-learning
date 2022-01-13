package com.donghao.cloud.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DongHao
 * @Date: 2022/1/13 10:05
 * @Description: 利用config配置path路由
 */
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route2", r -> r.path("/user/getByUserName").uri("http://localhost:8201/user/getByUsername"))
                .build();
    }
}
