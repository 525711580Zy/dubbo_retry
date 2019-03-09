package com.zy.shiro;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.spring.boot.DubboProperties;
import com.zy.entity.Menu;
import com.zy.entity.Role;
import com.zy.entity.User;
import com.zy.service.LoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {
/*    @Autowired
    ShiroApplication application;*/
    @Autowired
    private DubboProperties dubboProperties;

    private LoginService loginService;
    @Override
    //用户触发登录请求时自动执行,发生登录操作时将权限加入到shiro中
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user =(User)principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for(Role role:user.getRoles()){
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRole());
            for(Menu menu:role.getMenus()){
                //给某个用户下的某个角色赋权限
                simpleAuthorizationInfo.addStringPermission(menu.getUrl());
            }
        }
        System.out.println("从MyShirReallm出来了");
        return simpleAuthorizationInfo;
    }

    @Override
    //hashRole（"admin"）或者isPermitted("admin")
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取认证登录名
        Object username = authenticationToken.getPrincipal();
        if(username==null){
            return null;
        }else{
            //获取用户信息,将用户对象和认证密码保存在shiro中
//            LoginService loginService = application.getBean("loginService",LoginService.class);
            System.out.println("成功获取loginService对象");
            setLoginService();
            User user = loginService.findUserByName(username.toString());
            if(user==null){
                return null;
            }else{
                SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword().toString(),getName());
                return simpleAuthenticationInfo;
            }
        }
    }

    public void setLoginService(){
        ReferenceConfig<LoginService> reference=new ReferenceConfig<>();
        reference.setApplication(dubboProperties.getApplication());
        reference.setRegistry(dubboProperties.getRegistry());
        reference.setVersion("1.0");
        reference.setInterface(LoginService.class);
        this.loginService = reference.get();
    }

}

