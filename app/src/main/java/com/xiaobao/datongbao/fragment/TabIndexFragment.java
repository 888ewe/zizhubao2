package com.xiaobao.datongbao.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.tandong.sa.json.Gson;
import com.tandong.sa.system.SystemInfo;
import com.xiaobao.datongbao.MyApp;
import com.xiaobao.datongbao.R;
import com.xiaobao.datongbao.activity.LoginActivity;
import com.xiaobao.datongbao.activity.OrderActivity;
import com.xiaobao.datongbao.bean.ActivationCardBean;
import com.xiaobao.datongbao.bean.SearchGoodsBean;
import com.xiaobao.datongbao.bean.ServiceInfoBean;
import com.xiaobao.datongbao.manager.PhoneService;
import com.xiaobao.datongbao.util.AESCrypt;
import com.xiaobao.datongbao.util.PublicData;
import com.xiaobao.datongbao.util.URL;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Pan on 2017/2/15.
 */
public class TabIndexFragment extends Fragment {
     WebView webView;

    int brandId=0;
    int innerId=0;


    View rootView;
    private long exitTime = 0;

    ProgressDialog progressDialog;







    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getCurrentPhone();

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_tan_index,null);


        webView= (WebView) rootView.findViewById(R.id.webView);
        initAlise();
        initData();
        return rootView;
    }




    private void initData() {
        getCurrentPhone();

    }

    private void initAlise() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.addJavascriptInterface(this, "jsxb");
    }





    private class MyWebViewClient extends WebViewClient {



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);

            Log.d("uu",url);

            if(url.endsWith("php")){
                Intent intent=new Intent("MainActivityReceiver");
                intent.putExtra("flag",1);
                getActivity().sendBroadcast(intent);
            }else{
                Intent intent=new Intent("MainActivityReceiver");
                intent.putExtra("flag",0);
                getActivity().sendBroadcast(intent);
            }

            progressDialog.dismiss();

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }


    }


    @JavascriptInterface
    public void appInfo(String str){

        Log.d("appinfos",str);

        ServiceInfoBean serviceInfoBean=new ServiceInfoBean();

        String params[]=str.split(",");

        serviceInfoBean.setPhoneBrand(params[0]);
        serviceInfoBean.setPhoneModel(params[1]);
        serviceInfoBean.setIdCard(params[5]);
        serviceInfoBean.setTelephone(params[4]);
        serviceInfoBean.setRecognizeeName(params[3]);


        Log.d("appinfos0",str);



        ActivationCardBean activationCardBean = new ActivationCardBean();

        /// 服务卡卡密
        activationCardBean.setCardNumber(params[2]);

        /// 商品Id
        activationCardBean.setGoodsId(params[1]);

        /// 被保人姓名
        activationCardBean.setRecognizeeName(params[3]);
        /// 电话
        activationCardBean.setTelephone(params[4]);

        activationCardBean.setIDCard(params[5]);

        //小保Source
        activationCardBean.setSource(14);

        Log.d("appinfos1",str);


        PhoneService phoneService=new PhoneService(getActivity(),1,serviceInfoBean,activationCardBean);
        phoneService.requestService();

        Log.d("appinfos2",str);


    }


    @JavascriptInterface
    public String appPei(){

        if(PublicData.id!=0){
            Intent intent=new Intent(getActivity(), OrderActivity.class);
            intent.putExtra("state",8);
            intent.putExtra("title","理赔");
            startActivity(intent);
            return "";
        }else{
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return "";
        }



    }







    @JavascriptInterface
    public String appBao(){
        if(PublicData.id!=0){

            String content=brandId+","+innerId+","+PublicData.id+","+getPhoneImei()+","+100006+","+10207+","+10208;

            Log.d("xcv",content);

            try {
                AESCrypt aes= new AESCrypt();
                String encryptingCode = aes.encrypt(content);
                content= URLEncoder.encode(encryptingCode);


                Log.d("sh",content.substring(0,content.length()-3));
                Log.d("sh",content.replace("%0A",""));

                progressDialog=ProgressDialog.show(getActivity(),null,"页面加载中，请稍候");

                return content.replace("%0A","");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return "";
        }
        return "";
    }




    private class MyWebChromeClient extends WebChromeClient {
        /**
         * 处理alert弹出框
         */
        @Override public boolean onJsAlert(WebView view,String url,String message,JsResult result){
            return super.onJsAlert(view,url,message,result);
        }

    }


    public String getPhoneImei() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }








    //得到当前手机的信息
    private void getCurrentPhone() {
        SystemInfo systemInfo = new SystemInfo(getActivity());
        //手机型号
        final String key = systemInfo.getDeviceName();
        //手机品牌
        String carrier = Build.MANUFACTURER;
        Log.e("key",carrier+" "+key);
        OkHttpUtils
                .get()
                .url(URL.MATCH_PHONE)
                .addParams("categoryid", 1 + "")
                .addParams("page", 1 + "")
                .addParams("size", 10 + "")
                .addParams("word", ""+key)
//                .addParams("word", "snnnn7")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.d("MATCH_PHONE", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response) {
                        Log.d("MATCH_PHONE", response);
                        SearchGoodsBean vo = new Gson().fromJson(response, SearchGoodsBean.class);
                         List<SearchGoodsBean.DataBean.ListBean> list = vo.getData().getList();

                        brandId=list.get(0).getBrandId();
                        innerId=list.get(0).getInnerId();

                        Log.d("phonee",brandId+"=="+innerId);

                        webView.loadUrl(URL.BAO_H5);
                        progressDialog=ProgressDialog.show(getActivity(),null,"页面加载中，请稍候");

                     }

                });
    }


    public  boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (webView.canGoBack()) {  //表示按返回键
                webView.goBack();   //后退
                return true;    //已处理
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    MyApp.getInstance().exit();
                }
            }
        }
        return true;
    }
}
