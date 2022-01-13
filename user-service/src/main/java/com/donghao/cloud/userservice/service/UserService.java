package com.donghao.cloud.userservice.service;

import com.donghao.cloud.userservice.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: DongHao
 * @Date: 2022/1/11 16:17
 * @Description:
 */
@Service
public class UserService {
    private List<User> userList;

    public void create(User user) {
        userList.add(user);
    }

    public User getUser(Long id) {
        List<User> findUserList = userList.stream().filter(userItem -> userItem.getId().equals(id)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(findUserList)) {
            return findUserList.get(0);
        }
        return null;
    }

    public void update(User user) {
        userList.stream().filter(userItem -> userItem.getId().equals(user.getId())).forEach(userItem -> {
            userItem.setUsername(user.getUsername());
            userItem.setPassword(user.getPassword());
        });
    }

    public void delete(Long id) {
        User user = getUser(id);
        if (user != null) {
            userList.remove(user);
        }
    }

    public User getByUsername(String username) {
        List<User> findUserList = userList.stream().filter(userItem -> userItem.getUsername().equals(username)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(findUserList)) {
            return findUserList.get(0);
        }
        return null;
    }

    public List<User> getUserByIds(List<Long> ids) {
        return userList.stream().filter(userItem -> ids.contains(userItem.getId())).collect(Collectors.toList());
    }

    @PostConstruct
    public void initData() {
        userList = new ArrayList<>();
        userList.add(new User(1L, "Tom", "123456"));
        userList.add(new User(2L, "Jerry", "123456"));
        userList.add(new User(3L, "Maike", "123456"));
    }
}
