package com.donghao.cloud.hystrixservice.pojo;

import lombok.Data;

/**
 * @Author: DongHao
 * @Date: 2022/1/11 16:07
 * @Description:
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;

    public User(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
