package cn.lijilong.zauth.util;

import cn.hutool.crypto.SecureUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String sign(String appid, String appSecret, String nonce, String time){
        String content = appid + ":" + nonce + ":" + time + ":" + appSecret;
        String sign = SecureUtil.md5(content);
        return sign;
    }

}
