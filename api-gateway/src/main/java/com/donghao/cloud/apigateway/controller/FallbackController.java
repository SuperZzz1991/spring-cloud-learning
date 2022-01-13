package com.donghao.cloud.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DongHao
 * @Date: 2022/1/13 10:27
 * @Description: 服务降级
 */
@RestController
public class FallbackController {
    @GetMapping("/fallback")
    public Object fallback() {
        Map<String, Object> result = new HashMap<>();
        result.put("data",null);
        result.put("message","Get request fallback!");
        result.put("code",500);
        return result;
    }
}
