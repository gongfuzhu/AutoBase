package com.gongfuzhu.autotools.core.annotation.aop;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.service.Launch;
import com.gongfuzhu.autotools.core.annotation.Report;
import com.gongfuzhu.autotools.core.annotation.Test;
import com.gongfuzhu.autotools.core.annotation.agen.ReportPortalServer;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

@Aspect
@Log4j2
public class TestAop {

    @Pointcut("@annotation(test)")
    public void point(Test test) {
    }

    @Around("point(test)")
    public Object doAround(ProceedingJoinPoint pjp, Test test) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String methodKey = signature.getMethod().getName();
        String classKey = pjp.getTarget().getClass().getName();
        String concat = classKey.concat(methodKey);
        ReportPortalServer reportPortalServer = ReportPortalServer.CURRENT_ReportPortalServer.get();


        reportPortalServer.startTest(test.testName(), "名称描述", classKey, concat);


        Object[] args = pjp.getArgs();


        Object proceed = null;

        try {
            proceed = pjp.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
            log.info(e.toString());
            reportPortalServer.finishTest(ItemStatus.FAILED, concat);
            return proceed;
        }
        log.info("测试通过");
        reportPortalServer.finishTest(ItemStatus.PASSED, concat);


        return proceed;

    }
}
