
package com.gongfuzhu.autotools.core.appium.driver.manager;

import com.gongfuzhu.autotools.core.appium.driver.IDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@Log4j2
public class IOSDriverManager implements IDriver {

    private AppiumDriver driver;

    @Override
    public AppiumDriver createInstance(DesiredCapabilities capabilities, URL url) {
        driver =new IOSDriver(url, capabilities);
        return driver;
    }
}
