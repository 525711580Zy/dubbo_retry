package com.zy.serviceImpl;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

public class TemplatesUtil {
    /**
     * @param  dataMap 渲染数据源
     * @param  TemplatesName 模板名
     * @param  templateEngine 模板操作类
     * @return
     */
    public static String createTemplates(Map<String,Object> dataMap, String TemplatesName, TemplateEngine templateEngine){
        //context 对象用于注入要在模板上渲染的信息
        Context context = new Context();
        context.setVariables(dataMap);
        //emailTemplates为短信模板名称
        String emailText = templateEngine.process("emailTemplates",context);
        return emailText;
    }
}
