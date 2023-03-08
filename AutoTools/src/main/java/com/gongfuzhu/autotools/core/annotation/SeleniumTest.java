package com.gongfuzhu.autotools.core.annotation;

import com.gongfuzhu.autotools.core.selenium.options.GeneralChromeOptions;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.chrome.ChromeOptions;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SeleniumTest {


    /**
     * 是否需要driver
     * @return
     */
    boolean isDriver() default true;

    /**
     * driver 类型
     * @return
     */
    DriverManagerType driverType() default DriverManagerType.CHROME;

    /**
     * 是否使用docker启动driver
     * @return
     */
    boolean isDocker() default false;

    /**
     * 是否获取报告
     * @return
     */
    boolean report() default false;


    ChromeOption option() default ChromeOption.notUserInfo;


    enum ChromeOption {


        notUserInfo(GeneralChromeOptions.getCapabilities()),
        userInfod(GeneralChromeOptions.getUserCapabilities()),
        H5(GeneralChromeOptions.getUserCapabilities());
        private ChromeOptions abstractDriverOptions;


        ChromeOption(ChromeOptions capabilities) {
            this.abstractDriverOptions=capabilities;

        }
        public ChromeOptions getAbstractDriverOptions(){

            return abstractDriverOptions;
        }

    }


}
