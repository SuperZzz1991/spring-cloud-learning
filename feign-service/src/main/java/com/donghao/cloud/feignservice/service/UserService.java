package com.donghao.cloud.feignservice.service;

import com.donghao.cloud.feignservice.pojo.Response;
import com.donghao.cloud.feignservice.pojo.User;
import com.donghao.cloud.feignservice.service.impl.UserFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: DongHao
 * @Date: 2022/1/12 14:30
 * @Description:
 */
@FeignClient(value = "user-service", fallback = UserFallbackService.class)
public interface UserService {
    @PostMapping("/user/create")
    Response create(@RequestBody User user);

    @GetMapping("/user/{id}")
    Response<User> getUser(@PathVariable Long id);

    @GetMapping("/user/getByUsername")
    Response<User> getByUsername(@RequestParam String username);

    @PostMapping("/user/update")
    Response update(@RequestBody User user);

    @PostMapping("/user/delete/{id}")
    Response delete(@PathVariable Long id);
}
