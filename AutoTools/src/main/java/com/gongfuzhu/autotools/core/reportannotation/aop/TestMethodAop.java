package com.gongfuzhu.autotools.core.reportannotation.aop;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.listeners.ItemType;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.gongfuzhu.autotools.core.reportannotation.TestMethod;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Aspect
@Log4j2
public class TestMethodAop {


    @Pointcut("@annotation(test)")
    public void point(TestMethod test) {
    }

    @Around("point(test)")
    public Object doAround(ProceedingJoinPoint pjp, TestMethod test) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        String codeRefer = pjp.getTarget().getClass().getName() + "." + method.getName();


        String name = method.getName();
        String testName = test.testName().isEmpty() ? name : test.testName();
        String desc = test.desc();

        ReportPortalServer reportPortalServer = ReportPortalServer.currentLaunch();
        StringBuilder info = info(pjp, desc);

        // launch > suit > test > setp

        reportPortalServer.startTest(testName,info.toString(),codeRefer);


        Object[] args = pjp.getArgs();


        Object proceed = null;
        try {
            proceed = pjp.proceed(args);
        } catch (Throwable e) {
            log.fatal("exceptionT：", e);
            reportPortalServer.finishTest(ItemStatus.FAILED);
            throw e;
        }
        reportPortalServer.finishTest(ItemStatus.PASSED);

        return proceed;

    }


    private static StringBuilder info(ProceedingJoinPoint pjp, String desc) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = pjp.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        if (!desc.isEmpty()) stringBuilder.append("desc:").append(desc + "\n");

        if (args.length != 0) {
            stringBuilder.append("parameters:\n");
            for (int i = 0; i < parameters.length; i++) {
                int finalI = i;
                Optional.ofNullable(args[finalI]).ifPresent(it->{
                    stringBuilder.append("- ").append(parameterTypes[finalI].getSimpleName()).append(":").append(parameters[finalI].getName()).append(":").append(args[finalI].toString()).append("\n");
                });
            }
        }

        return stringBuilder;

    }
}
