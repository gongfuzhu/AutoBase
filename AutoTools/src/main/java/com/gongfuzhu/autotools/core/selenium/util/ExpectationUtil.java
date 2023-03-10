package com.gongfuzhu.autotools.core.selenium.util;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WebElement为po模式下的元素，@FindBy(xx) 否则达不到期望效果
 */
@Log4j2
public class ExpectationUtil {

    /**
     * 元素是否存在
     * @param by
     * @return
     */
    public static ExpectedCondition<Boolean> notElementr(By by) {



        return driver -> {
            log.info("等待元素消失");
            Duration implicitWaitTimeout = driver.manage().timeouts().getImplicitWaitTimeout();
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
            try {
                WebElement elements = driver.findElement(by);
                log.info("元素存在?：{}_{}",true, elements.getText());
                return false;
            } catch (Exception e) {
                log.info("元素存在?:{}",false);
                return true;
            } finally {
                driver.manage().timeouts().implicitlyWait(implicitWaitTimeout);
            }

        };
    }

    /**
     * 检查元素文本内容
     * @param wait
     * @param element
     */
    public static String logElmentText(WebDriverWait wait,WebElement element){

        WebElement webElement = wait.until(ExpectedConditions.visibilityOf(element));
        log.info("检查元素：{}",webElement.getText());


        return webElement.getText();


    }
}
