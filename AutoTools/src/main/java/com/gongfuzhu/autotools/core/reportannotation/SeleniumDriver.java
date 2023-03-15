package com.gongfuzhu.autotools.core.reportannotation;

import com.gongfuzhu.autotools.core.selenium.options.ChromeGeneralOptions;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Order(3)
public @interface SeleniumDriver {



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


    ChromeOption option() default ChromeOption.Chrome_NotUserInfoDriver;


    enum ChromeOption {
        Chrome_NotUserInfoDriver(ChromeGeneralOptions.getCapabilities()),
        Chrome_UserInfodDriver(ChromeGeneralOptions.getUserCapabilities()),
        Chrome_H5Driver(ChromeGeneralOptions.getH5Capabilities()),
        Chrome_CacheDriver(ChromeGeneralOptions.getCacheCapabilities());
        private ChromeOptions abstractDriverOptions;


        ChromeOption(ChromeOptions capabilities) {
            this.abstractDriverOptions=capabilities;

        }
        public ChromeOptions getAbstractDriverOptions(){

            return abstractDriverOptions;
        }

    }


}
