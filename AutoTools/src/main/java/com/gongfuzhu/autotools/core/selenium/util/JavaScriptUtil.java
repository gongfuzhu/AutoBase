package com.gongfuzhu.autotools.core.selenium.util;

import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

@Log4j2
public class JavaScriptUtil {

    // 页面是否到达底部
    public static boolean isBottom(WebDriver webDriver){

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;

        // 窗口高度
        long windoHeight = (long) javascriptExecutor.executeScript("return document.documentElement.clientHeight || document.body.clientHeight");

        // 已经滚动的高度
        long scrollHeight = (long) javascriptExecutor.executeScript("return document.documentElement.scrollTop || document.body.scrollTop");
        // 页面总高度
        long documentHeight = (long) javascriptExecutor.executeScript("return document.documentElement.scrollHeight || document.body.scrollHeight");
        if (windoHeight+scrollHeight == documentHeight){
            log.info("页面已经到达底部");

            return true;
        }

        return false;
    }



    // 翻页到页面顶部
    public static void toPageTop(WebDriver webDriver){
        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0 ,0)");


    }

    // 翻页到页面底部
    public static void toPageBottom(WebDriver webDriver){
        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0 ,document.body.scrollHeight)");

    }


    // 向下翻页
    public static boolean pageDown(WebDriver webDriver){

        if (isBottom(webDriver)){

            return true;
        }
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;

        long windoHeight = (long) javascriptExecutor.executeScript("return document.documentElement.clientHeight || document.body.clientHeight");
        long scrollHeight = (long) javascriptExecutor.executeScript("return document.documentElement.scrollTop || document.body.scrollTop");
        windoHeight+=scrollHeight;
        javascriptExecutor.executeScript("window.scrollTo(0 ,"+windoHeight+")");

        return isBottom(webDriver);


    }


    // 向上翻页
    public static boolean pageUp(WebDriver webDriver){
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;

        long windoHeight = (long) javascriptExecutor.executeScript("return document.documentElement.clientHeight || document.body.clientHeight");
        long scrollHeight = (long) javascriptExecutor.executeScript("return document.documentElement.scrollTop || document.body.scrollTop");
        if (scrollHeight==0){
            log.info("页面已经到达顶部，无需翻页");
            return true;
        }
        scrollHeight-=windoHeight;
        javascriptExecutor.executeScript("window.scrollTo(0 ,"+scrollHeight+")");

        return false;

    }

    // 获取网页cookie
    public static String getCookie(WebDriver webDriver){

        return  (String)((JavascriptExecutor) webDriver).executeScript("return document.cookie");

    }


    // 元素是否在当前窗口
    public static boolean inViewport(WebDriver driver,WebElement element){
        String script =
                "for(var e=arguments[0],f=e.offsetTop,t=e.offsetLeft,o=e.offsetWidth,n=e.offsetHeight;\n"
                        + "e.offsetParent;)f+=(e=e.offsetParent).offsetTop,t+=e.offsetLeft;\n"
                        + "return f<window.pageYOffset+window.innerHeight&&t<window.pageXOffset+window.innerWidth&&f+n>\n"
                        + "window.pageYOffset&&t+o>window.pageXOffset";

        return (boolean) ((JavascriptExecutor) driver).executeScript(script, element);
    }


}
