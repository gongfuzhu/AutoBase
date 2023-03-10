package com.gongfuzhu.autotools.core.reportannotation.aop;

import com.gongfuzhu.autotools.core.reportannotation.SeleniumTest;
import com.gongfuzhu.autotools.core.selenium.InitiWebDriver;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Log4j2
@Import(InitiWebDriver.class)
public class SeleniumTestAop {

    @Autowired
    InitiWebDriver initiWebDriver;

    @Pointcut(value = "@annotation(seleniumTest)")
    public void point(SeleniumTest seleniumTest) {
    }

    @Around("point(seleniumTest)")
    public Object doAround(ProceedingJoinPoint pjp,SeleniumTest seleniumTest) {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        if (method == null) {
            return null;
        }


        DriverManagerType driverManagerType = seleniumTest.driverType();
        boolean docker = seleniumTest.isDocker();
        boolean report = seleniumTest.report();
        boolean driver = seleniumTest.isDriver();
        SeleniumTest.ChromeOption option = seleniumTest.option();
        if (driver) {
            if (docker) {
                initiWebDriver.dockerDriver(driverManagerType, option.getAbstractDriverOptions());

            } else {
                initiWebDriver.localDriver(driverManagerType, option.getAbstractDriverOptions());
            }
        }

        if (report) {

        }


        Object[] args = pjp.getArgs();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof WebDriver) {
            }
        }


        Object proceed = null;

        try {
            proceed = pjp.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
            return proceed;
        }finally {
            initiWebDriver.closeDriver();
        }
        return proceed;

    }


}
