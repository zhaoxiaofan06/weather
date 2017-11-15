package com.xiaofan.www.weather.common;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by think on 2017/11/7.
 */

public class Common {
    public static String getSignature(HashMap params, String secret) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map sortedParams = new TreeMap(params);
        Set<Map.Entry> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起

        StringBuilder baseString = new StringBuilder();
        for (Map.Entry param : entrys) {
            //sign参数 和 空值参数 不加入算法
            if(param.getValue()!=null && !"".equals(param.getKey().toString().trim()) && !"sign".equals(param.getKey().toString().trim()) && !"".equals(param.getValue().toString().trim())) {
                baseString.append(param.getKey().toString().trim()).append("=").append(param.getValue().toString().trim()).append("&");
            }
        }
        if(baseString.length() > 0 ) {
            baseString.deleteCharAt(baseString.length() - 1).append(secret);
        }

        // 使用MD5对待签名串求签
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(baseString.toString().getBytes("UTF-8"));
            return Base64Encoder.encode(bytes);
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }
    }
}
