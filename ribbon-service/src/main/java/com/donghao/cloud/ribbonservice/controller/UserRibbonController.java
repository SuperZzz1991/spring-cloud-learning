package com.donghao.cloud.ribbonservice.controller;

import com.donghao.cloud.ribbonservice.pojo.Response;
import com.donghao.cloud.ribbonservice.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: DongHao
 * @Date: 2022/1/11 16:04
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserRibbonController {

    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{id}")
    public Response getUser(@PathVariable Long id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Response.class, id);
    }

    @GetMapping("/getByUsername")
    public Response getByUsername(@RequestParam String username) {
        return restTemplate.getForObject(userServiceUrl + "/user/getByUsername?username={1}", Response.class, username);
    }

    @GetMapping("/getEntityByUsername")
    public Response getEntityByUsername(@RequestParam String username) {
        ResponseEntity<Response> entity = restTemplate.getForEntity(userServiceUrl + "/user/getByUsername?username={1}", Response.class, username);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return new Response("操作失败", 500);
        }
    }

    @PostMapping("/create")
    public Response create(@RequestBody User user) {
        return restTemplate.postForObject(userServiceUrl + "/user/create", user, Response.class);
    }

    @PostMapping("/update")
    public Response update(@RequestBody User user) {
        return restTemplate.postForObject(userServiceUrl + "/user/update", user, Response.class);
    }

    @PostMapping("/delete/{id}")
    public Response delete(@PathVariable Long id) {
        return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, Response.class, id);
    }
}
