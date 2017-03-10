package com.xiaobao.datongbao.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Random;

public class NetUtils {


	//得到随机字符串
	public static String getRandomString(int length){
		String str="123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < length; ++i){
			int number = random.nextInt(8);//[0,62)
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	// 判断网络连接状态
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}


	//获取设备唯一码
	public static String getPhoneUniqueCode(Context context){
		UniqueCode uniqueCode=new UniqueCode(context);

//		String uniqueCodeStr=uniqueCode.getUniqueCode();
//
//		if(uniqueCodeStr.contains("\n")){
//			String uniqueCodes[] =uniqueCodeStr.split("\n");
//			return uniqueCodes[1];
//		}else{
//			return uniqueCodeStr;
//		}

		return getRandomString(15);
	}
}
