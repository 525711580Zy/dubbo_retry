package com.zy.service;

import com.zy.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    List<Role> getALL();
    Page<Role> roleList(int pageindex, int pagesize);
    void addRole(Role role, boolean isMenu, List<Integer> menus);
    Role getRole(int roleId);
    void removeRole(int roleId);
}
