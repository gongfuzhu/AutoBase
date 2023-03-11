package com.gongfuzhu.report.core.service;

import com.gongfuzhu.autotools.core.reportannotation.TestMethod;
import com.gongfuzhu.autotools.core.selenium.WebDriverServer;
import com.gongfuzhu.report.core.step.SendLog;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@Log4j2
public class TestService {


    @Autowired
    WebDriverServer initiWebDriver;
    @Autowired
    SendLog testSetp;



    @TestMethod(testName = "上传测试附件测试")
//    @Step
    public void logFile(String aaa, int ddd){

        log.info("上传图片，时间：{}",new Date());
        testSetp.jpg();
        log.info("上传css");
        testSetp.css();
        log.info("上传zip");
        testSetp.zip();



    }

    @TestMethod(testName = "异常信息")
    public void logException(){
        log.warn("异常信息");
        testSetp.exception();

    }

    @TestMethod(testName = "断言测试",desc = "这是断言描述")
    public void logAssert(){
        log.warn("断言");
        testSetp.assertFail();
    }


    @TestMethod
    public void seleniumTest(WebDriver driver ){

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


//        driver.findElement(By.name("ssss"));

        log.info("执行完毕，{}",System.currentTimeMillis());

    }





}
