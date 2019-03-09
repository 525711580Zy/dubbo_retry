package com.zy.log;

import java.lang.annotation.*;

//JDK的三大元注解
// 表示该注解用于什么地方，可能的值在枚举类 ElemenetType
@Target({ElementType.METHOD,ElementType.TYPE})
//表示在什么级别保存该注解信息。可选的参数值在枚举类型 RetentionPolicy 中
@Retention(RetentionPolicy.RUNTIME)
//将此注解包含在 javadoc 中 ，它代表着此注解会被javadoc工具提取成文档
@Documented
public @interface LogInfo {
   //对应模块
    String title() default "";
    //对应功能
    String action() default "";
}
