package com.gongfuzhu.autotools.core.appium.driver;

import com.gongfuzhu.autotools.core.appium.driver.manager.AndroidDriverManager;
import com.gongfuzhu.autotools.core.appium.driver.manager.IOSDriverManager;
import com.gongfuzhu.autotools.core.exception.PlatformNotSupportedException;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Duration;

@Component
public class DriverFactory {

    public AppiumDriver createInstance(String platform, DesiredCapabilities capabilities, URL url) {


        AppiumDriver driver;
        Platform mobilePlatform = Platform.valueOf(platform.toUpperCase());

        switch (mobilePlatform) {
            case IOS:
                driver = new IOSDriverManager().createInstance(capabilities, url);
                break;

            case ANDROID:
                driver = new AndroidDriverManager().createInstance(capabilities, url);
                break;

            default:
                throw new PlatformNotSupportedException(
                        "Platform not supported! Check if you set ios or android on the parameter.");
        }

//        MyWebDriverListener myWebDriverListener = new MyWebDriverListener();
//        EventFiringDecorator eventFiringDecorator = new EventFiringDecorator(myWebDriverListener);
//        WebDriver decorate = eventFiringDecorator.decorate(driver);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        return driver;
    }
}
