package com.xiaobao.datongbao.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.xiaobao.datongbao.MyApp;

/**
 * Created by Pan on 2017/2/15.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        MyApp.getInstance().addActivity(this);
    }
}
