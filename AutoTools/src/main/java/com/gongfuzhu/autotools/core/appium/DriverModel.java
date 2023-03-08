package com.gongfuzhu.autotools.core.appium;

import io.appium.java_client.AppiumDriver;
import lombok.Data;

@Data
public class DriverModel {



    private AppiumDriver appiumDriver;


    private String uuid;


    private String platform;


    private boolean use;



}
