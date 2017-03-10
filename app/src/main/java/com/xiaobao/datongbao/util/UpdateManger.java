package com.xiaobao.datongbao.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.xiaobao.datongbao.service.UpdataService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

/**
 * Created by song on 2016/10/20.
 * 作者:沉默
 * QQ:823925783
 */
public class UpdateManger {
    // 应用程序Context
    private Context mContext;
    // 提示消息
    private String updateMsg = "有最新的软件包，请选择下载！";
    private Dialog noticeDialog;// 提示有软件更新的对话框
    UpdataService updataService;
    String newVersion;

    public UpdateManger(Context context) {
        this.mContext = context;
        updataService=new UpdataService(context);
    }

    // 显示更新程序对话框，供主程序调用
    public void checkUpdateInfo() {

        OkHttpUtils
                .get()
                .url(URL.APK_NUMBER)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("download", "apk版本号error" + request);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("download", "apk版本号成功" + response.toString());
                        Map<String, String> map = null;
                        try {
                            map = Json2Object.jsonToObject(response);
                            map.put("newestVersionAndroid", map.get("newestVersionAndroid"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        newVersion = map.get("newestVersionAndroid");
                        try {
                            PackageManager pm = mContext.getPackageManager();//得到PackageManager对象
                            PackageInfo pi = null;//得到PackageInfo对象，封装了一些软件包的信息在里面
                            pi = pm.getPackageInfo(mContext.getPackageName(), 0);
                            int version = pi.versionCode;//获取清单文件中versionCode节点的值
                            Log.e("Version", version + "");

                            if(newVersion==null) {

                            }else {
                                int newV = Integer.parseInt(newVersion);
                                Log.e("Version", newV + "");
                                if (version < newV) {
                                    showNoticeDialog();
                                } else {
                                    Log.e("TAG", "不需要升级");
                                }
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showNoticeDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                mContext);// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               SPUtils.put(mContext, "updateApk1", true);
                updataService.initDownManager();
                dialog.dismiss();// 当取消对话框后进行操作一定的代码？取消对话框
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SPUtils.put(mContext, "updateApk1", true);
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }
}
