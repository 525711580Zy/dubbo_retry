package com.zy.service;

public interface SendMailService {
    //to：代表接收方邮箱地址
    //activeurl：激活链接
    public void sendMail(String to, String activeurl);
}
