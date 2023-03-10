package com.gongfuzhu.autotools.core.selenium;

import com.epam.reportportal.service.ReportPortal;
import com.gongfuzhu.autotools.core.selenium.util.WebDriverUtil;
import com.google.common.io.Resources;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.*;

@Log4j2
public class MyWebDriverListener implements WebDriverListener {
    private WebDriver wb;

    public MyWebDriverListener(WebDriver wb) {
        this.wb = wb;
    }

    // 全局设置
    @Override
    public void beforeAnyCall(Object target, Method method, Object[] args) {


    }

    @Override
    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {

    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        log.fatal("webDriver exception:{}",e.getTargetException().getMessage());
    }

    // WebDriver

    @Override
    public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
    }

    @Override
    public void afterAnyWebDriverCall(WebDriver driver, Method method, Object[] args, Object result) {
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        log.info("打开网页：{}", url);

    }

    @Override
    public void beforeGetCurrentUrl(WebDriver driver) {
        log.info("获取当前链接");
    }

    @Override
    public void afterGetCurrentUrl(String result, WebDriver driver) {
        log.info("获取当前链接：{}",result);

    }

    @Override
    public void beforeGetTitle(WebDriver driver) {
    }

    @Override
    public void afterGetTitle(WebDriver driver, String result) {
        log.info("获取网页title:{}",result);
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        log.info("beforeFindElement：{}", locator.toString());
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        log.info("afterFindElement:{}",result.getAriaRole());
    }

    @Override
    public void beforeFindElements(WebDriver driver, By locator) {
        log.info("查找多个元素：{}", locator.toString());
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
    }

    @Override
    public void beforeGetPageSource(WebDriver driver) {

    }

    @SneakyThrows
    @Override
    public void afterGetPageSource(WebDriver driver, String result) {
        File tempFile = File.createTempFile("PageSource", "html");

        FilterWriter.nullWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
        bufferedWriter.write(result);
        bufferedWriter.close();
        infoSc(tempFile,"获取网页源码");
    }

    @Override
    public void beforeClose(WebDriver driver) {
        log.info("关闭浏览器");
    }

    @Override
    public void afterClose(WebDriver driver) {
    }

    @Override
    public void beforeQuit(WebDriver driver) {
    }

    @Override
    public void afterQuit(WebDriver driver) {
        log.info("afterQuit");
    }

    @Override
    public void beforeGetWindowHandles(WebDriver driver) {
    }

    @Override
    public void afterGetWindowHandles(WebDriver driver, Set<String> result) {
        log.info("afterGetWindowHandles：{}",result.toString());
    }

    @Override
    public void beforeGetWindowHandle(WebDriver driver) {
    }

    @Override
    public void afterGetWindowHandle(WebDriver driver, String result) {
        log.info("afterGetWindowHandle：{}",result);
    }

    @Override
    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        log.info("执行脚本：{}", script);
    }

    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        infoSc(driver, "afterExecuteScript");
    }

    @Override
    public void beforeExecuteAsyncScript(WebDriver driver, String script, Object[] args) {
        log.info("执行异步脚本：{}", script);
    }

    @Override
    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {
        infoSc(driver, "afterExecuteAsyncScript");
    }

    @Override
    public void beforePerform(WebDriver driver, Collection<Sequence> actions) {
        log.info("beforePerform：{}",actions.toArray());
    }

    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
        infoSc(wb,"afterPerform");
    }

    @Override
    public void beforeResetInputState(WebDriver driver) {
    }

    @Override
    public void afterResetInputState(WebDriver driver) {
    }


    // WebElement


    @Override
    public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
    }

    @Override
    public void afterAnyWebElementCall(WebElement element, Method method, Object[] args, Object result) {
    }

    @Override
    public void beforeClick(WebElement element) {
        infoSc(element.getScreenshotAs(OutputType.FILE), "beforeClick");
    }

    @Override
    public void afterClick(WebElement element) {
        infoSc(wb, "afterClick");
    }

    @Override
    public void beforeSubmit(WebElement element) {
        infoSc(element.getScreenshotAs(OutputType.FILE), "beforeSubmit");
    }

    @Override
    public void afterSubmit(WebElement element) {
        infoSc(wb, "afterSubmit");
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(keysToSend).forEach(it->{
            stringBuilder.append(it);
        });
        infoSc(element.getScreenshotAs(OutputType.FILE), "sendKeys：" + stringBuilder);
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        infoSc(wb, "afterSendKeys");

    }

    @Override
    public void beforeClear(WebElement element) {
        infoSc(element.getScreenshotAs(OutputType.FILE), "beforeClear");
    }

    @Override
    public void afterClear(WebElement element) {
        infoSc(wb, "afterClear");
    }

    @Override
    public void beforeGetTagName(WebElement element) {
    }

    @Override
    public void afterGetTagName(WebElement element, String result) {
        log.info("afterGetTagName：{}",result);
    }

    @Override
    public void beforeGetAttribute(WebElement element, String name) {
    }

    @Override
    public void afterGetAttribute(WebElement element, String name, String result) {
        log.info("afterGetAttribute：{}-{}",name,result);
    }

    @Override
    public void beforeIsSelected(WebElement element) {
    }

    @Override
    public void afterIsSelected(WebElement element, boolean result) {
        log.info("afterIsSelected：{}",result);
    }

    @Override
    public void beforeIsEnabled(WebElement element) {
    }

    @Override
    public void afterIsEnabled(WebElement element, boolean result) {
        log.info("afterIsEnabled：{}",result);
    }

    @Override
    public void beforeGetText(WebElement element) {
    }

    @Override
    public void afterGetText(WebElement element, String result) {
        log.info("getText：{}", result);
    }

    @Override
    public void beforeFindElement(WebElement element, By locator) {
    }

    @Override
    public void afterFindElement(WebElement element, By locator, WebElement result) {
    }

    @Override
    public void beforeFindElements(WebElement element, By locator) {
    }

    @Override
    public void afterFindElements(WebElement element, By locator, List<WebElement> result) {
    }

    @Override
    public void beforeIsDisplayed(WebElement element) {
    }

    @Override
    public void afterIsDisplayed(WebElement element, boolean result) {
    }

    @Override
    public void beforeGetLocation(WebElement element) {
    }

    @Override
    public void afterGetLocation(WebElement element, Point result) {
    }

    @Override
    public void beforeGetSize(WebElement element) {
    }

    @Override
    public void afterGetSize(WebElement element, Dimension result) {
    }

    @Override
    public void beforeGetCssValue(WebElement element, String propertyName) {
    }

    @Override
    public void afterGetCssValue(WebElement element, String propertyName, String result) {
    }

    // Navigation
    @Override
    public void beforeAnyNavigationCall(WebDriver.Navigation navigation, Method method, Object[] args) {
    }

    @Override
    public void afterAnyNavigationCall(WebDriver.Navigation navigation, Method method, Object[] args, Object result) {
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, String url) {
    }

    @Override
    public void afterTo(WebDriver.Navigation navigation, String url) {
    }

    @Override
    public void beforeTo(WebDriver.Navigation navigation, URL url) {
    }

    @Override
    public void afterTo(WebDriver.Navigation navigation, URL url) {
    }

    @Override
    public void beforeBack(WebDriver.Navigation navigation) {
    }

    @Override
    public void afterBack(WebDriver.Navigation navigation) {
    }

    @Override
    public void beforeForward(WebDriver.Navigation navigation) {
    }

    @Override
    public void afterForward(WebDriver.Navigation navigation) {
    }

    @Override
    public void beforeRefresh(WebDriver.Navigation navigation) {
    }

    @Override
    public void afterRefresh(WebDriver.Navigation navigation) {
    }

    // Alert
    @Override
    public void beforeAnyAlertCall(Alert alert, Method method, Object[] args) {
    }

    @Override
    public void afterAnyAlertCall(Alert alert, Method method, Object[] args, Object result) {
    }

    @Override
    public void beforeAccept(Alert alert) {
    }

    @Override
    public void afterAccept(Alert alert) {
    }

    @Override
    public void beforeDismiss(Alert alert) {
    }

    @Override
    public void afterDismiss(Alert alert) {
    }

    @Override
    public void beforeGetText(Alert alert) {
    }

    @Override
    public void afterGetText(Alert alert, String result) {
    }

    @Override
    public void beforeSendKeys(Alert alert, String text) {
    }

    @Override

    public void afterSendKeys(Alert alert, String text) {
    }


    // Options
    @Override
    public void beforeAnyOptionsCall(WebDriver.Options options, Method method, Object[] args) {
    }

    @Override
    public void afterAnyOptionsCall(WebDriver.Options options, Method method, Object[] args, Object result) {
    }

    @Override
    public void beforeAddCookie(WebDriver.Options options, Cookie cookie) {
    }

    @Override
    public void afterAddCookie(WebDriver.Options options, Cookie cookie) {
    }

    @Override
    public void beforeDeleteCookieNamed(WebDriver.Options options, String name) {
    }

    @Override
    public void afterDeleteCookieNamed(WebDriver.Options options, String name) {
    }

    @Override
    public void beforeDeleteCookie(WebDriver.Options options, Cookie cookie) {
    }

    @Override
    public void afterDeleteCookie(WebDriver.Options options, Cookie cookie) {
    }

    @Override
    public void beforeDeleteAllCookies(WebDriver.Options options) {
    }

    @Override
    public void afterDeleteAllCookies(WebDriver.Options options) {
    }

    @Override
    public void beforeGetCookies(WebDriver.Options options) {
    }

    @Override
    public void afterGetCookies(WebDriver.Options options, Set<Cookie> result) {
    }

    @Override
    public void beforeGetCookieNamed(WebDriver.Options options, String name) {
    }

    @Override

    public void afterGetCookieNamed(WebDriver.Options options, String name, Cookie result) {
    }

    // Timeouts
    @Override
    public void beforeAnyTimeoutsCall(WebDriver.Timeouts timeouts, Method method, Object[] args) {
    }

    @Override
    public void afterAnyTimeoutsCall(WebDriver.Timeouts timeouts, Method method, Object[] args, Object result) {
    }

    @Override
    public void beforeImplicitlyWait(WebDriver.Timeouts timeouts, Duration duration) {
    }

    @Override
    public void afterImplicitlyWait(WebDriver.Timeouts timeouts, Duration duration) {
    }

    @Override
    public void beforeSetScriptTimeout(WebDriver.Timeouts timeouts, Duration duration) {
    }

    @Override
    public void afterSetScriptTimeout(WebDriver.Timeouts timeouts, Duration duration) {
    }

    @Override
    public void beforePageLoadTimeout(WebDriver.Timeouts timeouts, Duration duration) {
    }

    @Override
    public void afterPageLoadTimeout(WebDriver.Timeouts timeouts, Duration duration) {
    }

    // Window
    @Override
    public void beforeAnyWindowCall(WebDriver.Window window, Method method, Object[] args) {
    }

    @Override
    public void afterAnyWindowCall(WebDriver.Window window, Method method, Object[] args, Object result) {
    }

    @Override
    public void beforeGetSize(WebDriver.Window window) {
    }

    @Override
    public void afterGetSize(WebDriver.Window window, Dimension result) {
    }

    @Override
    public void beforeSetSize(WebDriver.Window window, Dimension size) {
    }

    @Override
    public void afterSetSize(WebDriver.Window window, Dimension size) {
    }

    @Override
    public void beforeGetPosition(WebDriver.Window window) {
    }

    @Override
    public void afterGetPosition(WebDriver.Window window, Point result) {
    }

    @Override
    public void beforeSetPosition(WebDriver.Window window, Point position) {
    }

    @Override
    public void afterSetPosition(WebDriver.Window window, Point position) {
    }

    @Override
    public void beforeMaximize(WebDriver.Window window) {

    }

    @Override
    public void afterMaximize(WebDriver.Window window) {
        log.info("窗口最大化");
    }

    @Override
    public void beforeFullscreen(WebDriver.Window window) {
    }

    @Override
    public void afterFullscreen(WebDriver.Window window) {
    }


    private void infoSc(WebDriver webDriver, String message) {

        File screenshot = WebDriverUtil.screenshot(webDriver);
        ReportPortal.emitLog(message+" screenShot", "info", new Date(), screenshot);
    }

    private void infoSc(File file, String message) {

        ReportPortal.emitLog(message+" screenShot", "info", new Date(), file);
    }


}
