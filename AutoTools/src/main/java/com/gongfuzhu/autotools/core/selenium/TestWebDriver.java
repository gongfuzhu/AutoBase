package com.gongfuzhu.autotools.core.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.SneakyThrows;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.time.Duration;

public abstract class TestWebDriver {
    @Value("${docker.catchPath}")
    private String catchPath = "/tmp/catch";
   public WebDriver webDriver;
   public Actions actions;
   public WebDriverManager webDriverManager;

    public void initDriver(DriverManagerType type, Capabilities capabilities, boolean docker) {

        if (docker) {
            webDriverManager = WebDriverManager.getInstance(type).browserInDocker();

            webDriverManager.dockerTmpfsMount(catchPath + File.separator + "mount" + File.separator);

            webDriverManager.dockerScreenResolution("1920x1080x24").enableRecording().enableVnc();
            webDriverManager.dockerRecordingOutput(catchPath + File.separator + "video" + File.separator + System.currentTimeMillis() + ".mp4");
            webDriver = webDriverManager.create();

        } else {

            webDriverManager = WebDriverManager.getInstance(type).capabilities(capabilities);

        }

        webDriver = webDriverManager.create();
        actions = new Actions(webDriver);


    }


    public void closeDriver() {

        webDriver.quit();
        webDriverManager.quit();
    }

    @SneakyThrows
    private WebDriver generalSettings(WebDriver driver) {
        //等待录制功能启动
        Thread.sleep(5000);

        MyWebDriverListener myWebDriverListener = new MyWebDriverListener();
        EventFiringDecorator eventFiringDecorator = new EventFiringDecorator(myWebDriverListener);
        WebDriver decorate = eventFiringDecorator.decorate(driver);


        decorate.manage().window().maximize();
        decorate.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        decorate.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        return decorate;

    }
}
