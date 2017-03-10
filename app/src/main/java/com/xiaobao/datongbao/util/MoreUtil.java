package com.xiaobao.datongbao.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Pan on 2017/2/15.
 */
public class MoreUtil {
    //应用版本号
    public static String getAppCurrentVersion(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pi.versionName;
        return  versionName;
    }
}
