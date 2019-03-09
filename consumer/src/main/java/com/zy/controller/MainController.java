package com.zy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zy.service.UserManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainController {

    @Reference(version = "1.4")
    private UserManageService userManageService;
    @RequestMapping("/toMain")
    public String toMain(){
        System.out.println("进入了toMain");
        return "main";
    }

    @RequestMapping("/toUserList")
    public String toUserList(){
        System.out.println("进入了userList");
        return "userList";
    }
}
