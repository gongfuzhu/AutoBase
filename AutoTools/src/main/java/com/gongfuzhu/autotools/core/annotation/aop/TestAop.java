package com.gongfuzhu.autotools.core.annotation.aop;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.utils.AttributeParser;
import com.gongfuzhu.autotools.core.annotation.Report;
import com.gongfuzhu.autotools.core.annotation.Test;
import com.gongfuzhu.autotools.core.annotation.agen.ReportPortalServer;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.json.Json;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.epam.reportportal.utils.properties.ListenerProperty.LAUNCH_ATTRIBUTES;

@Aspect
@Log4j2
public class TestAop {

    @Pointcut("@annotation(test)")
    public void point(Test test) {
    }

    @Around("point(test)")
    public Object doAround(ProceedingJoinPoint pjp, Test test) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        Object[] args1 = pjp.getArgs();
        Parameter[] parameters = method.getParameters();


        String name = method.getName();
        String testName = test.testName().isEmpty() ? name : test.testName();
        String desc = test.desc();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("方法名:").append(method.getName()).append(";");
//        Json json = new Json();
//        String desc = json.toJson();
        ReportPortalServer reportPortalServer = ReportPortalServer.CURRENT_ReportPortalServer.get();
        reportPortalServer.startTest(testName, desc, Collections.unmodifiableSet(AttributeParser.parseAsSet(stringBuilder.toString())));

        Object[] args = pjp.getArgs();


        Object proceed = null;
        try {
            proceed = pjp.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
            reportPortalServer.finishTest(ItemStatus.FAILED);
            return proceed;
        }
        reportPortalServer.finishTest(ItemStatus.PASSED);

        return proceed;

    }
}
