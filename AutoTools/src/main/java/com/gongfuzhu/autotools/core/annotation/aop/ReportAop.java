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
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        String suitName = report.suitName().isEmpty() ? signature.getName() : report.suitName();

        StringBuilder info = info(pjp, report.desc());

        ReportPortalServer reportPortalServer = new ReportPortalServer(reportPortal, report.desc());
        reportPortalServer.startLaunch();


        reportPortalServer.startTestSuite(suitName, info.toString());


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

    protected static StringBuilder info(ProceedingJoinPoint pjp, String desc) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = pjp.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("class:").append(pjp.getTarget().getClass().getSimpleName() + "\n");
        stringBuilder.append("methodName:").append(method.getName() + "\n");

        if (!desc.isEmpty()) stringBuilder.append("desc:").append(desc + "\n");

        if (args.length != 0) {
            stringBuilder.append("parameters:\n");
            for (int i = 0; i < parameters.length; i++) {
                stringBuilder.append("- ").append(parameterTypes[i].getSimpleName()).append(":").append(parameters[i].getName()).append(":").append(args[i].toString()).append("\n");
            }
        }



//        if (args.length != 0) Arrays.stream(args).forEach(it -> {
//            stringBuilder.append(it);
//        });

        return stringBuilder;

    }


}
