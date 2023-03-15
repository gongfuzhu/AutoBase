package com.gongfuzhu.report.core.controller;

import com.epam.reportportal.formatting.http.converters.DefaultHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingCookieConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingUriConverter;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.restassured.ReportPortalRestAssuredLoggingFilter;
import com.gongfuzhu.autotools.core.reportannotation.Report;
import com.gongfuzhu.autotools.core.reportannotation.SeleniumDriver;
import com.gongfuzhu.autotools.core.selenium.WebDriverServer;
import com.gongfuzhu.report.core.service.RestfullService;
import com.gongfuzhu.report.core.service.TestService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.internal.ResponseSpecificationImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.manager.SeleniumManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.restassured.config.EncoderConfig.encoderConfig;

@RestController
@Log4j2
public class Testcontroller {



    @Autowired
    RestfullService restfullService;
    @Autowired

    TestService testService;
    @RequestMapping("log1")
    @Report(suitName = "测试日志上传附件",desc = "测试日志上传附件")
    public void log1(){
        log.info("测试日志上传附件");
        testService.logFile("这是参数",50);

    }
    @RequestMapping("log2")
    @Report(suitName = "测试日志报异常",desc = "测试日志报异常")
    public void log2(){
        log.info("测试日志报异常");
        testService.logException();

    }
    @RequestMapping("log3")
    @Report(suitName = "测试断言",desc = "测试断言")
    public void log3(){
        log.info("测试断言");
        testService.logAssert();

    }

    @RequestMapping("se")
    @Report(suitName = "UI测试",desc = "UI测试")
    public void seleniumTest(){
        testService.seleniumTest();
    }
    @RequestMapping("demo")
    @Report(suitName = "reportDemo",desc = "demo demo")
    public void demo(){

        testService.logFile("admin",123);
        try {

            testService.logException();
        }catch (Exception e){

        }
        try {

            testService.logAssert();
        }catch (Exception e){

        }
        testService.seleniumTest();

        RestAssured.reset();
        RestAssured.baseURI = "http://222.180.202.110:4141";
        RestAssured.filters(new ReportPortalRestAssuredLoggingFilter(
                42,
                LogLevel.INFO,
                SanitizingHttpHeaderConverter.INSTANCE,
                DefaultHttpHeaderConverter.INSTANCE,
                SanitizingCookieConverter.INSTANCE,
                SanitizingUriConverter.INSTANCE
        ));


        RestAssured.responseSpecification = new ResponseSpecBuilder().expectStatusCode(200).build();
        RestAssured.config = RestAssuredConfig.config().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
        restfullService.demo();


    }
    @RequestMapping("re")
    @Report(suitName = "接口测试demo",desc = "接口测试-描述")
    public void re(){
        RestAssured.reset();
        RestAssured.baseURI = "http://222.180.202.110:4141";
        RestAssured.filters(new ReportPortalRestAssuredLoggingFilter(
                42,
                LogLevel.INFO,
                SanitizingHttpHeaderConverter.INSTANCE,
                DefaultHttpHeaderConverter.INSTANCE,
                SanitizingCookieConverter.INSTANCE,
                SanitizingUriConverter.INSTANCE
        ));


        RestAssured.responseSpecification = new ResponseSpecBuilder().expectStatusCode(200).build();
        RestAssured.config = RestAssuredConfig.config().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
        restfullService.demo();
    }
}
