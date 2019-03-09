package com.zy.service;

import com.zy.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface UserManageService {
    Page<User> getUserList(int pageindex, int pagesize);
    void addUser(User user, boolean isRole, List<Integer> roleid);
    void delUser(int userId);
}
