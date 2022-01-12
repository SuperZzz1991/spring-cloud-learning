package com.donghao.cloud.hystrixservice.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.donghao.cloud.hystrixservice.pojo.Response;
import com.donghao.cloud.hystrixservice.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @Author: DongHao
 * @Date: 2022/1/12 09:21
 * @Description:
 */
@Log4j2
@Service
public class UserService {

    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getDefaultUser")
    public Response getUser(Long id){
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Response.class, id);
    }

    @HystrixCommand(fallbackMethod = "getDefaultUser",
            commandKey = "getUserCommand",
            groupKey = "getUserGroup",
            threadPoolKey = "getUserThreadPool")
    public Response getUserCommand(@PathVariable Long id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Response.class, id);
    }

    @HystrixCommand(fallbackMethod = "getDefaultUser2", ignoreExceptions = {NullPointerException.class})
    public Response getUserException(Long id) {
        if (id == 1) {
            throw new IndexOutOfBoundsException();
        } else if (id == 2) {
            throw new NullPointerException();
        }
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Response.class, id);
    }

    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "getDefaultUser", commandKey = "getUserCache")
    public Response getUserCache(Long id) {
        log.info("getUserCache id:{}", id);
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", Response.class, id);
    }

    /**
     * 为缓存生成key的方法
     */
    public String getCacheKey(Long id) {
        return String.valueOf(id);
    }

    @CacheRemove(commandKey = "getUserCache", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public Response removeCache(Long id) {
        log.info("removeCache id:{}", id);
        return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, Response.class, id);
    }

    public Response getDefaultUser(@PathVariable Long id){
        User defaultUser = new User(-1L, "defaultUser", "123456");
        return new Response<>(defaultUser);
    }

    public Response getDefaultUser2(@PathVariable Long id, Throwable e) {
        log.error("getDefaultUser2 id:{},throwable class:{}", id, e.getClass());
        User defaultUser = new User(-2L, "defaultUser2", "123456");
        return new Response(defaultUser);
    }

    @HystrixCollapser(batchMethod = "getUserByIds",
        collapserProperties = {@HystrixProperty(name="timerDelayInMilliseconds", value = "100")
    })
    public Future<User> getUserFuture(Long id) {
        return new AsyncResult<User>(){
            @Override
            public User invoke() {
                Response commonResult = restTemplate.getForObject(userServiceUrl + "/user/{1}", Response.class, id);
                Map data = (Map) commonResult.getData();
                User user = BeanUtil.mapToBean(data,User.class,true);
                log.info("getUserById username:{}", user.getUsername());
                return user;
            }
        };
    }

    @HystrixCommand
    public List<User> getUserByIds(List<Long> ids) {
        log.info("getUserByIds:{}", ids);
        Response response = restTemplate.getForObject(userServiceUrl + "/user/getUserByIds?ids={1}", Response.class, CollUtil.join(ids,","));
        return (List<User>) response.getData();
    }
}
