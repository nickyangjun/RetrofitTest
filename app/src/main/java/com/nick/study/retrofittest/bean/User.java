package com.nick.study.retrofittest.bean;

/**
 * Created by yangjun1 on 2016/9/8.
 */
public class User {
    public long id;
    public String avatar_url;
    public String type;
    public String login;
    public User(long id,String name){
        this.id = id;
        this.login = name;
    }
}
