package com.nick.study.retrofittest.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangjun1 on 2016/9/8.
 */
public class UserAgentInterceptor implements Interceptor {
    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private final String userAgentHeaderValue;

    public UserAgentInterceptor(String userAgentHeaderValue) {
        this.userAgentHeaderValue = userAgentHeaderValue;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest.newBuilder()
                .removeHeader(USER_AGENT_HEADER_NAME)   //移除先前默认的User-Agent
                .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)  //设置新的User-Agent
                .build();
        return chain.proceed(requestWithUserAgent);
    }
}
