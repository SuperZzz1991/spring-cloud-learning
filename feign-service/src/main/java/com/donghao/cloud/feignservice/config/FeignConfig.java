package com.donghao.cloud.feignservice.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DongHao
 * @Date: 2022/1/12 15:18
 * @Description:
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feginLoggerLevel() {
        return Logger.Level.FULL;
    }
}
