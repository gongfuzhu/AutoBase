package com.gongfuzhu.autotools.core.tools;

import com.google.common.net.MediaType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.devtools.v108.network.Network;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;

import java.io.File;

public class SystemTool {


    /**
     * 随机获得可用的端口
     */

    public static int getFreeProt() {
        return PortProber.findFreePort();
    }


    public static boolean isWindos(){
        if (System.getProperty("os.name").contains("dows")){
            return true;
        }
        return false;
    }

    public static String projectPath(){
        return System.getProperty("user.dir");
    }



    public static String getPath() {

        String path = SystemTool.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        if (System.getProperty("os.name").contains("dows")) {

            path = path.substring(1, path.length());

        } else if (path.contains("jar")) {

            path = path.substring(0, path.lastIndexOf("."));

            return path.substring(0, path.lastIndexOf("/"));
        }


        return path.replace("target/classes/", "");


    }

    /**
     * 获取jar包所在路径
     *
     * @return
     */
    public static String getJarPath() {
        String path = SystemTool.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8"); // 转换处理中文及空格
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return new File(path).getAbsolutePath();
    }







}
