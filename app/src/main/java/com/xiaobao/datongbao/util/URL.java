package com.xiaobao.datongbao.util;

/**
 * Created by song on 2017/2/15.
 * 作者:沉默
 * QQ:823925783
 */

public class URL {

    //public static String baseUrl="http://123.56.87.26:8100/api/";
    //正式
    public static String baseUrl="http://182.92.213.115:8100/api/";
    //下载apk专用
    public static String baseUrl2="http://182.92.213.115:8100/";

    //激活服务卡
    public static String activationServiceCardUrl=baseUrl+"Insurance/ActivateCardAndPay";
    //上传日志
    public static final String UPDATELOGS =baseUrl+ "search/SaveLogs";
    //下载apk
    public static final String DOWNLOAD =baseUrl2+ "Version/download?platform=2";
    //apk版本号
    public static final String APK_NUMBER =baseUrl2+ "Version/NewestVersion?os=1&platform=2";
    //发送验证码
    public static final String SEND_CODE =baseUrl + "user/SendCode" ;
    //登录
    public static final String USER_LOGIN = baseUrl + "user/Login";
    // 上传头像：
    public static String ADD_HEADER = baseUrl + "user/headphoto";
    //昵称、性别
    public static final String UPDATE_USER = baseUrl + "user/update";
    //用户信息
    public static String USER_INFO = baseUrl + "user/info/";
    //匹配手机
    public static String MATCH_PHONE = baseUrl+"search/Matching";
    //保====H5
    public static String BAO_H5="http://m.baosm.com/oldmobileprotect.php";

}
