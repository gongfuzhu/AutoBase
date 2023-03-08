package com.gongfuzhu.autotools.core.appium.driver;

import com.gongfuzhu.autotools.core.appium.DriverModel;
import com.gongfuzhu.autotools.core.appium.android.tool.ADBUtil;
import com.gongfuzhu.autotools.core.appium.driver.manager.GeneralCapabilite;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.util.Assert;

import java.io.File;
import java.net.URL;
import java.util.*;

@Log4j2
public class FindDevices {


    private static List<DriverModel> driverModels = new ArrayList<>();

    private static Set<String> devidesList = new HashSet<>();


    /**
     * 获取设备
     * @return
     */
    public DriverModel getDriver() {

        Optional<DriverModel> first = driverModels.stream().filter(it -> !it.isUse()).findFirst();


        if (first.isEmpty()){
            Assert.isTrue(false,"当前没有设备可以操作");
        }
        first.get().setUse(true);
        return first.get();

    }

    /**
     * 查找设备
     * @return
     */

    public List<DriverModel> findAndroidDevices(URL appiumUrl) {
        log.info("当前初始化的driver：{}", driverModels);
        Set<String> result = new HashSet<String>();
        String android = "ANDROID";
        String android_home = System.getenv("ANDROID_HOME");
        if (null == android_home) {
            log.error("请设置android环境变量：ANDROID_HOME");
        }

        File file = new File(android_home + File.separator + "platform-tools");

        Set<String> list = ADBUtil.list(file);

        //新增的设备

        result.clear();
        result.addAll(list);
        result.removeAll(devidesList);
        devidesList.addAll(result);


        try {
            for (String s : result) {
                log.info("添加设备：{}", s);

                DriverModel driverModel = initDriver(android, s,appiumUrl);
                driverModels.add(driverModel);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }


        //移除的设备
        result.clear();
        result.addAll(devidesList);
        result.removeAll(list);
        devidesList.removeAll(result);

        for (String s : result) {
            log.info("移除设备：{}", s);
            driverModels.removeIf(it -> it.getUuid() == s);

        }


        return this.driverModels;
    }


    /**
     * 获取设备时间 防止session过期
     */

    public void dontSleep() {
        if (!driverModels.isEmpty()) {


            driverModels.forEach(it -> it.getAppiumDriver().getSessionId());


        }


    }

    private DriverModel initDriver(String platform, String UUID, URL appiumUrl) {
        DriverModel driverModel = new DriverModel();
         DriverFactory driverFactory=new DriverFactory();

        GeneralCapabilite generalCapabilite=new GeneralCapabilite();

        DesiredCapabilities capabilite = generalCapabilite.capabilite(platform, UUID);


        driverModel.setAppiumDriver(driverFactory.createInstance(platform, capabilite, appiumUrl));
        driverModel.setUuid(UUID);
        driverModel.setPlatform(platform);


        return driverModel;


    }
}
