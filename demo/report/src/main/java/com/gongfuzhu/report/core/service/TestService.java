package com.gongfuzhu.report.core.service;

import com.epam.reportportal.service.ReportPortal;
import com.gongfuzhu.autotools.core.annotation.Test;
import com.gongfuzhu.autotools.core.selenium.InitiWebDriver;
import com.gongfuzhu.autotools.core.selenium.util.WebDriverUtil;
import com.gongfuzhu.autotools.core.tools.LoggingTools;
import com.gongfuzhu.report.core.step.SendLog;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;


@Service
@Log4j2
public class TestService {


    @Autowired
    InitiWebDriver initiWebDriver;
    @Autowired
    SendLog testSetp;



    @Test(testName = "上传测试附件测试")
    public void test01(String aaa,int ddd){

        log.info("上传图片，时间：{}",new Date());
        testSetp.jpg();
        log.info("上传css");
        testSetp.css();
        log.info("上传zip");
        testSetp.zip();
        log.warn("异常信息");
        testSetp.exception();

    }


    @Test
    public void seleniumTest(){

        WebDriver driver = initiWebDriver.getDriver();
        driver.get("https://www.baidu.com");
        // 获取搜索框元素
        WebElement searchBox = driver.findElement(By.name("wd"));
        searchBox.getAriaRole();
        // 在搜索框中输入关键字
        searchBox.sendKeys("Selenium");

        // 提交表单
        searchBox.submit();
        String currentUrl = driver.getCurrentUrl();
        driver.getTitle();
        driver.getPageSource();
        driver.getWindowHandles();
        driver.getWindowHandle();


    }





}
