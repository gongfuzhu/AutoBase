package com.gongfuzhu.autotools.core.selenium.util;

import com.gongfuzhu.autotools.core.tools.PictureTool;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.File;
import java.time.Duration;

@Log4j2
public class WebDriverUtil {


    public static File screenshot(WebDriver driver) {


        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);

        File file = eventFiringWebDriver.getScreenshotAs(OutputType.FILE);
        return file;

    }

    /**
     * 等到元素可以点击
     *
     * @param webDriver
     * @param webElement
     */
    public static void elementToBeClickable(WebDriver webDriver, WebElement webElement) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));

    }

    public static  void presenceOfElementLocated(WebDriver webDriver,By by){
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(by));

    }

    public static void presenceOfElement(WebDriver webDriver, WebElement webElement) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        webDriverWait.withMessage("");
        webDriverWait.until(ExpectedConditions.refreshed(ExpectedConditions.refreshed(ExpectedConditions.alertIsPresent())));
        ExpectedCondition<WebElement> refreshed = ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(webElement));
//        refreshed.

    }


}
