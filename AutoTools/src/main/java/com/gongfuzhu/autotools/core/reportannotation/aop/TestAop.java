package com.gongfuzhu.autotools.core.reportannotation.aop;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.listeners.ItemType;
import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.gongfuzhu.autotools.core.reportannotation.Test;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Aspect
@Log4j2
public class TestAop {

    @Autowired
    ReportPortalServer reportPortalServer;

    @Pointcut("@annotation(test)")
    public void point(Test test) {
    }

    @Around("point(test)")
    public Object doAround(ProceedingJoinPoint pjp, Test test) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();


        String name = method.getName();
        String testName = test.testName().isEmpty() ? name : test.testName();
        String desc = test.desc();

        StringBuilder info = info(pjp, desc);

        // launch > suit > test > setp
        Launch launch = Launch.currentLaunch();
        Optional.ofNullable(launch).ifPresent(it -> {
            StartTestItemRQ testItemRQ = reportPortalServer.buildStartItemRq(testName, ItemType.STEP);
            testItemRQ.setDescription(info.toString());
            String methodPath = pjp.getSignature().getName();
            testItemRQ.setCodeRef(methodPath);
            testItemRQ.setTestCaseId(methodPath);
            testItemRQ.setDescription(desc);
            reportPortalServer.startTest(testItemRQ);

        });

        Object[] args = pjp.getArgs();


        Object proceed = null;
        try {
            proceed = pjp.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
            Optional.ofNullable(launch).ifPresent(it -> reportPortalServer.finishTest(ItemStatus.FAILED));

            return proceed;
        }
        Optional.ofNullable(launch).ifPresent(it -> reportPortalServer.finishTest(ItemStatus.PASSED));


        return proceed;

    }


    private static StringBuilder info(ProceedingJoinPoint pjp, String desc) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = pjp.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        if (!desc.isEmpty()) stringBuilder.append(desc + "\n");

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
