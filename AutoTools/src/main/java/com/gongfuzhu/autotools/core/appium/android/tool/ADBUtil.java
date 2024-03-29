package com.gongfuzhu.autotools.core.appium.android.tool;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class ADBUtil {

    /**
     * 获取adb 路径
     * @return
     */
    public static File adbPath(){

        String android_home = System.getenv("ANDROID_HOME");
        if (null==android_home){
            log.error("请设置android环境变量：ANDROID_HOME");
        }

        return new File(android_home + File.separator + "platform-tools");

    }



    /**
     * 重启adb
     *
     * @param home
     */
    public static void restartADB(File home) {
        runAdb(home, "kill-server");
        runAdb(home, "start-server");
    }


    /**
     * 获取所有的设备的连接名
     *
     * @param home
     * @return
     */
    public static Set<String> list(File home) {
        //取出所有的设备
        List<String> devices = Arrays.stream(runAdb(home, "devices").split("\r\n")).map((it) -> {
            return it.split("\t")[0];
        }).collect(Collectors.toList());
        //删除一个
        devices.remove(0);
        return new HashSet<>(devices);
    }


    /**
     * 获取设备详情
     *
     * @param home
     * @param deviceName
     * @return
     */
    public static AndroidDeviceInfo getInfo(File home, String deviceName) {
        Map<String, String> ret = new HashMap<>();
        String[] lines = runAdb(home, "-s", deviceName, "shell", "getprop").split("\r\n");
        Arrays.stream(lines).forEach((line) -> {
            int at = line.indexOf(":");
            if (at > -1) {
                String key = line.substring(0, at).trim();
                String value = line.substring(at + 1, line.length()).trim();
                ret.put(formatInfoChar(key, "\\[", "\\]"), formatInfoChar(value, "\\[", "\\]"));
            }
        });


        AndroidDeviceInfo androidDeviceInfo = new AndroidDeviceInfo();
        androidDeviceInfo.setVersion(ret.get("ro.build.version.release"));
        androidDeviceInfo.setSdk(ret.get("ro.build.version.sdk"));
        androidDeviceInfo.setProductModel(ret.get("ro.product.model"));
        androidDeviceInfo.setProductBrand(ret.get("ro.product.brand"));
        androidDeviceInfo.setSerialno(ret.get("ro.serialno"));
        androidDeviceInfo.setMac(getMac(home, deviceName));

        return androidDeviceInfo;
    }

    /**
     * 获取mac地址
     *
     * @param home
     * @param deviceName
     * @return
     */
    public static String getMac(File home, String deviceName) {
        String info = runAdb(home, "-s", deviceName, "shell", "cat", "/sys/class/net/wlan0/address");
        if (!StringUtils.hasText(info)) {
            info = runAdb(home, "-s", deviceName, "shell", "cat", "/sys/class/net/eth0/address");
        }

        //格式化mac地址
        if (StringUtils.hasText(info)) {
            return info.replaceAll(":", "").trim().toUpperCase();
        }

        return info;
    }

    /**
     * 删除收尾字符
     *
     * @param ret
     * @return
     */
    private static String formatInfoChar(String ret, String... cleanChar) {
        if (!StringUtils.hasText(ret)) {
            return null;
        }
        String info = ret.trim();
        if (cleanChar != null) {
            for (String s : cleanChar) {
                info = info.replaceAll(s, "");
            }
        }
        return info;
    }


    @SneakyThrows
    public static String runAdb(File home, String... cmds) {
        return command(home, "adb.exe", cmds);
    }

    @SneakyThrows
    private static String command(File home, String fileName, String... cmds) {
        String filePath = home.getAbsolutePath() + "/" + fileName;
        if (!new File(filePath).exists()) {
            return null;
        }
        List<String> cmdLines = new ArrayList() {{
            add("cmd");
            add("/c");
            add(FilenameUtils.normalize(filePath));
            addAll(List.of(cmds));
        }};
        log.debug("cmd : {}", org.apache.commons.lang3.StringUtils.join(cmdLines.toArray(new String[0]), " "));
        Process p = Runtime.getRuntime().exec(cmdLines.toArray(new String[0]));
        @Cleanup InputStream inputStream = p.getInputStream();
        String ret = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
        p.waitFor(5, TimeUnit.SECONDS);
        return ret.trim();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AndroidDeviceInfo {

        //系统版本
        private String version;

        //系统api版本
        private String sdk;

        //手机设备型号
        private String productModel;

        //手机厂商名称
        private String productBrand;

        //手机序列号
        private String serialno;

        //mac地址
        private String mac;

    }


}
