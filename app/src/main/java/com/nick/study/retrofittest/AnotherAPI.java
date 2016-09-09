package com.nick.study.retrofittest;

import com.nick.study.retrofittest.bean.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by yangjun1 on 2016/9/8.
 */
public interface AnotherAPI {
    @Headers("Cache-Control:no-cache,max-age=0")
    @GET
    Call<User> gankIOHistory(@Url String url);

    //http://gank.io/api/day/2015/08/07
    @GET
    Call<User> getOneDay(@Url String url);

    //http://news-at.zhihu.com/api/4/news/before/20160517
    @Headers("Cache-Control: max-age=120")
    @GET
    Call<User> getNewsList(@Url String url);
}
