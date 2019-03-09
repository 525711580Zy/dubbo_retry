package com.zy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zy.entity.User;
import com.zy.log.LogInfo;
import com.zy.service.RoleService;
import com.zy.service.UserManageService;
import com.zy.utils.MD5Utils;
import com.zy.utils.TableResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/main")
public class UserListController {
    @Reference(version = "1.2")
    private RoleService roleService;
    @Reference(version = "1.4")
    private UserManageService userManageService;
    @RequestMapping("/getUserList") //pageindex为页码，pagecount为记录数
    @ResponseBody
    public Object getUserList(@RequestParam("cp") Integer currentPage,@RequestParam("ps") Integer pageSize){
        System.out.println(userManageService);
        //user是Page对象,
        Page<User> users = userManageService.getUserList(currentPage,pageSize);
        System.out.println(users.getContent());
        TableResult<List<User>> tr = new TableResult<>(currentPage,users.getTotalElements(),users.getContent());
        return tr;
    }

    @RequestMapping("/getRole")
    @ResponseBody
    public Object getRole(){
        return roleService.getALL();
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @ResponseBody//添加用户时候的控制器方法
    @LogInfo(title = "用户管理",action = "添加用户")
    public Object addUser(User user, @RequestParam("role")List<Integer>roles, @RequestParam("upload")MultipartFile unload) throws IOException {
        //获取文源路径
        StringBuilder sb = new StringBuilder("/ui/user/");
        //获取文件名
        String originalFilename = unload.getOriginalFilename();
        //获取文件上传的路径
        String file = this.getClass().getResource("/static/ui/user").getFile();
        //把图片文件上传
        unload.transferTo(new File(file,originalFilename));
        sb.append(originalFilename);
        user.setIcon(sb.toString());
        //密码
        user.setPassword(new MD5Utils().EncoderByMd5(user.getPassword()));
        //添加用户时候分配角色

        userManageService.addUser(user,true,roles);
        return true;
    }
    //删除用户
    @RequestMapping("/delUser")
    @ResponseBody
    @LogInfo(title = "用户管理",action = "删除用户")
    public Object delUser(int userId){
        try{
            userManageService.delUser(userId);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
