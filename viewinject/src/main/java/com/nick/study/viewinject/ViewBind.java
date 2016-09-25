package com.nick.study.viewinject;

import android.app.Activity;

/**
 * Created by Administrator on 2016/9/25.
 */
public class ViewBind {
    private static final String BINDING_CLASS_SUFFIX = "$$ViewBinder";//生成类的后缀 以后会用反射去取

    public static void init(Activity activity){
        String bindClassName = activity.getClass().getName() + BINDING_CLASS_SUFFIX;
        try {
            Class bindClass = Class.forName(bindClassName);
            ViewBinder viewBind = (ViewBinder) bindClass.newInstance();
            viewBind.viewBind(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
