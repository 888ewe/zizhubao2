package com.xiaobao.datongbao.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xiaobao.datongbao.R;
import com.xiaobao.datongbao.util.MoreUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pan on 2016/9/23.
 */
public class AboutUsActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.iv_about_bg)
    ImageView ivAboutBg;
    @Bind(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initIalise();
        initData();
    }

    private void initIalise() {
        tvVersion.setText("v" + MoreUtil.getAppCurrentVersion(this));
    }

    private void initData() {
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
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
