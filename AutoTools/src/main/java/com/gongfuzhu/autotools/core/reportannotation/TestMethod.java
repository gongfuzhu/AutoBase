package com.gongfuzhu.autotools.core.reportannotation;


import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Order(2)
public @interface TestMethod {

    String testName() default "";

    String desc() default "";
}
