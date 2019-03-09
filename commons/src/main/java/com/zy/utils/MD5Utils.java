package com.zy.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    /**利用MD5进行加密
     　　* @param str  待加密的字符串
     　　* @return  加密后的字符串
     　　* @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     　　 * @throws UnsupportedEncodingException
     　　*/
public static String EncoderByMd5(String str) {
            //确定计算方法
    MessageDigest md5= null;
    try {
        md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    md5.update(str.getBytes());
           //加密后的字符串
           return new BigInteger(1, md5.digest()).toString(16);
    }
}
