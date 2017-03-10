package com.xiaobao.datongbao.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.tandong.sa.json.Gson;
import com.xiaobao.datongbao.bean.ActivationBean;
import com.xiaobao.datongbao.bean.ActivationCardBean;
import com.xiaobao.datongbao.bean.ServiceInfoBean;
import com.xiaobao.datongbao.manager.ScreenShotListenManager;
import com.xiaobao.datongbao.util.AESCrypt;
import com.xiaobao.datongbao.util.HttpUtilsPan;
import com.xiaobao.datongbao.util.Json2Object;
import com.xiaobao.datongbao.util.NetUtils;
import com.xiaobao.datongbao.util.PublicData;
import com.xiaobao.datongbao.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xiaobao.datongbao.util.URL.activationServiceCardUrl;


@SuppressLint("SetJavaScriptEnabled")
public class PhoneServiceActivity extends Activity {

    boolean flag = false;
    boolean keyFlag = false;

    WebView webView;
    final int RESULT_CODE = 2018;
    ServiceInfoBean serviceInfoBean;
    ActivationCardBean activationCardBean;
    NetReceiver mReceiver = new NetReceiver();
    IntentFilter mFilter = new IntentFilter();
    String content;
    String imei;


    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(PhoneServiceActivity.this, "网络异常，请重新进行碎屏检测", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra("result", "0");
            setResult(RESULT_CODE, intent);
            finish();
        }
    };

    class NetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                boolean isConnected = NetUtils.isNetworkConnected(context);
                if (isConnected) {
                } else {
                    Message message = Message.obtain();
                    handler.sendMessage(message);
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);


        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);//防止屏幕截屏
        Intent intent = getIntent();

        ScreenShotListenManager manager = ScreenShotListenManager.newInstance(this);
        manager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {
                    public void onShot(String imagePath) {
                        webView.loadUrl("javascript:warn()");
                    }
                }
        );
        manager.startListen();
        serviceInfoBean = (ServiceInfoBean) intent.getSerializableExtra("serviceInfoBean");
        activationCardBean = (ActivationCardBean) intent.getSerializableExtra("activationCardBean");
        String recognizeeName = serviceInfoBean.getRecognizeeName();
        String telephone = serviceInfoBean.getTelephone();
        String idCard = serviceInfoBean.getIdCard();
        String phoneBrand = serviceInfoBean.getPhoneBrand();
        String phoneModel = serviceInfoBean.getPhoneModel();
        imei=intent.getStringExtra("imei");
        content = recognizeeName + "," + telephone + "," + idCard + "," + phoneBrand + "," + phoneModel + "," + imei+ "," + 100006;
        AESCrypt aes = null;
        try {

            aes = new AESCrypt();
            String encryptingCode = aes.encrypt(content);
            content = URLEncoder.encode(encryptingCode);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setTextZoom(100);
            webView.setWebViewClient(new MyWebViewClient());
            webView.setWebChromeClient(new MyWebChromeClient());
            webView.getSettings().setAppCacheEnabled(false);
            webView.addJavascriptInterface(new JsInterface(PhoneServiceActivity.this, serviceInfoBean, activationCardBean), "jsxb");
            Log.d("content", content);
            webView.loadUrl("http://yj.baosm.com/index/index.html?stapp=1&content=" + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        /**
         * 处理alert弹出框
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
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
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyFlag) {
            return false;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (webView.canGoBack()) {
                    flag = false;//表示按返回键
                    webView.goBack();   //后退
                    return true;    //已处理
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("result", "0");
                    setResult(RESULT_CODE, intent);
                    finish();
                }
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }



    public class JsInterface {
        private Activity context;
        private ServiceInfoBean serviceInfoBean;
        private ProgressDialog progressDialog;
        ActivationCardBean activationCardBean;

        public JsInterface(Activity context, ServiceInfoBean serviceInfoBean, ActivationCardBean activationCardBean) {
            this.context = context;
            this.serviceInfoBean = serviceInfoBean;
            this.activationCardBean = activationCardBean;
        }

        @JavascriptInterface
        public void appBack1() {
            Intent intent = new Intent();
            intent.putExtra("result", "0");
            context.setResult(2018, intent);
            context.finish();
        }

        @JavascriptInterface
        public void appBack2(String str) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("请等待...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            keyFlag = true;
            //激活
            goActivation(str);
        }

        //激活
        private void goActivation(final String str) {
            String token = (String) SPUtils.get(context, "encodeToken", "");
            String Key = (String) SPUtils.get(context, "Key", "");
            /// 用户Id
            long CustomerId = PublicData.id;
            /// 服务卡卡密
            String CardNumber = activationCardBean.getCardNumber() + "";
            final String strs[] = str.split(",");
            //流水号
            String SerialsNumber = strs[1];
            /// 商品Id
            int GoodsId = Integer.parseInt(activationCardBean.getGoodsId());
      //      String GoodsSerialNo = activationCardBean.getImei().trim();

            /// 被保人姓名
            String RecognizeeName = activationCardBean.getRecognizeeName();
            /// 电话
            String Telephone = activationCardBean.getTelephone();
            String uGoodsId = activationCardBean.getuGoodsId();
            /// 身份证
            String IDCard = activationCardBean.getIDCard();

            int source = activationCardBean.getSource();
            List<ActivationBean.CoOrderInfo.CoOrderRecognizee> recognizeelist = new ArrayList<>();
            recognizeelist.add(new ActivationBean.CoOrderInfo.CoOrderRecognizee(RecognizeeName, Telephone, IDCard));
            List<ActivationBean.CoOrderInfo> OrderList = new ArrayList<>();
            OrderList.add(new ActivationBean.CoOrderInfo(GoodsId, NetUtils.getPhoneUniqueCode(context), source, recognizeelist));
            String json = new Gson().toJson(new ActivationBean(CustomerId, CardNumber, SerialsNumber, OrderList, uGoodsId));
            Log.e("json", "json" + json);
            OkHttpUtils
                    .post()
//                .postString()
                    .url(activationServiceCardUrl)
                    .addParams("token", token + "")
                    .addParams("key", Key + "")
//                .content(json4)
                    .addParams("json", json)
                    .build()
                    .connTimeOut(20000)
                    .readTimeOut(20000)
                    .writeTimeOut(20000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.e("TAG", "激活 联网error" + request.toString());
                            Log.e("TAG", "联网error" + e.getMessage());
                            progressDialog.setCancelable(true);
                            progressDialog.cancel();
                            flag = true;
                            keyFlag = false;
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.e("TAG", " 激活 联网成功" + response.toString());
                            progressDialog.setCancelable(true);
                            progressDialog.cancel();

                            keyFlag = false;
                            if (!flag) {
                                flag = true;
                                try {
                                    Map<String, String> map = Json2Object.jsonToObject(response);
                                    map.put("Status", map.get("Status"));
                                    map.put("Msg", map.get("Msg"));
                                    if (!"6".equals(map.get("Status"))) {
                                        Toast.makeText(context, map.get("Msg"), Toast.LENGTH_SHORT).show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("服务卡激活成功，正在等待生成保单");
                                        builder.setCancelable(false);// 设置点击屏幕Dialog不消失
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.d("zuo", str);
                                                Intent intent = new Intent();
                                                intent.putExtra("result", "1");
                                                String jsonStr = "";
                                                JSONObject json = new JSONObject();
                                                try {
                                                    json.put("EncryptedData", strs[0]);
                                                    json.put("SerialsNumber", strs[1]);
                                                    json.put("IMEI", imei);
                                                    json.put("PictureCode", strs[2]);
                                                    json.put("RecognizeeName", serviceInfoBean.getRecognizeeName());
                                                    json.put("Telephone", serviceInfoBean.getTelephone());
                                                    json.put("IDCard", serviceInfoBean.getIdCard());
                                                    json.put("Brand", serviceInfoBean.getPhoneBrand());
                                                    json.put("BrandType", serviceInfoBean.getPhoneModel());
                                                    jsonStr = json.toString();
                                                    intent.putExtra("jsonStr", jsonStr);
                                                    context.setResult(2017, intent);
                                                    context.finish();
                                                } catch (JSONException e) {
                                                    Log.d("zuo", "==" + e.getMessage());

                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Message message = Message.obtain();
                                                            message.obj = sendException();
                                                            handler.sendMessage(message);
                                                        }
                                                    }).start();
                                                }
                                                context.finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                //强制销毁
                Intent intent = new Intent();
                intent.putExtra("result", "0");
                context.setResult(2017, intent);
                context.finish();
            }
        };

        public String sendException() {
            //参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("", "");
            //服务器请求路径
            String strUrlPath = "http://yanji.baosm.com/insurance/CheckOrder";
            String strResult = HttpUtilsPan.submitPostData(strUrlPath, params, "utf-8");
            return strResult;
        }

    }
}




