package com.gongfuzhu.autotools.core.reportannotation.aop;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.listeners.ItemType;
import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.gongfuzhu.autotools.core.reportannotation.Report;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
@Log4j2
@Import(ReportPortalServer.class)
public class ReportAop {


    @Autowired
    ReportPortalServer reportPortalServer;
    @Autowired
    ReportPortal reportPortal;

    @Pointcut("@annotation(report)")
    public void point(Report report) {
    }

    @Around("point(report)")
    public Object doAround(ProceedingJoinPoint pjp, Report report) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String suitName = report.suitName().isEmpty() ? signature.getName() : report.suitName();


        reportPortalServer.initReport(reportPortal, report.desc());
        reportPortalServer.startLaunch();

        StartTestItemRQ testSuit = reportPortalServer.buildStartItemRq(suitName, ItemType.SUITE);

        testSuit.setDescription(report.desc());

        reportPortalServer.startTestSuite(suitName, info(pjp).toString());


        Object[] args = pjp.getArgs();


        Object proceed = null;

        try {
            proceed = pjp.proceed(args);
            reportPortalServer.finishTestSuite(ItemStatus.PASSED);
            reportPortalServer.finishLaunch(ItemStatus.PASSED);
        } catch (Throwable e) {
            log.fatal("exceptionR：",e);
            reportPortalServer.finishTestSuite(ItemStatus.FAILED);
            reportPortalServer.finishLaunch(ItemStatus.FAILED);
            throw e;
        }


        return proceed;

    }

    private StringBuilder info(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        Parameter[] parameters = method.getParameters();
        Class<?>[] parameterTypes = method.getParameterTypes();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("- ").append(pjp.getTarget().getClass().getName()).append(".").append(pjp.getSignature().getName()).append("\n");
        if (args.length != 0) {
            stringBuilder.append("parameters:\n");
            for (int i = 0; i < parameters.length; i++) {
                stringBuilder.append("- ").append(parameterTypes[i].getSimpleName()).append(":").append(parameters[i].getName()).append(":").append(args[i].toString()).append("\n");
            }
        }

        return stringBuilder;

    }


}
