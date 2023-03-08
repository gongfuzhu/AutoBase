package com.gongfuzhu.autotools.core.tools;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class VideoTool {

    /**
     * 下载网络视频
     * @param videoUrl
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean downloadNet(String videoUrl,String path) throws IOException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(videoUrl);

        URLConnection conn = url.openConnection();
        InputStream inStream = conn.getInputStream();
        FileOutputStream fs = new FileOutputStream(path);
        byte[] buffer = new byte[1204];
        int length;
        while ((byteread = inStream.read(buffer)) != -1) {
            bytesum += byteread;
            fs.write(buffer, 0, byteread);
        }
        fs.close();

        return new File(path).exists();
    }
}
