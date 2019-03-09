package com.zy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zy.dao.MenuRepository;
import com.zy.entity.Menu;
import com.zy.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(version = "1.1",interfaceClass = MenuService.class)
@Component
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Override
    public List<Menu> getAllMenu() {
        List<Menu> all = menuRepository.findAll();
        System.out.println();
        return all;
    }

    @Override
    public Object[] getMenusByRole(int roleid) {
        return menuRepository.getMenuByRole(roleid);
    }
}
