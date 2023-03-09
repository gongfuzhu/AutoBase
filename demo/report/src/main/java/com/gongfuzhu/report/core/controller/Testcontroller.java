package com.gongfuzhu.report.core.controller;

import com.gongfuzhu.autotools.core.annotation.Report;
import com.gongfuzhu.report.core.service.TestService;
import com.gongfuzhu.report.core.step.TestSetp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class Testcontroller {



    @Autowired

    TestService testService;
    @RequestMapping("log")
    @Report(suitName = "登录模块",desc = "这还是描述")
    public void test(){
        log.info("这是日志1");
        testService.test01("这是参数",50);

    }
}
