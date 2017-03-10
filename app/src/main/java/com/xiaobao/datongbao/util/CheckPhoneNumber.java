package com.xiaobao.datongbao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by song on 2017/2/15.
 * 作者:沉默
 * QQ:823925783
 */

public class CheckPhoneNumber {
    //判断手机号格式
    public static boolean isChinaPhoneNum(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
