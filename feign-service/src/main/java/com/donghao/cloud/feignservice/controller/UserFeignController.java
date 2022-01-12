package com.donghao.cloud.feignservice.controller;

import com.donghao.cloud.feignservice.pojo.Response;
import com.donghao.cloud.feignservice.pojo.User;
import com.donghao.cloud.feignservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: DongHao
 * @Date: 2022/1/12 14:32
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserFeignController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Response getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/getByUsername")
    public Response getByUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }

    @PostMapping("/create")
    public Response create(@RequestBody User user) {
        return userService.create(user);
    }

    @PostMapping("/update")
    public Response update(@RequestBody User user) {
        return userService.update(user);
    }

    @PostMapping("/delete/{id}")
    public Response delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}
