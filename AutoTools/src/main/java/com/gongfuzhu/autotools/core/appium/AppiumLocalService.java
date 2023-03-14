package com.gongfuzhu.autotools.core.appium;

import com.gongfuzhu.autotools.core.appium.driver.FindDevices;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;


@Log4j2
@Component
@Import(FindDevices.class)
public class AppiumLocalService {

    @Autowired
    FindDevices findDevices;

    private static AppiumDriverLocalService service;

    @PostConstruct
    private void startServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        //            builder.withAppiumJS(new File("C:\\Users\\81461\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"));
//            builder.usingAnyFreePort();
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
//        builder.withLogFile(new File(System.getProperty("user.dir") + File.separator + "logs" + File.separator + "appiumServer.log"));
        builder.usingAnyFreePort();
        builder.withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/");
        this.service = AppiumDriverLocalService.buildService(builder);

        try {
            log.info("启动AppiumServer.........");
            service.start();
            find();
        } catch (Exception e) {
            log.info("AppiumDriverLocalService 启动失败");
            e.printStackTrace();
        }

    }


    @PreDestroy
    private void stopServer() {
        log.info("尝试关闭AppiumServer");
        if (service != null) {
            service.stop();
            log.info("关闭AppiumServer");
        }
    }

    public static URL getUrl() {

        if (service != null) {

            return service.getUrl();
        }
        return null;
    }
    private void find(){
        Timer timer = new Timer();

        // 创建一个定时任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                findDevices.findAndroidDevices(getUrl());
            }
        };

        // 每隔5秒执行一次任务
        timer.schedule(task, 0, 5000);
    }





}
