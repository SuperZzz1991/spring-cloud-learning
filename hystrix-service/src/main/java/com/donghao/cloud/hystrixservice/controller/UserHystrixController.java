package com.donghao.cloud.hystrixservice.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.donghao.cloud.hystrixservice.pojo.Response;
import com.donghao.cloud.hystrixservice.pojo.User;
import com.donghao.cloud.hystrixservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: DongHao
 * @Date: 2022/1/12 09:18
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserHystrixController {
    @Autowired
    private UserService userService;

    @GetMapping("/testFallback/{id}")
    public Response testFallback(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/testCommand/{id}")
    public Response testCommand(@PathVariable Long id) {
        return userService.getUserCommand(id);
    }

    @GetMapping("/testException/{id}")
    public Response testException(@PathVariable Long id) {
        return userService.getUserException(id);
    }

    @GetMapping("/testCache/{id}")
    public Response testCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.getUserCache(id);
        userService.getUserCache(id);
        return new Response("操作成功", 200);
    }

    @GetMapping("/testRemoveCache/{id}")
    public Response testRemoveCache(@PathVariable Long id) {
        userService.getUserCache(id);
        userService.removeCache(id);
        userService.getUserCache(id);
        return new Response("操作成功", 200);
    }

    @GetMapping("/testCollapser")
    public Response testCollapser() throws ExecutionException, InterruptedException {
        Future<User> future1 = userService.getUserFuture(1L);
        Future<User> future2 = userService.getUserFuture(2L);
        future1.get();
        future2.get();
        ThreadUtil.safeSleep(200);
        Future<User> future3 = userService.getUserFuture(3L);
        future3.get();
        return new Response("操作成功", 200);
    }
}
