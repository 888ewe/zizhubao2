package com.xiaobao.datongbao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.xiaobao.datongbao.MainActivity;
import com.xiaobao.datongbao.R;
import com.xiaobao.datongbao.util.SPUtils;



public class FirstActivity extends BaseActivity {

    ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_first);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);


        AlphaAnimation animation = new AlphaAnimation(0.1f, 1f);
        animation.setDuration(1500);//设置动画持续时间

        iv_icon.setAnimation(animation);             //设置动画
        animation.startNow();                          //启动动画

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                Toast.makeText(FirstActivity.this, "AnimationStart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean first = (boolean) SPUtils.get(FirstActivity.this, "first_start", false);

//                if (!first) {//第一次进入
//                    Intent intent = new Intent(FirstActivity.this, GuideActivity.class);
//                    startActivity(intent);
//                    SPUtils.put(FirstActivity.this, "first_start", true);
//
//                } else {
                    Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                    startActivity(intent);
//                }
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
//                Toast.makeText(FirstActivity.this, "AnimationRepeat", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
