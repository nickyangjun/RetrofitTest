package com.nick.study.retrofittest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yangjun1 on 2016/9/8.
 */
public enum  RetrofitClient {
    INSTANCE;

    private final Retrofit retrofit;

    RetrofitClient() {
        retrofit = new Retrofit.Builder()
                //设置OKHttpClient
                .client(OkHttpFactory.getOkHttpClient())
                //baseUrl
                .baseUrl(ApiContants.GITHUB_BASEURL)
                //gson转化器
                .addConverterFactory(GsonConverterFactory.create())
                //创建
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
