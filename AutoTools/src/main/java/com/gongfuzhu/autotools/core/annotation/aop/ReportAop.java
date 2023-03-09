package com.gongfuzhu.autotools.core.annotation.aop;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.service.ReportPortal;
import com.gongfuzhu.autotools.core.annotation.Report;
import com.gongfuzhu.autotools.core.annotation.agen.ReportPortalServer;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

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
        String suitName = report.suitName().isEmpty() ? signature.getName() : report.suitName();


        Object[] args1 = pjp.getArgs();
        String desc = report.desc();
        Map<String, Object> keyValue = Map.of("描述", desc, "方法", method.getName(), "参数", args1);
        Json json = new Json();


        ReportPortalServer reportPortalServer = new ReportPortalServer(reportPortal);
        reportPortalServer.startTestSuite(suitName, json.toJson(keyValue));


        Object[] args = pjp.getArgs();


        Object proceed = null;

        try {
            proceed = pjp.proceed(args);
            reportPortalServer.finishTestSuite(ItemStatus.PASSED);
        } catch (Throwable e) {
            e.printStackTrace();
            log.info(e.toString());
            reportPortalServer.finishTestSuite(ItemStatus.FAILED);
            return proceed;
        } finally {
            reportPortalServer.finishLaunch();
        }


        return proceed;

    }


}
