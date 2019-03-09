package com.zy.utils;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class CreadentialsMatcher extends SimpleCredentialsMatcher {
    //获取用户验证的令牌 UserName Password

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken utoken = (UsernamePasswordToken)token;

        String source = String.valueOf(utoken.getPassword());
        String strsh = MD5Utils.EncoderByMd5(source);
        //打印最终结果
        System.out.println("正确密码为："+strsh);
        //获取数据库中的密码
        String dbPassword = (String)getCredentials(info);
        System.out.println("数据库密码为："+dbPassword);
        //进行密码的对比
        return this.equals(strsh,dbPassword);
    }
}
