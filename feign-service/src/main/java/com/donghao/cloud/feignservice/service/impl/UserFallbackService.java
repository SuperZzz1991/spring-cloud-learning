package com.donghao.cloud.feignservice.service.impl;

import com.donghao.cloud.feignservice.pojo.Response;
import com.donghao.cloud.feignservice.pojo.User;
import com.donghao.cloud.feignservice.service.UserService;
import org.springframework.stereotype.Component;

/**
 * @Author: DongHao
 * @Date: 2022/1/12 14:41
 * @Description:
 */
@Component
public class UserFallbackService implements UserService {
    @Override
    public Response create(User user) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return new Response<>(defaultUser);
    }

    @Override
    public Response<User> getUser(Long id) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return new Response<>(defaultUser);
    }

    @Override
    public Response<User> getByUsername(String username) {
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return new Response<>(defaultUser);
    }

    @Override
    public Response update(User user) {
        return new Response("调用失败，服务被降级",500);
    }

    @Override
    public Response delete(Long id) {
        return new Response("调用失败，服务被降级",500);
    }
}
