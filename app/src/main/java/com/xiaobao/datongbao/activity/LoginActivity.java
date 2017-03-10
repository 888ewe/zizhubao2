package com.xiaobao.datongbao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.tandong.sa.json.Gson;
import com.tandong.sa.system.SystemInfo;
import com.xiaobao.datongbao.MainActivity;
import com.xiaobao.datongbao.R;
import com.xiaobao.datongbao.bean.LoginBean;
import com.xiaobao.datongbao.util.CheckPhoneNumber;
import com.xiaobao.datongbao.util.EncodeTrans;
import com.xiaobao.datongbao.util.MoreUtil;
import com.xiaobao.datongbao.util.NetUtils;
import com.xiaobao.datongbao.util.PublicData;
import com.xiaobao.datongbao.util.SPUtils;
import com.xiaobao.datongbao.util.URL;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_checkcode)
    EditText etCheckcode;
    @Bind(R.id.btn_checkcode)
    Button btnCheckcode;
    @Bind(R.id.linearlayout)
    LinearLayout linearlayout;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.tv_service_xiyi)
    TextView tvServiceXiyi;
    @Bind(R.id.activity_login)
    RelativeLayout activityLogin;
    private int secend;
    private Handler handler = new Handler();
    private String phoneStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        if(PublicData.id!=0) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    @OnClick({R.id.btn_checkcode, R.id.btn_login, R.id.tv_service_xiyi})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击验证码
            case R.id.btn_checkcode:
                phoneStr = etPhone.getText().toString();

                if (!CheckPhoneNumber.isChinaPhoneNum(phoneStr)) {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                //发送验证码按钮
                btnCheckcode.setClickable(false);
                btnCheckcode.setBackgroundColor(Color.parseColor("#ffffff"));
                secend = 60;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        secend--;
                        if (secend <= 60 & secend > 0) {
                            btnCheckcode.setText(secend + "秒后重发");
                            handler.postDelayed(this, 1000);
                            ColorStateList color = getResources().getColorStateList(R.color.lineViewColor);
                            btnCheckcode.setTextColor(color);
                        } else if (secend == 0) {
                            btnCheckcode.setText("发送验证码");
                            btnCheckcode.setClickable(true);
                            btnCheckcode.setBackgroundColor(Color.parseColor("#ffffff"));//F4751E
                            ColorStateList color = getResources().getColorStateList(R.color.mainColor);
                            btnCheckcode.setTextColor(color);
                            handler.removeCallbacks(this);
                        } else {
                        }

                    }
                };
                handler.postDelayed(runnable, 1000);
                //手机号码
                PublicData.user_phone = phoneStr;

                OkHttpUtils
                        .post().url(URL.SEND_CODE)
                        .addParams("tel", phoneStr)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Toast.makeText(LoginActivity.this, "休息一会，程序员去约会了！", Toast.LENGTH_SHORT).show();
                                Log.e("Tag", "request" + request.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.e("TAG", "发送验证码" + response.toString());
                            }
                        });
                break;
            //登录
            case R.id.btn_login:
                phoneStr = etPhone.getText().toString();
                  /*
                验证码
                */
                String codeStr = etCheckcode.getText().toString();
                if (phoneStr.equals("") || phoneStr.length() == 0 || codeStr.equals("") || codeStr.length() == 0) {
                    Toast.makeText(LoginActivity.this, "请输入手机号或验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                int hasOpenREAD_PHONE_STATEPermission = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE);

                if (hasOpenREAD_PHONE_STATEPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                    return;
                }
                denglu();
                break;
            //协议
            case R.id.tv_service_xiyi:
                break;
        }
    }

    private void denglu() {

        OkHttpUtils
                .post().url(URL.USER_LOGIN)
                .addParams("source", "1")
                .addParams("tel", etPhone.getText().toString())
                .addParams("code", etCheckcode.getText().toString())
//                .addParams("invite",etLoginId.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("login", "login error" + request.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("login", "login 成功" + response.toString());
                        LoginBean login = new Gson().fromJson(response, LoginBean.class);
                        if (login.getStatus() == 0) {
                            //登录成功
                            String Signature = login.getData().getToken().getSignature();
                            PublicData.id = login.getData().getToken().getUserID();
                            PublicData.Key = login.getData().getToken().getKey();
                            String token2 = "{\"UserID\":" + PublicData.id + ",\"Key\":\"" + PublicData.Key + "\",\"Signature\":\"" + Signature + "\"}";
                            String encodeToken = EncodeTrans.EncodeTrans(token2);
                            PublicData.token = encodeToken;

                            //Sp存储
                            SPUtils.put(LoginActivity.this, "uid", PublicData.id);
                            SPUtils.put(LoginActivity.this, "Key", PublicData.Key);
                            SPUtils.put(LoginActivity.this, "encodeToken", encodeToken);
                            SPUtils.put(LoginActivity.this, "tel",etPhone.getText().toString()+"");
                            //上传日志
                            updateLogs();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, login.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void updateLogs() {
        int Goodsid;//设备ID
        String Notes;//备注
        String phone = (String) SPUtils.get(LoginActivity.this, "tel", "0");
        String Customer;//会员编号
        String Mobile;//手机号

        if (PublicData.id == 0) {//未登录
            Customer = "0";//会员编号
            Mobile = "0";//手机号
            Goodsid = 0;
        } else {//已登录
            Customer = PublicData.id + "";//会员编号
            Mobile = phone;//手机号
            Goodsid = 0;
        }
        //获得手机品牌型号
        SystemInfo systemInfo = new SystemInfo(this);
        String carrier = Build.MANUFACTURER;//品牌
        String key = systemInfo.getDeviceName();//型号
        //获得app版本号
        String versionName = MoreUtil.getAppCurrentVersion(this);

        //获得android版本
        int currentapiVersion = Build.VERSION.SDK_INT;

        String Imei = NetUtils.getPhoneUniqueCode(this) + "";//设备号
        String Version = versionName;//APP版本号;
        String Longitude = (String) SPUtils.get(LoginActivity.this, "lontitude", "");//经度
        String Latitude = (String) SPUtils.get(LoginActivity.this, "latitude", "");
        ;//纬度
        String PositionDescription = (String) SPUtils.get(LoginActivity.this, "addr", "");//位置描述

        String OS = "android" + currentapiVersion;//操作系统（包含版本号）
        String Brand = carrier;//设备品牌
        String Model = key;//设备型号
        OkHttpUtils
                .get()
//                .url("http://123.56.87.26:8100/api/search/SaveLos")
                .url(URL.UPDATELOGS)
                .addParams("Customer", Customer)
                .addParams("Mobile", Mobile)
                .addParams("Imei", Imei)
                .addParams("Brand", Brand)
                .addParams("Model", Model)
                .addParams("Goodsid", Goodsid + "")
                .addParams("OS", OS)
                .addParams("Version", Version)
                .addParams("Longitude", Longitude)
                .addParams("Latitude", Latitude)
                .addParams("PositionDescription", PositionDescription)
                .addParams("Notes", "0")//备注
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("Logs", "上传日志error:" + request.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("Logs", "上传日志成功:" + response.toString());
                    }
                });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            finish();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);



            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
