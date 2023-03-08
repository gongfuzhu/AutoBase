package com.gongfuzhu.autotools.core.annotation.aop;

//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.reporter.ExtentKlovReporter;
//import com.mongodb.MongoClientURI;
import com.gongfuzhu.autotools.core.annotation.SeleniumTest;
import com.gongfuzhu.autotools.core.selenium.InitiWebDriver;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
@Aspect
@Log4j2
@Import(InitiWebDriver.class)
public class SeleniumTestAop {

    @Autowired
    InitiWebDriver initiWebDriver;

    @Pointcut(value = "@annotation(annotation.SeleniumTest)")
    public void point() {
    }

    @Around("point()")
    public Object doAround(ProceedingJoinPoint pjp) {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        if (method == null) {
            return null;
        }

        SeleniumTest annotation = method.getAnnotation(SeleniumTest.class);

        DriverManagerType driverManagerType = annotation.driverType();
        boolean docker = annotation.isDocker();
        boolean report = annotation.report();
        boolean driver = annotation.isDriver();
        SeleniumTest.ChromeOption option = annotation.option();
        if (driver) {
            if (docker) {
                initiWebDriver.dockerDriver(driverManagerType, option.getAbstractDriverOptions());

            } else {
                initiWebDriver.localDriver(driverManagerType, option.getAbstractDriverOptions());
            }
        }

//        ExtentReports extent = null;
        if (report) {

//            ExtentKlovReporter klovReporter = new ExtentKlovReporter("tiktok", "报告名称");
//
//            MongoClientURI mongoClientURI = new MongoClientURI("mongodb://klov:klov@192.168.8.55:27017/klov");
//
//            // Set Klov Server URL
//            klovReporter.initMongoDbConnection(mongoClientURI);
//
//            // Set Klov Server URL (Can be External too)
//            klovReporter.initKlovServerConnection("http://127.0.0.1:80");

            // Create ExtentReports and attach Klov Reporter
//            extent = new ExtentReports();
////            extent.attachReporter(klovReporter);
//            ExtentTest test = extent.createTest(method.getName());
//            initiWebDriver.startLinsen(test);

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
//        extent.flush();
        return proceed;

    }


}
