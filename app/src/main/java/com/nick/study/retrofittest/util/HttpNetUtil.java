package com.nick.study.retrofittest.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nick.study.retrofittest.MyApplication;

/**
 * Created by yangjun1 on 2016/9/8.
 */
public class HttpNetUtil {

    /**
     * 判断网络连接是否存在
     */
    public static boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) MyApplication.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo info = manager.getActiveNetworkInfo();
        boolean connected = info != null && info.isConnected();
        return connected;
    }
}
