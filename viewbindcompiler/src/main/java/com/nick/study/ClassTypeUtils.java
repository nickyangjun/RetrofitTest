package com.nick.study;

import com.squareup.javapoet.ClassName;

/**
 * Created by Administrator on 2016/9/29.
 */
public class ClassTypeUtils {
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
}
