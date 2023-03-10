package com.gongfuzhu.autotools.core.reportannotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Report {




    /**
     * 存放报告的文件名
     * @return
     */
    String suitName() default "";


    String desc() default "";




}
