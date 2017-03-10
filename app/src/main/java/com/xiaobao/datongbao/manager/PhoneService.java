package com.xiaobao.datongbao.manager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.xiaobao.datongbao.activity.PhoneServiceActivity;
import com.xiaobao.datongbao.bean.ActivationCardBean;
import com.xiaobao.datongbao.bean.ServiceInfoBean;
import com.xiaobao.datongbao.util.HttpUtilsPan;
import com.xiaobao.datongbao.util.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pan on 2016/11/3.
 */
public class PhoneService {
    Activity activity;
    int requestCode;
    ServiceInfoBean serviceInfoBean;
    ActivationCardBean activationCardBean;


    public PhoneService(Activity activity, int requestCode, ServiceInfoBean serviceInfoBean,ActivationCardBean activationCardBean) {
        this.activity = activity;
        this.requestCode = requestCode;
        this.serviceInfoBean = serviceInfoBean;
        this.activationCardBean=activationCardBean;
    }






    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonStr= (String) msg.obj;
            if(!jsonStr.equals("")){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    boolean result=jsonObject.optBoolean("result");
                    if(result){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message= Message.obtain();
                                message.obj=requestNetWork2();
                                handler2.sendMessage(message);
                            }
                        }).start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"文本解析失败[1]", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(activity,"服务器异常[1]", Toast.LENGTH_SHORT).show();
            }

        }
    };


    Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonStr= (String) msg.obj;

            if(!jsonStr.equals("")){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    boolean result=jsonObject.optBoolean("Result");
                    if(!result){
                        Intent intent=new Intent(activity,PhoneServiceActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("serviceInfoBean",serviceInfoBean);

                        bundle.putSerializable("activationCardBean",activationCardBean);

                        intent.putExtra("imei",getPhoneImei());
                        intent.putExtras(bundle);
                        activity.startActivityForResult(intent,requestCode);
                    }else{
                        Toast.makeText(activity,"本设备已经投保", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"文本解析失败[2]", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(activity,"服务器异常[2]", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void requestService() {

        if(activity!=null&&serviceInfoBean!=null){
            if(serviceInfoBean.getTelephone()==null||serviceInfoBean.getRecognizeeName()==null||serviceInfoBean.getIdCard()==null||serviceInfoBean.getPhoneBrand()==null||serviceInfoBean.getPhoneModel()==null){
                Toast.makeText(activity,"进行验机失败【参数信息不能为null】", Toast.LENGTH_SHORT).show();
                return;
            }

            if(serviceInfoBean.getTelephone().trim().length()==0||serviceInfoBean.getRecognizeeName().trim().length()==0||serviceInfoBean.getIdCard().trim().length()==0||serviceInfoBean.getPhoneBrand().trim().length()==0||serviceInfoBean.getPhoneModel().trim().length()==0){
                Toast.makeText(activity,"进行验机失败【参数信息不能为空字符串】", Toast.LENGTH_SHORT).show();
                return;
            }

            if(serviceInfoBean.getTelephone().contains(",")||serviceInfoBean.getRecognizeeName().contains(",")||serviceInfoBean.getIdCard().contains(",")||serviceInfoBean.getPhoneBrand().contains(",")||serviceInfoBean.getPhoneModel().contains(",")){
                Toast.makeText(activity,"进行验机失败【参数信息不能包含\",\"号】", Toast.LENGTH_SHORT).show();
                return;
            }



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ArrayList<String> permissions = new ArrayList<String>();

                //读取电话状态权限
                addPermission(permissions, Manifest.permission.READ_PHONE_STATE);
                // 读写权限
                addPermission(permissions, Manifest.permission.READ_EXTERNAL_STORAGE);


                if (permissions.size() > 0) {
                    ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), 0);
                }else{
                    initAlise();
                }
            }else{
                if(alertMessage1(Manifest.permission.READ_PHONE_STATE)&&alertMessage2(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    initAlise();
                }
            }
        }else{
            Toast.makeText(activity,"进行验机失败【初始化参数异常】", Toast.LENGTH_SHORT).show();
        }
    }



    private void addPermission(ArrayList<String> permissionsList, String permission) {
        if(ContextCompat.checkSelfPermission(activity, permission)!= PackageManager.PERMISSION_GRANTED){
            permissionsList.add(permission);
        }
    }

    private void initAlise() {
        if(getPhoneImei().equals("000000000000000")||getPhoneImei().equals("0")||getPhoneImei().contains("00000000000000")){
            alertImeiError();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message= Message.obtain();
                    message.obj=requestNetWork();
                    handler.sendMessage(message);
                }
            }).start();
        }
    }

    private void alertImeiError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
         builder.setMessage("无法获取本机的有效设备号，暂无法受理投保");
         builder.setTitle("提示");

          builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                   }
              });
          builder.create().show();
    }


    public String requestNetWork(){
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://yanji.baosm.com/Home/GetDeviceCode?DeviceType=1&DeviceCode="+getPhoneImei()+"&ChannelCode=100006");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            String result = streamToString(is);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }




    public String requestNetWork2(){
        //参数
        Map<String,String> params = new HashMap<String,String>();
        params.put("IMEI", getPhoneImei());
        //服务器请求路径
        String strUrlPath = "http://yanji.baosm.com/insurance/CheckOrder";
        String strResult= HttpUtilsPan.submitPostData(strUrlPath,params, "utf-8");
        return strResult;
    }



    public boolean alertMessage1(String permissionStr){
        if(checkPermission(permissionStr)){
            return true;
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示");
            builder.setMessage("您当前未开启读取电话状态权限，是否开启?");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setClassName("com.android.settings","android.permission.READ_PHONE_STATE");
                        intent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
                    }
                    activity.startActivity(intent);
                }
            });
            builder.show();
            return false;
        }
    }
    public boolean alertMessage2(String permissionStr){
        if(checkPermission(permissionStr)){
            return true;
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示");
            builder.setMessage("您当前未开启存储权限，是否开启?");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setClassName("com.android.settings","android.permission.READ_EXTERNAL_STORAGE");
                        intent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
                    }
                    activity.startActivity(intent);
                }
            });
            builder.show();
            return false;
        }
    }

    //得到手机Imei
    public String getPhoneImei() {
        return NetUtils.getPhoneUniqueCode(activity);

    }




    public String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public boolean checkPermission(String permissionStr) {
        int res = activity.checkCallingOrSelfPermission(permissionStr);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


}
