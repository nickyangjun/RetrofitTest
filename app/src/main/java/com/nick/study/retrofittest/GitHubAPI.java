package com.nick.study.retrofittest;

import com.nick.study.retrofittest.bean.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yangjun1 on 2016/9/8.
 */
public interface GitHubAPI {
    @GET("users")
    Call<List<User>> getUser(@Header("Authorization") String authorization);

    @GET("users/{user}")
    Call<User> getUserInfo(@Path("user") String user);

    @GET("users")
    Call <List<User>> getUserInfoBySort(@Query("sortby") String sort);

    @POST("add")
    Call<User> addUser(@Body User user);

    @POST("login")
    @FormUrlEncoded
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @Multipart
    @POST("register")
    Call<User> registerUser(@Part MultipartBody.Part photo, @Part("username") RequestBody username, @Part("password") RequestBody password);

    @Multipart
    @POST("register")
    Call<User> registerUser(@PartMap Map<String, RequestBody> params);
}
