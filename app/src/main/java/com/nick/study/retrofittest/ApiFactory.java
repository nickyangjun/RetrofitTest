package com.nick.study.retrofittest;

/**
 * Created by yangjun1 on 2016/9/8.
 */
public enum  ApiFactory {
    INSTANCE;

    private static GitHubAPI gitHubAPI;
    private static AnotherAPI anotherAPI;

    ApiFactory() {
    }

    public static GitHubAPI gitHubAPI() {
        if (gitHubAPI == null) {
            gitHubAPI = RetrofitClient.INSTANCE.getRetrofit().create(GitHubAPI.class);
        }
        return gitHubAPI;
    }

    public static AnotherAPI getAnotherAPI() {
        if (anotherAPI == null) {
            anotherAPI = RetrofitClient.INSTANCE.getRetrofit().create(AnotherAPI.class);
        }
        return anotherAPI;
    }
}
