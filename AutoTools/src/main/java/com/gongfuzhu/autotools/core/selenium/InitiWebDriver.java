package com.gongfuzhu.autotools.core.selenium;

import com.gongfuzhu.autotools.core.selenium.mode.TaskMode;
import com.gongfuzhu.autotools.core.selenium.options.ChromeGeneralOptions;
import com.gongfuzhu.autotools.core.tools.SystemTool;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.io.File;
import java.time.Duration;

@Log4j2
public class InitiWebDriver {


    private String catchPath = "/tmp/catch";


    private ThreadLocal<TaskMode> taskMode = ThreadLocal.withInitial(() -> null);
    private ThreadLocal<Capabilities> capabilities = ThreadLocal.withInitial(() -> null);


    public WebDriver getDriver() {
        if (null == taskMode.get().getWebDriver()) {
            localDriver(DriverManagerType.CHROME, ChromeGeneralOptions.getCapabilities());
        }
        return taskMode.get().getWebDriver();
    }


    /**
     * 本地浏览器
     *
     * @param type
     * @return
     */

    public WebDriver localDriver(DriverManagerType type, ChromeOptions capabilities) {
        if (null == taskMode.get()) {
            WebDriver webDriver;
            if (SystemTool.isWindos()) {

                WebDriverManager wd = WebDriverManager.getInstance(type).capabilities(capabilities);
                webDriver = wd.create();
                this.taskMode.set(new TaskMode(webDriver, wd));
            } else {
                dockerDriver(type, capabilities);
            }

        } else {
            return taskMode.get().getWebDriver();

        }
        startLinsen();
        return generalSettings(taskMode.get().getWebDriver());
    }

    /**
     * 本地docker
     *
     * @param type
     * @return
     */

    public WebDriver dockerDriver(DriverManagerType type, ChromeOptions capabilities) {


        WebDriver webDriver;
        WebDriverManager wd = WebDriverManager.getInstance(type).browserInDocker().capabilities(capabilities);

        wd.dockerTmpfsMount(catchPath + File.separator + "mount" + File.separator);

        wd.dockerScreenResolution("1920x1080x24").enableRecording().enableVnc();
        wd.dockerRecordingOutput(catchPath + File.separator + "video" + File.separator + System.currentTimeMillis() + ".mp4");
        webDriver = wd.create();
        this.taskMode.set(new TaskMode(webDriver, wd));
        return generalSettings(webDriver);

    }


    @SneakyThrows
    public void closeDriver() {
        //等待录制功能结束
        Thread.sleep(5000);
        TaskMode taskMode = this.taskMode.get();
        taskMode.getWebDriver().quit();
        taskMode.getWebDriverManager().quit();
        this.taskMode.remove();


    }

    @SneakyThrows
    private WebDriver generalSettings(WebDriver driver) {


        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));


        return driver;

    }

    @SneakyThrows
    private void startLinsen() {
        //等待录制功能启动
        Thread.sleep(5000);
        WebDriver driver = getDriver();
        MyWebDriverListener myWebDriverListener = new MyWebDriverListener(driver);

        EventFiringDecorator eventFiringDecorator = new EventFiringDecorator(myWebDriverListener);
        WebDriver decorate = eventFiringDecorator.decorate(driver);
        TaskMode taskMode1 = taskMode.get();
        taskMode1.setWebDriver(decorate);


    }


}
