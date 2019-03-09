package com.zy.service;

import com.zy.entity.User;

public interface LoginService {
    void unlock(String loginname);
    void addUser(User user);
    User findUserByName(String userName);
}
