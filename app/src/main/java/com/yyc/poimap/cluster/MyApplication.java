package com.yyc.poimap.cluster;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Administrator on 2019/1/28.
 * 自定义application类
 */

public class MyApplication extends Application {
    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        Utils.init(this);
        super.onCreate();
        sInstance = this;
    }
}
