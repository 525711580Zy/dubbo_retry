package com.zy.serviceImpl;


import com.alibaba.dubbo.config.annotation.Service;
import com.zy.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service(version = "1.3",interfaceClass = SendMailService.class)
@Component
@Transactional
public class SendMailServiceImpl implements SendMailService {
    static final Lock lock=new ReentrantLock();
    //发送邮件的用户名
    @Value("${spring.mail.username}")
    private String sender;
    //模板引擎对象
    @Autowired
    private TemplateEngine templateEngine;
    //javaMail核心对象
//    @Autowired  //这里改动过，原装的是Autiwired报错，改成了Resource就没问题了
    @Resource
    private JavaMailSender javaMailSender;

    @Override
    @Async//spring异步处理
    public void sendMail(String to,String activeurl) {
        lock.lock();
        //消息处理类
        MimeMessage message = javaMailSender.createMimeMessage();
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("title","用户名激活");
        dataMap.put("url",activeurl);

        //获取生成的模板
        String emailText = TemplatesUtil.createTemplates(dataMap,"emailTemplates.html",templateEngine);

        try {
            //消息处理助手对象
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            //设置发件人
            helper.setFrom(sender);
            //设置收件人
            helper.setTo(to);
            //设置邮件标题
            helper.setSubject("主题：用户名激活");
            //设置邮件内容，true标识发送html格式
            helper.setText(emailText,true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
          javaMailSender.send(message);

          lock.unlock();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
