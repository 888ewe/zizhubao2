package com.xiaobao.datongbao.util;

import java.net.URLEncoder;

/**
 * Created by song on 2017/2/15.
 * 作者:沉默
 * QQ:823925783
 * token加密
 */

public class EncodeTrans {
    //加密
    public static String EncodeTrans(String token2) {
        String token4 = null;
        try {
            String token3 = URLEncoder.encode(token2, "UTF-8");
            byte[] srtbyte = token3.getBytes();
            token4 = Base64.encode(srtbyte, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token4;
    }
}
