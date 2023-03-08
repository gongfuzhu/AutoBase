package com.gongfuzhu.autotools.core.selenium;

import com.gongfuzhu.autotools.core.selenium.util.WebDriverUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Log4j2
@Getter
@Setter
public class MyWebDriverListener implements WebDriverListener {
//    private ExtentTest extentTest;

    private int step = 1;

    private boolean screenshot = false;

    private long time = 1000;

    private String savePath;

    // 全局设置
    @Override
    public void beforeAnyCall(Object target, Method method, Object[] args) {
        log.info("------------------------------------beforeAnyCall------------------------------------");
        log.info("方法名：{}", method.getName());
        log.info("target:{}", target.toString());


    }

    @Override
    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
//        extentTest.log(Status.INFO,"方法名称："+method.getName()+"参数"+result.toString());

    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
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
        String screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//        extentTest.addScreenCaptureFromBase64String(screenshotAs,"打开网页：" + url);
    }

    @Override
    public void beforeGetCurrentUrl(WebDriver driver) {
    }

    @Override
    public void afterGetCurrentUrl(String result, WebDriver driver) {
    }

    @Override
    public void beforeGetTitle(WebDriver driver) {
    }

    @Override
    public void afterGetTitle(WebDriver driver, String result) {
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
    }

    @Override
    public void beforeFindElements(WebDriver driver, By locator) {
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
    }

    @Override
    public void beforeGetPageSource(WebDriver driver) {
    }

    @Override
    public void afterGetPageSource(WebDriver driver, String result) {
    }

    @Override
    public void beforeClose(WebDriver driver) {
    }

    @Override
    public void afterClose(WebDriver driver) {
    }

    @Override
    public void beforeQuit(WebDriver driver) {
    }

    @Override
    public void afterQuit(WebDriver driver) {
    }

    @Override
    public void beforeGetWindowHandles(WebDriver driver) {
    }

    @Override
    public void afterGetWindowHandles(WebDriver driver, Set<String> result) {
    }

    @Override
    public void beforeGetWindowHandle(WebDriver driver) {
    }

    @Override
    public void afterGetWindowHandle(WebDriver driver, String result) {
    }

    @Override
    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
    }

    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
    }

    @Override
    public void beforeExecuteAsyncScript(WebDriver driver, String script, Object[] args) {
    }

    @Override
    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {
    }

    @Override
    public void beforePerform(WebDriver driver, Collection<Sequence> actions) {
    }

    @Override
    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
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
    }

    @Override
    public void afterClick(WebElement element) {
    }

    @Override
    public void beforeSubmit(WebElement element) {
    }

    @Override
    public void afterSubmit(WebElement element) {
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
    }

    @Override
    public void beforeClear(WebElement element) {
    }

    @Override
    public void afterClear(WebElement element) {
    }

    @Override
    public void beforeGetTagName(WebElement element) {
    }

    @Override
    public void afterGetTagName(WebElement element, String result) {
    }

    @Override
    public void beforeGetAttribute(WebElement element, String name) {
    }

    @Override
    public void afterGetAttribute(WebElement element, String name, String result) {
    }

    @Override
    public void beforeIsSelected(WebElement element) {
    }

    @Override
    public void afterIsSelected(WebElement element, boolean result) {
    }

    @Override
    public void beforeIsEnabled(WebElement element) {
    }

    @Override
    public void afterIsEnabled(WebElement element, boolean result) {
    }

    @Override
    public void beforeGetText(WebElement element) {
    }

    @Override
    public void afterGetText(WebElement element, String result) {
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

    @SneakyThrows
    private void waitTime() {
        Thread.sleep(time);


    }


    private void screenshot(WebDriver driver, String text) {
        log.info("截图方法被执行：{}", screenshot);

        if (!screenshot) {
            step++;
            return;
        }


        String no = "No:";
        no += step;

        String fileName = step + "_" + text + ".png";


        String content = no + " " + text;


//        File file3 = new File(this.getClass().getResource("/").getPath());

//        log.info("文件路径：",file3.getPath());

//        String jarPath = SystemTool.getJarPath();
//        String jarPath= JarTool.getJarDir();

        WebDriverUtil.screenshot(driver, content, fileName, savePath);
        step++;


    }
}
