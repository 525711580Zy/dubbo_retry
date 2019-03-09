package com.zy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zy.entity.Menu;
import com.zy.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MenuController {
    @Reference(version = "1.1")
    private MenuService menuService;

    @RequestMapping("/getMenus")
    @ResponseBody
    public Object getALLMenus(){
        System.out.println("进入到getMenus里面啦");
        List<Menu> allMenu = menuService.getAllMenu();
        System.out.println();
        return allMenu;
    }

    @ResponseBody
    @RequestMapping("/getMenusByRole")
    public Object[] getMenusByRole(int roleid){
        System.out.println("进入了获取某个角色的权限controller");

        Object[] menusByRole = menuService.getMenusByRole(roleid);
        return menusByRole;
    }
}
