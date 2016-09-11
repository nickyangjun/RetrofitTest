package com.nick.study.retrofittest.annotation;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/9/10.
 */
public class ViewBindUtil {

    public static void initViewBind(final Activity activity){
        Class cls = activity.getClass(); //获取Class类
        Field[] declaredFields = cls.getDeclaredFields();  // 拿到所有Field
        for(Field field : declaredFields){
            // 获取Field上的注解
            ViewBind annotation = field.getAnnotation(ViewBind.class);
            if(annotation != null){
                // 获取注解值
                int id = annotation.value();
                try {
                    field.setAccessible(true); //设置可访问
                    field.set(activity,activity.findViewById(id)); //设置findViewById值
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Method[] methods = cls.getDeclaredMethods();
        for(final Method method:methods){
            // 获取Field上的注解
            ViewClick annotation = method.getAnnotation(ViewClick.class);
            if(annotation != null){
                int id = annotation.id();
                //设置点击事件
                activity.findViewById(id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            method.invoke(activity, new Class[0]); //调用自定义的点击事件
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
