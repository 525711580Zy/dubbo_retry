package com.zy.service;

import com.zy.entity.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getAllMenu();
    Object[] getMenusByRole(int roleid);

}
