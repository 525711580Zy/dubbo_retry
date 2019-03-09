package com.zy.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration//shiro配置类
public class ShiroConfiguration {
    @Bean//用于生成thymeleaf模板
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
    @Bean //配置自定义密码比对器
    public CreadentialsMatcher creadentialsMatcher(){
        return new CreadentialsMatcher();
    }
    @Bean//配置Shiro自定义的验证方式
    public MyShiroRealm myShiroRealm(CreadentialsMatcher creadentialsMatcher){
        //自定义验证器
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(creadentialsMatcher);
        return myShiroRealm;
    }
    @Bean//权限管理，配置主要Realm的管理认证
    public SecurityManager securityManager(MyShiroRealm myShiroRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        //自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        //自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    @Bean //配置FactoryBean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean bean =new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        //拦截器
        Map<String,String> maps = new LinkedHashMap<>();
        //所有匿名用户均可以访问到Controller层的该方法下
        maps.put("/login/**","anon");
        maps.put("/register/**","anon");
        maps.put("/ui/**","anon");
        maps.put("/libs/**","anon");
        maps.put("/layer/**","anon");
        maps.put("/bootstrap/**","anon");
        maps.put("/AdminLTE/**","anon");
        maps.put("/Ionicons/","anon");
        //配置登出
        maps.put("/login/logout","logout");
        //authc：经过认证才能访问 anon：游客
        maps.put("/**","authc");//经过认证才可以访问
        //设置登录界面
        bean.setLoginUrl("/login/toLogin");
        //设置未授权页面
        //自定义拦截器,shiro的权限验证过滤器，anno：放行 游客模式、logout：注销、authc:需要验证、以上操作配置URL的映射
        Map<String,Filter>  filterMap = new HashedMap();
        bean.setFilters(filterMap);
        bean.setFilterChainDefinitionMap(maps);
        System.out.println("从ShiroConfinguation返回了要");
        return bean;
    }
    @Bean//
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    /**
     * cacheManager：缓存redis实现
     * 使用的是shiro-redis开源插件
     * @return
     */
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**配置shiro redisManageer
     * 使用的是shiro-redis开源插件
     * @return
     */

    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("1.0.0.10");
        redisManager.setPort(6379);
        redisManager.setExpire(1800);
        redisManager.setTimeout(0);
        return redisManager;
    }
    /**session manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }
    /**redisSessionDao shiro sessionDao 层的实现通过redis
     *
     */
    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
}
