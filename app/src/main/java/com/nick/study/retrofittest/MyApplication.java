package com.nick.study.retrofittest;

import android.app.Application;
import android.content.Context;

/**
 * Created by yangjun1 on 2016/9/8.
 */
public class MyApplication extends Application {
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
