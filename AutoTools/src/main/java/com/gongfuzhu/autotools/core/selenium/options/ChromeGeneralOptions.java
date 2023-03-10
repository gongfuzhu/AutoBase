package com.gongfuzhu.autotools.core.selenium.options;

import com.gongfuzhu.autotools.core.selenium.options.arguments.ChromeArguments;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

public class ChromeGeneralOptions {


    /**
     * 没有用户信息
     * @return
     */
    public static ChromeOptions getCapabilities(){

        ChromeOptions chromeOptions = new ChromeOptions();


        //加载用户信息
        chromeOptions.addArguments( ChromeArguments.disableBlinkFeatures, ChromeArguments.automationControlled);
        //禁止弹窗提示
        chromeOptions.setExperimentalOption("excludeSwitches",new String[]{"enable-automation"});
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        return chromeOptions;

    }

    /**
     * 加载有用户信息的浏览器
     * @return
     */

    public static ChromeOptions getUserCapabilities(){

        ChromeOptions chromeOptions = new ChromeOptions();



        chromeOptions.addArguments(ChromeArguments.userDataDir, ChromeArguments.disableBlinkFeatures, ChromeArguments.automationControlled);
        //禁止弹窗提示
        chromeOptions.setExperimentalOption("excludeSwitches",new String[]{"enable-automation"});
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        return chromeOptions;

    }


    /**
     * 加载H5
     */
    public static ChromeOptions getH5Capabilities(){
        HashMap<String, String> mobileEmulation  = new HashMap<>();
        String deviceName = "Galaxy S5";	//iPhone X/Galaxy S5
        mobileEmulation.put("deviceName",deviceName);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation",mobileEmulation);
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        return chromeOptions;

    }



}