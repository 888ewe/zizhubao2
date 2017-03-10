package com.xiaobao.datongbao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.squareup.okhttp.Request;
import com.tandong.sa.json.Gson;
import com.umeng.analytics.MobclickAgent;
import com.xiaobao.datongbao.R;
import com.xiaobao.datongbao.bean.UserInfoBean;
import com.xiaobao.datongbao.util.CompressPic;
import com.xiaobao.datongbao.util.PublicData;
import com.xiaobao.datongbao.util.SPUtils;
import com.xiaobao.datongbao.util.URL;
import com.xiaobao.datongbao.view.GlideCircleTransform;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;


public class UserHeaderActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.iv_userinfo_head)
    ImageView ivUserinfoHead;
    @Bind(R.id.et_user_nickname)
    EditText etUserNickname;
    @Bind(R.id.tv_user_nickname)
    TextView tvUserNickname;
    @Bind(R.id.rb_boy)
    RadioButton rbBoy;
    @Bind(R.id.rb_girl)
    RadioButton rbGirl;
    @Bind(R.id.btn_ok_commit)
    Button btnOkCommit;
    private File tempFile;
    private int IMAGE_CODE = 10;
    private int CAMERA = 1;
    private boolean sex = false;
    String photo;
    private String nick = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_header);
        ButterKnife.bind(this);
        getUserHeadImg();
        etUserNickname.addTextChangedListener(watcher);
    }

    private TextWatcher watcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            String editable = s.toString();
            nick = editable;
            int len = editable.length();
            if (len >= 6) {
                Toast.makeText(UserHeaderActivity.this, "不能超过5个汉字!", Toast.LENGTH_SHORT).show();
            }
            //设置新光标所在的位置
            Selection.setSelection(etUserNickname.getText(), len);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };


    //用户头像获取
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
                        nick = bean.getData().getInfo().getNickName();
                        //用户头像
                        if ("".equals(bean.getData().getInfo().getHeadPhoto())) {
                            ivUserinfoHead.setImageResource(R.drawable.header);
                        } else {
                            RequestManager glideRequest = Glide.with(UserHeaderActivity.this);
                            glideRequest.load(bean.getData().getInfo().getHeadPhoto())
                                    .transform(new GlideCircleTransform(UserHeaderActivity.this))
                                    .error(R.drawable.header).into(ivUserinfoHead);
                        }
                        //用户性别
                        boolean sex1 = bean.getData().getInfo().isSex();
                        //男是false
                        if (sex1 == false) {//man
                            rbBoy.setChecked(true);
                            rbGirl.setChecked(false);
                        } else if (sex1 == true) {
                            rbBoy.setChecked(false);
                            rbGirl.setChecked(true);
                        } else {
                            rbBoy.setChecked(true);
                            rbGirl.setChecked(false);
                        }

                        //昵称
                        if ("".equals(bean.getData().getInfo().getNickName())) {
                            tvUserNickname.setVisibility(View.GONE);
                            etUserNickname.setVisibility(View.VISIBLE);
                        } else {
                            tvUserNickname.setText(bean.getData().getInfo().getNickName());
                        }

                    }
                });
    }

    //提交信息
    private void commitInfo() {
        String key = (String) SPUtils.get(UserHeaderActivity.this, "key", "");
        String token = (String) SPUtils.get(UserHeaderActivity.this, "encodeToken", "");
        if (etUserNickname.getText().toString() == null || "".equals(etUserNickname.getText().toString())) {
            nick = nick;
        } else {
            nick = etUserNickname.getText().toString();
            tvUserNickname.setText(nick);
        }
        OkHttpUtils
                .post()
                .url(URL.UPDATE_USER)
                .addParams("key", key)
                .addParams("token", token)
                .addParams("nick", nick)
                .addParams("sex", sex + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAg", "usetinfo修改信息失败" + request.toString());
                        Toast.makeText(UserHeaderActivity.this, "修改信息失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("TAg", "usetinfo修改信息成功" + response.toString());
                        Toast.makeText(UserHeaderActivity.this, "修改信息成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    //修改上传头像
    private void updataHeadImg() {

        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @OnClick({R.id.img_back, R.id.iv_userinfo_head, R.id.tv_user_nickname, R.id.rb_boy, R.id.rb_girl, R.id.btn_ok_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.iv_userinfo_head:
                updataHeadImg();
                break;
            case R.id.rb_boy:
                sex = false;
                break;
            case R.id.rb_girl:
                sex = true;
                break;
            case R.id.btn_ok_commit:
                commitInfo();
                tvUserNickname.setVisibility(View.VISIBLE);
                etUserNickname.setVisibility(View.GONE);

                break;
            case R.id.tv_user_nickname:
                tvUserNickname.setVisibility(View.GONE);
                etUserNickname.setVisibility(View.VISIBLE);
                getNickName();
                break;
        }
    }

    private void getNickName() {
        //显示光标
        etUserNickname.requestFocus();
        //设置新光标所在的位置
        Selection.setSelection(etUserNickname.getText(), etUserNickname.getText().length());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                photo = photos.get(0);
                updataimg();
            }
        }

    }

    private void updataimg() {
        //上传头像
        String key = (String) SPUtils.get(UserHeaderActivity.this, "key", "");
        String token = (String) SPUtils.get(UserHeaderActivity.this, "encodeToken", "");
        //压缩
        CompressPic.compressPicture(photo, photo);
        //提交
        File file = new File(photo);
        Map<String, String> headers = new HashMap<>();
        headers.put("img", "");
        OkHttpUtils
                .post().url(URL.ADD_HEADER)
                .addFile("img", "header.jpg", file)
                .addParams("key", key)
                .addParams("token", token)
                .headers(headers)
                .build()
                .connTimeOut(100000)
                .readTimeOut(100000)
                .writeTimeOut(100000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("Tag", "上传头像失败，请检查网络哦!" + request.toString());
                        Toast.makeText(UserHeaderActivity.this, "添加失败，请检查网络哦!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("Tag", "上传头像成功" + response.toString());
                        getUserHeadImg();
                    }
                });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
