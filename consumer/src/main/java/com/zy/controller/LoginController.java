package com.zy.controller;

import com.zy.entity.User;
import com.zy.log.LogInfo;
import com.zy.utils.GifCaptcha;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/toLogin")
    public String toLogin(){
        System.out.println("进入了tologin");
        return "login";
    }
    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    @LogInfo(title = "登录状态",action = "尝试登陆")
    public Object login(String username,String password,String vCode,HttpSession session){
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        String code = session.getAttribute("vCode").toString();
        String msg="";
        //先验证验证码是否正确，减轻数据库验证错误时候的压力
        if(code.equals(vCode)){
            //验证用户是否被锁定
            try{
                //根据令牌完成登录
                subject.login(token);
                User user = (User)subject.getPrincipal();
                if(user.getIsactive()==1){
                    //ajax回调状态码
                    session.setAttribute("userDetail",user);
                    msg = "{success:true,message:'登录成功'}";
                }else {
                    msg="{success:false,message:'请点击右键进行激活'}";
                }
            } catch (UnknownAccountException e){
                msg="{success:false,message:'用户名不存在'}";
            }catch (IncorrectCredentialsException e){
                msg="{success:false,message:'密码错误'}";
            }
        }else {
            msg="{success:false,message:'验证码不正确'}";
        }return msg;
    }

    @RequestMapping(value = "/getGifCode",method = RequestMethod.GET)
    public void getGifCode(HttpServletResponse response, HttpSession session){
        /**
         * gif格式动画验证码
         * 宽，高，位数
         */
        try {
            GifCaptcha gifCaptcha = new GifCaptcha(80, 30, new Font("宋体", Font.BOLD, 20), 300);
            response.setHeader("pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");//页面不缓存

            try {
                gifCaptcha.out(response.getOutputStream());
                session.setAttribute("vCode", gifCaptcha.getWord());//将验证码保存到session
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            System.err.println("获取验证码异常"+e.getMessage());
            //搞不清楚这里为啥要弄个大的try catch
        }
    }


}
