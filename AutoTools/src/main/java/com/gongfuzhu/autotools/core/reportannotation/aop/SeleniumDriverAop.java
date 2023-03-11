package com.gongfuzhu.autotools.core.reportannotation.aop;

import com.gongfuzhu.autotools.core.reportannotation.SeleniumDriver;
import com.gongfuzhu.autotools.core.selenium.WebDriverServer;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
@Import(WebDriverServer.class)
public class SeleniumDriverAop {

    @Autowired
    WebDriverServer webDriverServer;

    @Pointcut(value = "@annotation(seleniumTest)")
    public void point(SeleniumDriver seleniumTest) {
    }

    @Around("point(seleniumTest)")
    public Object doAround(ProceedingJoinPoint pjp, SeleniumDriver seleniumTest) throws Throwable {

        WebDriverServer.driverMode driverMode = WebDriverServer.getCURRENT_TaskMode().get();
        final WebDriver webDriver;
        if (null == driverMode) {

            DriverManagerType driverManagerType = seleniumTest.driverType();

            SeleniumDriver.ChromeOption option = seleniumTest.option();
            webDriver = webDriverServer.localDriver(driverManagerType, option.getAbstractDriverOptions());
        } else {

            webDriver = driverMode.getWebDriver();
        }


        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof WebDriver) {
                args[i] = webDriver;
            }
        }


        Object proceed = null;

        try {
            proceed = pjp.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
            log.fatal("seleniumException：",e);
            throw e;
        } finally {
            webDriverServer.closeDriver();
        }
        return proceed;

    }


}
