//package com.xiaobao.datongbao.manager;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.webkit.JavascriptInterface;
//import android.widget.Toast;
//
//import com.squareup.okhttp.Request;
//import com.tandong.sa.json.Gson;
//import com.xiaobao.datongbao.bean.ActivationBean;
//import com.xiaobao.datongbao.bean.ActivationCardBean;
//import com.xiaobao.datongbao.bean.ServiceInfoBean;
//import com.xiaobao.datongbao.util.HttpUtilsPan;
//import com.xiaobao.datongbao.util.Json2Object;
//import com.xiaobao.datongbao.util.PublicData;
//import com.xiaobao.datongbao.util.SPUtils;
//import com.xiaobao.datongbao.util.URL;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Pan on 2016/11/27.
// */
//public class JsInterface {
//
//    private Activity context;
//    private ServiceInfoBean serviceInfoBean;
//    ActivationCardBean activationCardBean;
//    private ProgressDialog progressDialog;
//
////    ProgressBar progressBar;
//
//    public JsInterface(Activity context, ServiceInfoBean serviceInfoBean, ActivationCardBean activationCardBean) {
//        this.context = context;
//        this.serviceInfoBean = serviceInfoBean;
//        this.activationCardBean = activationCardBean;
//    }
//
//    @JavascriptInterface
//    public void appBack1() {
//        Intent intent = new Intent();
//        intent.putExtra("result", "0");
//        context.setResult(2017, intent);
//        context.finish();
//    }
//
//    @JavascriptInterface
//    public void appBack2(String str) {
//
//         progressDialog=new ProgressDialog(context);
//        progressDialog.setMessage("请等待...");
//        progressDialog.show();
//
////        progressBar=new ProgressBar(context);
////        progressBar.setVisibility(View.VISIBLE);
//        //激活
//        goActivation(str);
//    }
//
//    //激活
//    private void goActivation(final String str) {
//        String token = (String) SPUtils.get(context, "encodeToken", "");
//        String Key = (String) SPUtils.get(context, "Key", "");
//        /// 用户Id
//        long CustomerId = PublicData.id;
//        /// 服务卡卡密
//        String CardNumber = activationCardBean.getCardNumber() + "";
//        final String strs[] = str.split(",");
//        //流水号
//        String SerialsNumber = strs[1];
//        /// 商品Id
//        int GoodsId = Integer.parseInt(activationCardBean.getGoodsId());
//
//
//        /// 被保人姓名
//        String RecognizeeName = activationCardBean.getRecognizeeName();
//        /// 电话
//        String Telephone = activationCardBean.getTelephone();
//        /// 身份证
//        String IDCard = activationCardBean.getIDCard();
//
//        int  source=activationCardBean.getSource();
//        List<ActivationBean.CoOrderInfo.CoOrderRecognizee> recognizeelist = new ArrayList<>();
//        recognizeelist.add(new ActivationBean.CoOrderInfo.CoOrderRecognizee(RecognizeeName, Telephone, IDCard));
//        List<ActivationBean.CoOrderInfo> OrderList = new ArrayList<>();
//        OrderList.add(new ActivationBean.CoOrderInfo(GoodsId, getPhoneImei(), source, recognizeelist));
//        String json = new Gson().toJson(new ActivationBean(CustomerId, CardNumber, SerialsNumber, OrderList));
//        Log.e("json", "json" + json);
//        OkHttpUtils
//                .post()
////                .postString()
//                .url(URL.activationServiceCardUrl)
//                .addParams("token", token + "")
//                .addParams("key", Key + "")
////                .content(json4)
//                .addParams("json", json)
//                .build()
//                .connTimeOut(50000)
//                .readTimeOut(50000)
//                .writeTimeOut(10000)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        Log.e("TAG", "激活 联网error" + request.toString());
//                        Log.e("TAG", "联网error" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
////                        progressBar.setVisibility(View.GONE);
//                        progressDialog.cancel();
//                        Log.e("TAG", " 激活 联网成功" + response.toString());
//                        try {
//                            Map<String, String> map = Json2Object.jsonToObject(response);
//                            map.put("Status", map.get("Status"));
//                            map.put("Msg", map.get("Msg"));
//                            if (!"6".equals(map.get("Status"))) {
//                                Toast.makeText(context, map.get("Msg"), Toast.LENGTH_SHORT).show();
//                            } else {
//                                Log.e("Tag", "Msg" + map.get("Msg"));
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                builder.setMessage("服务卡激活成功，正在等待生成保单");
//                                builder.setCancelable(false);// 设置点击屏幕Dialog不消失
//                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Log.d("zuo", str);
//                                        Intent intent = new Intent();
//                                        intent.putExtra("result", "1");
//                                        String imei = getPhoneImei();
//                                        String jsonStr = "";
//                                        JSONObject json = new JSONObject();
//                                        try {
//                                            json.put("EncryptedData", strs[0]);
//                                            json.put("SerialsNumber", strs[1]);
//                                            json.put("IMEI", imei);
//                                            json.put("PictureCode", strs[2]);
//                                            json.put("RecognizeeName", serviceInfoBean.getRecognizeeName());
//                                            json.put("Telephone", serviceInfoBean.getTelephone());
//                                            json.put("IDCard", serviceInfoBean.getIdCard());
//                                            json.put("Brand", serviceInfoBean.getPhoneBrand());
//                                            json.put("BrandType", serviceInfoBean.getPhoneModel());
//                                            jsonStr = json.toString();
//                                            intent.putExtra("jsonStr", jsonStr);
//                                            context.setResult(2017, intent);
//                                            context.finish();
//                                        } catch (JSONException e) {
//                                            Log.d("zuo", "==" + e.getMessage());
//
//                                            new Thread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    Message message = Message.obtain();
//                                                    message.obj = sendException();
//                                                    handler.sendMessage(message);
//                                                }
//                                            }).start();
//                                        }
//                                        context.finish();
//                                    }
//                                });
//                                builder.show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            String result = (String) msg.obj;
//            //强制销毁
//            Intent intent = new Intent();
//            intent.putExtra("result", "0");
//            context.setResult(2017, intent);
//            context.finish();
//
//        }
//    };
//
//
//    public String sendException() {
//        //参数
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("", "");
//        //服务器请求路径
//        String strUrlPath = "http://yanji.baosm.com/insurance/CheckOrder";
//        String strResult = HttpUtilsPan.submitPostData(strUrlPath, params, "utf-8");
//        return strResult;
//    }
//
//    //得到手机Imei
//    public String getPhoneImei() {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        return tm.getDeviceId();
//    }
//}
