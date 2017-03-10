package com.xiaobao.datongbao;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.squareup.okhttp.Request;
import com.tandong.sa.system.SystemInfo;
import com.xiaobao.datongbao.activity.LoginActivity;
import com.xiaobao.datongbao.fragment.TabIndexFragment;
import com.xiaobao.datongbao.fragment.TabMineFragment;
import com.xiaobao.datongbao.util.MoreUtil;
import com.xiaobao.datongbao.util.NetUtils;
import com.xiaobao.datongbao.util.PublicData;
import com.xiaobao.datongbao.util.SPUtils;
import com.xiaobao.datongbao.util.URL;
import com.xiaobao.datongbao.util.UpdateManger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaobao.datongbao.util.SPUtils.get;

public class MainActivity extends FragmentActivity {
    FragmentManager fm;
    TabIndexFragment tabIndexFragment;
    TabMineFragment tabMineFragment;
    @Bind(R.id.view_index)
    RelativeLayout viewIndex;
    @Bind(R.id.view_mine)
    RelativeLayout viewMine;
    @Bind(R.id.img_tab_one)
    ImageView imgTabOne;
    @Bind(R.id.img_tab_two)
    ImageView imgTabTwo;
    private UpdateManger mUpdateManger;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    private boolean isUpdateLog = false;


    public static LinearLayout view_nav;
    public static LinearLayout view_root;
    public static FrameLayout fl;


    Fragment fragment;


    class  MainActivityReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int flag=intent.getIntExtra("flag",0);
            if(flag==0){
                view_root.removeView(view_nav);
            }else{
                view_root.removeView(view_nav);
                view_root.addView(view_nav);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 2017:
                Fragment f=fm.findFragmentByTag("tabIndexFragment");
                f.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(new MainActivityReceiver(),new IntentFilter("MainActivityReceiver"));


        view_nav= (LinearLayout) findViewById(R.id.view_nav);
        fl= (FrameLayout) findViewById(R.id.fl);
        view_root= (LinearLayout) findViewById(R.id.view_root);


        MyApp.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initData();
        initAlise();
        //权限
        getPersimmions();

    }

    private void initData() {
        PublicData.id = (int) get(MainActivity.this, "uid", 0);
        PublicData.Key = (String) get(MainActivity.this, "Key", "");
        PublicData.token = (String) get(MainActivity.this, "encodeToken", "");
        PublicData.user_phone = (String) SPUtils.get(MainActivity.this, "tel", "");
    }

    private void initAlise() {
        fm =getSupportFragmentManager();
        tabIndexFragment = new TabIndexFragment();
        tabMineFragment = new TabMineFragment();
        viewIndex.performClick();

        //更新apk
        mUpdateManger = new UpdateManger(MainActivity.this);// 注意此处不能传入getApplicationContext();会报错，因为只有是一个Activity才可以添加窗体


        if(!MyApp.updata_app_flag){
            mUpdateManger.checkUpdateInfo();
            MyApp.updata_app_flag=true;
        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            //相机权限
            if (checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            /*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }else {
                //百度定位
                baiduLocation();
            }

        }
    }

    //百度定位
    private void baiduLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        if(isUpdateLog=false) {
            mLocationClient.start();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location.getLatitude() != 0) {
                String latitude = location.getLatitude() + "";
                String lontitude = location.getLongitude() + "";
                String addr = location.getAddrStr();

                SPUtils.put(MainActivity.this, "latitude", latitude);
                SPUtils.put(MainActivity.this, "lontitude", lontitude);
                SPUtils.put(MainActivity.this, "addr", addr);

                Log.e("Logs", "latitude" + latitude);//纬度
                Log.e("Logs", "lontitude" + lontitude);
                Log.e("Logs", "addr" + addr);

                if (isUpdateLog == false) {
                    //上传日志
                    updateLogs(latitude, lontitude, addr);
                }
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.e("Connect", "Connect" + s + "\nConnect" + i);
        }
    }

    private void updateLogs(String latitude, String lontitude, String addr) {
        int Goodsid;//设备ID
        String Notes;//备注
        String phone = (String) get(MainActivity.this, "tel", "0");
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
        //获得IMEI
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
        String Longitude = latitude;//经度
        String Latitude = lontitude;//纬度
        String PositionDescription = addr;//位置描述

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
                        isUpdateLog = true;
                        mLocationClient.stop();
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == SDK_PERMISSION_REQUEST) {
            //百度定位
            baiduLocation();
        }


    }

    @OnClick({R.id.view_index, R.id.view_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_index:

                imgTabOne.setImageResource(R.mipmap.icon_tab_index_ed);
                imgTabTwo.setImageResource(R.mipmap.icon_tab_mine);

                FragmentTransaction transaction1 = fm.beginTransaction();
                if (!tabIndexFragment.isAdded()) {
                    transaction1.add(R.id.fl, tabIndexFragment, "tabIndexFragment");
                }

                transaction1.show(tabIndexFragment);
                transaction1.hide(tabMineFragment);
                transaction1.commit();

                fragment=tabIndexFragment;

                break;
            case R.id.view_mine:

                if(PublicData.id==0){
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                imgTabOne.setImageResource(R.mipmap.icon_tab_index);
                imgTabTwo.setImageResource(R.mipmap.icon_tab_mine_ed);

                FragmentTransaction transaction2 = fm.beginTransaction();
                if (!tabMineFragment.isAdded()) {
                    transaction2.add(R.id.fl, tabMineFragment, "tabMineFragment");
                }

                transaction2.show(tabMineFragment);
                transaction2.hide(tabIndexFragment);
                transaction2.commit();
                fragment=tabMineFragment;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }


    //点2次返回键退出应用
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(fragment instanceof TabIndexFragment){
            ((TabIndexFragment) fragment).onKeyDown(keyCode,event);
        }else if(fragment instanceof TabMineFragment){
            ((TabMineFragment) fragment).onKeyDown(keyCode,event);
        }

        return true;
    }
}
