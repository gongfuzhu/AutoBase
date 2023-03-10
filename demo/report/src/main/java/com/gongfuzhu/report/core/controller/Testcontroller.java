package com.gongfuzhu.report.core.controller;

import com.gongfuzhu.autotools.core.annotation.Report;
import com.gongfuzhu.autotools.core.annotation.SeleniumTest;
import com.gongfuzhu.report.core.service.TestService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class Testcontroller {



    @Autowired

    TestService testService;
    @RequestMapping("log1")
    @Report(suitName = "测试日志上传附件",desc = "描述描述")
    public void log1(){
        log.info("测试日志上传附件");
        testService.logFile("这是参数",50);

    }
    @RequestMapping("log2")
    @Report(suitName = "测试日志报异常",desc = "描述描述")
    public void log2(){
        log.info("测试日志报异常");
        testService.logException();

    }
    @RequestMapping("log3")
    @Report(suitName = "测试断言",desc = "描述描述")
    public void log3(){
        log.info("测试断言");
        testService.logAssert();

    }

    @RequestMapping("se")
    @SeleniumTest
    @Report(suitName = "UI测试",desc = "描述描述")
    public void seleniumTest(){
        testService.seleniumTest();
    }
}
