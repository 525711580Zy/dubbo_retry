package com.zy.log;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zy.entity.Log;
import com.zy.entity.User;
import com.zy.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    //构建日志对象
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    //构建service对象
    @Reference(version = "1.5")
    private LogService service;
    //声明切点
    @Pointcut(value = "execution(* com.zy.service.*.*(..))||@annotation(com.zy.log.LogInfo)")
    public void logPointCut(){}
    @After("logPointCut()")
    public void doAfter(JoinPoint jp){
        //获取request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        handleLog(jp,request);
    }
    private void handleLog(JoinPoint joinPoint,HttpServletRequest request){
        try{
            //获得注解
            LogInfo controllerLog = getAnnotationLog(joinPoint);
            if(controllerLog==null){
                return;
            }
            //获得方法名称
            String action = controllerLog.action();
            String title = controllerLog.title();
            //获得用户名称
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("userDetail");
            String username = user.getName();
            //实现新增操作
            Log loginfo = new Log(title,new Date(),action,username);
            service.addLog(loginfo);
            //打印日志
            log.info("模块名称：{}",title);
            log.info("操作名称：{}",action);
            log.info("操作员：{}",username);
        }catch (Exception exp){
            //记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}",exp.getMessage());
            exp.printStackTrace();
        }
    }
    private static LogInfo getAnnotationLog(JoinPoint joinPoint)throws Exception{
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if(method!=null){
            return method.getAnnotation(LogInfo.class);
        }
        return null;
    }
}
