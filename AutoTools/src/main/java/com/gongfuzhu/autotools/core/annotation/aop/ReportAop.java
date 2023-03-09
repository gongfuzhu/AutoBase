package com.gongfuzhu.autotools.core.annotation.aop;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.service.ReportPortal;
import com.gongfuzhu.autotools.core.annotation.Report;
import com.gongfuzhu.autotools.core.annotation.agen.ReportPortalServer;
import com.gongfuzhu.autotools.core.selenium.MyWebDriverListener;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
@Log4j2
public class ReportAop {



    @Autowired
    ReportPortal reportPortal;

    @Pointcut("@annotation(report)")
    public void point(Report report) {
    }

    @Around("point(report)")
    public Object doAround(ProceedingJoinPoint pjp, Report report) {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        String key = pjp.getTarget().getClass().getName();
        log.info("方法名称{}", method);

        ReportPortalServer reportPortalServer = new ReportPortalServer(reportPortal);
        reportPortalServer.startLaunch();

        reportPortalServer.startTestSuite("测试测试", "这里是描述",key);


        Object[] args = pjp.getArgs();


        Object proceed = null;

        try {
            proceed = pjp.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
            log.info(e.toString());
            reportPortalServer.finishTestSuite(ItemStatus.FAILED, key);
            return proceed;
        }finally {
            reportPortalServer.finishLaunch();
        }
        log.info("方法被执行");
        reportPortalServer.finishTestSuite(ItemStatus.PASSED, key);


        return proceed;

    }


}
