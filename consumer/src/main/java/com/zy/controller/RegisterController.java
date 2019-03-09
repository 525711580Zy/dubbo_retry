package com.zy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zy.entity.User;
import com.zy.service.LoginService;
import com.zy.service.SendMailService;
import com.zy.utils.MD5Utils;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/register")
@EnableAsync
public class RegisterController {
    @Reference(version = "1.3")
    private SendMailService sendMailService;
    @Reference(version = "1.0")
    private LoginService loginService;

    @RequestMapping( "/registerHtml")
    public String html(){
        System.out.println("进入registerHtml");
        return "register";
    }
    @ResponseBody
    @RequestMapping(value = "/registerAjax")
    public Object ajax(HttpServletRequest req, User user, @RequestParam("upload") MultipartFile upload) throws IOException {
        System.out.println("进入到ajaxController");
 /*       String logName=req.getParameter("login_name");
        System.out.println("登录名："+logName);*/
        StringBuilder sb = new StringBuilder("/ui/user/");

        //拼接激活链接，防止硬编码
        StringBuilder activeUrl = new StringBuilder(req.getScheme());
        activeUrl.append("://");
        activeUrl.append(req.getServerName());
        activeUrl.append(":");
        activeUrl.append(req.getServerPort());
        activeUrl.append(req.getContextPath());
        activeUrl.append("/register/unlock");
        activeUrl.append("/"+user.getLogin_name());
        activeUrl.append("/"+UUID.randomUUID().toString());
        //获取文件名,文件上传，也就是在服务器的user路径下存储图片
        //获取文件名
        String originalFileName = upload.getOriginalFilename();
        //获取文件上传路径
        String file = this.getClass().getResource("/static/ui/user").getFile();
        System.out.println("文件上传路径为："+file);
        upload.transferTo(new File(file,originalFileName));
        System.out.println("图片上传成功");
        //保存头像路径，路径存储到数据库中
        sb.append(originalFileName);
        user.setIcon(sb.toString());
        //密码md5加密
        try {
            user.setPassword(new MD5Utils().EncoderByMd5(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginService.addUser(user);
        System.out.println("保存用户成功");
        sendMailService.sendMail(user.getEmail(),activeUrl.toString());
        System.out.println("发送邮件成功");
        return true;
    }
    @RequestMapping("/unlock/{loginName}/{UUid}")
    public String unlock(@PathVariable("loginName")String loginName){
        loginService.unlock(loginName);
        return "login";
    }
    @RequestMapping("/jumpToLogin")
    public String login(){
        System.out.println("进入login");
        return "login";
    }

}
