package com.gongfuzhu.report.core.controller;

import com.gongfuzhu.autotools.core.reportannotation.Report;
import com.gongfuzhu.autotools.core.reportannotation.SeleniumDriver;
import com.gongfuzhu.report.core.service.TestService;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class Testcontroller {



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
    @SeleniumDriver
    @Report(suitName = "UI测试",desc = "UI测试")
    public void seleniumTest(WebDriver webDriver){
        testService.seleniumTest(webDriver);
    }
}
