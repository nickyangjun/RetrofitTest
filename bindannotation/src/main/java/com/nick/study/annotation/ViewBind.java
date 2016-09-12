package com.nick.study.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2016/9/12.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
@Inherited
public @interface ViewBind {
    int value() default 0;
}