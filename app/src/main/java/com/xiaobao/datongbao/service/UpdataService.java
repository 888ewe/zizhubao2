package com.xiaobao.datongbao.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.xiaobao.datongbao.util.HttpUtil;
import com.xiaobao.datongbao.util.URL;

import java.io.File;


/**
 * Created by song on 2016/10/20.
 * 作者:沉默
 * QQ:823925783
 */
public class UpdataService extends Service {

    Context context;
    /**
     * 安卓系统下载类
     **/
    public DownloadManager manager;

    public UpdataService() {
        super();
    }

    public UpdataService(Context context) {
        this.context = context;
        this.manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 接收下载完的广播
     **/
    public  DownloadCompleteReceiver receiver;

    /**
     * 初始化下载器
     **/
    public void initDownManager() {

//        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        receiver = new DownloadCompleteReceiver();

        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(
                Uri.parse(URL.DOWNLOAD));

        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);

        // 下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        // 显示下载界面
        down.setVisibleInDownloadsUi(true);

        // 设置下载后文件存放的位置
        down.setDestinationInExternalFilesDir(context,
                Environment.DIRECTORY_DOWNLOADS, "datongbao.apk");

        // 将下载请求放入队列
        manager.enqueue(down);

        //注册下载广播
        context.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 调用下载
        initDownManager();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {

        // 注销下载广播
        if (receiver != null)
            unregisterReceiver(receiver);

        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                //获取下载的文件id
                long downId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                //自动安装apk
                installAPK(manager.getUriForDownloadedFile(downId));

                //停止服务并关闭广播
                UpdataService.this.stopSelf();

            }
        }

        /**
         * 安装apk文件
         */
        private void installAPK(Uri apk) {

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String filePath= HttpUtil.getRealFilePath(context, apk);
            i.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");// File.toString()会返回路径信息
            //            android.os.Process.killProcess(android.os.Process.myPid());
            // 如果不加上这句的话在apk安装完成之后点击单开会崩溃(目前没有出现崩溃情况)
            try {
                context.startActivity(i);
            }catch (Exception e){
            }
        }

    }
}
