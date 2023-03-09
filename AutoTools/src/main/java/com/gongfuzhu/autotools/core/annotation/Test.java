package com.gongfuzhu.autotools.core.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Test {

    String testName() default "";

    String desc() default "";
}
