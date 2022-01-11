package com.donghao.cloud.userservice.controller;

import com.donghao.cloud.userservice.pojo.Response;
import com.donghao.cloud.userservice.pojo.User;
import com.donghao.cloud.userservice.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author: DongHao
 * @Date: 2022/1/11 15:55
 * @Description:
 */
@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Response create(@RequestBody User user) {
        userService.create(user);
        return new Response("操作成功", 200);
    }

    @GetMapping("/{id}")
    public Response<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        log.info("根据id获取用户信息，用户名称为：{}",user.getUsername());
        return new Response<>(user);
    }

    @GetMapping("/getUserByIds")
    public Response<List<User>> getUserByIds(@RequestParam List<Long> ids) {
        List<User> userList= userService.getUserByIds(ids);
        log.info("根据ids获取用户信息，用户列表为：{}",userList);
        return new Response<>(userList);
    }

    @GetMapping("/getByUsername")
    public Response<User> getByUsername(@RequestParam String username) {
        User user = userService.getByUsername(username);
        return new Response<>(user);
    }

    @PostMapping("/update")
    public Response update(@RequestBody User user) {
        userService.update(user);
        return new Response("操作成功", 200);
    }

    @PostMapping("/delete/{id}")
    public Response delete(@PathVariable Long id) {
        userService.delete(id);
        return new Response("操作成功", 200);
    }
}
