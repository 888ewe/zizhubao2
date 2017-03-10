package com.xiaobao.datongbao.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.squareup.okhttp.Request;
import com.tandong.sa.json.Gson;
import com.xiaobao.datongbao.MyApp;
import com.xiaobao.datongbao.R;
import com.xiaobao.datongbao.activity.AboutUsActivity;
import com.xiaobao.datongbao.activity.LoginActivity;
import com.xiaobao.datongbao.activity.OrderActivity;
import com.xiaobao.datongbao.activity.UserHeaderActivity;
import com.xiaobao.datongbao.bean.UserInfoBean;
import com.xiaobao.datongbao.util.MoreUtil;
import com.xiaobao.datongbao.util.PublicData;
import com.xiaobao.datongbao.util.SPUtils;
import com.xiaobao.datongbao.util.URL;
import com.xiaobao.datongbao.view.GlideCircleTransform;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pan on 2017/2/15.
 */
public class TabMineFragment extends Fragment {
    View rootView;
    @Bind(R.id.img_exit_login)
    ImageView imgExitLogin;
    @Bind(R.id.img_user_head)
    ImageView imgUserHead;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.view_user_info)
    LinearLayout viewUserInfo;
    @Bind(R.id.view_my_order)
    RelativeLayout viewMyOrder;
    @Bind(R.id.view_about_app)
    RelativeLayout viewAboutApp;
    @Bind(R.id.tv_code)
    TextView tvCode;
    @Bind(R.id.view_check_updata_app)
    RelativeLayout viewCheckUpdataApp;

    long exitTime=0;

    public  boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    MyApp.getInstance().exit();
                }
            }

        return true;
    }



    private void OutLogin() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("退出当前帐号");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // Toast.makeText(getActivity(), "确定", Toast.LENGTH_SHORT).show();
                PublicData.id=0;
                SPUtils.put(getActivity(), "uid", 0);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_mine, null);
        ButterKnife.bind(this, rootView);
        initAlise();
        initData();
        return rootView;
    }

    private void initAlise() {

    }

    private void initData() {
        tvCode.setText("v"+MoreUtil.getAppCurrentVersion(getActivity()));
        //获取头像

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserHeadImg();
    }

    private void getUserHeadImg() {
        OkHttpUtils
                .get()
                .url(URL.USER_INFO + PublicData.id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "userinfo失败" + request.toString());
                    }
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG", "userinfo成功" + response.toString());
                        UserInfoBean bean = new Gson().fromJson(response, UserInfoBean.class);
                        tvNickname.setText(bean.getData().getInfo().getNickName()); 
                        //用户头像
                        if ("".equals(bean.getData().getInfo().getHeadPhoto())) {
                            imgUserHead.setImageResource(R.drawable.header);
                        } else {
                            RequestManager glideRequest = Glide.with(getActivity());
                            glideRequest.load(bean.getData().getInfo().getHeadPhoto())
                                    .transform(new GlideCircleTransform(getActivity()))
                                    .error(R.drawable.header).into(imgUserHead);
                        }
                        //用户性别
                        boolean sex1 = bean.getData().getInfo().isSex();
                        //男是false
                        if(sex1==false) {
                            tvSex.setText("男");
                        }else {
                            tvSex.setText("女");
                        }
                    }
                });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.img_exit_login, R.id.view_user_info, R.id.view_my_order, R.id.view_about_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_exit_login:
                OutLogin();
                break;
            case R.id.view_user_info:
                Intent intent = new Intent(getActivity(), UserHeaderActivity.class);
                startActivity(intent);
                break;
            case R.id.view_my_order:
                Intent intent1=new Intent(getActivity(), OrderActivity.class);
                intent1.putExtra("title","我的订单");
                startActivity(intent1);
                break;
            case R.id.view_about_app:
                Intent intent2 = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
