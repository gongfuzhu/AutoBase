package com.gongfuzhu.report.core.service;

import com.gongfuzhu.autotools.core.annotation.Report;
import com.gongfuzhu.autotools.core.annotation.Test;
import com.gongfuzhu.report.core.step.TestSetp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class TestService {


    @Autowired
    TestSetp testSetp;
    @Test(testName = "这是测试名称")
    public void test01(){

        log.info("这是test01");
        log.info("这是test01");
        log.info("这是test01");
        testSetp.test();

    }



}
