package com.gongfuzhu.report.core.step;

import com.epam.reportportal.annotations.Step;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.service.ReportPortal;
import com.gongfuzhu.autotools.core.reportannotation.aop.ReportPortalServer;
import com.gongfuzhu.autotools.core.tools.LoggingTools;
import com.google.common.io.Resources;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
@Log4j2
public class SendLog {

    @SneakyThrows
    @Step(value = "上传图片")
    public void jpg(){
        log.info("上传图片");
        File file = new File(Resources.getResource("files/1212.jpg").toURI());
        ReportPortal.emitLog("方法一", "error", new Date(), file);
        LoggingTools.log(file,"方法二");
        ReportPortalServer.getCURRENT_ReportPortalServer().get().sendLog("方法三", LogLevel.ERROR,file);


    }
    @SneakyThrows
    @Step()
    public void css(){
        log.info("上传css");
        File file = new File(Resources.getResource("files/css.css").toURI());
        ReportPortal.emitLog("方法一", "error", new Date(), file);
        LoggingTools.log(file,"方法二");
        ReportPortalServer.getCURRENT_ReportPortalServer().get().sendLog("方法三", LogLevel.ERROR,file);

    }

    @SneakyThrows
    @Step()
    public void zip(){
        log.info("上传css");
        File file = new File(Resources.getResource("files/demo.zip").toURI());
        ReportPortal.emitLog("方法一", "error", new Date(), file);
        LoggingTools.log(file,"方法二");
        ReportPortalServer.getCURRENT_ReportPortalServer().get().sendLog("方法三", LogLevel.ERROR,file);

    }

    @SneakyThrows
    @Step()
    public void exception(){
        log.info("上传css");
        File file = new File(Resources.getResource("files/ssdemo.zip").toURI());
        ReportPortal.emitLog("方法一", "error", new Date(), file);
        LoggingTools.log(file,"方法二");
        ReportPortalServer.getCURRENT_ReportPortalServer().get().sendLog("方法三", LogLevel.ERROR,file);

    }

    public void assertFail(){
        log.info("下面是true断言");
        // 使用Junit 断言
        Assert.fail();
//        Assert.isTrue(false,"断言失败消息");




    }

}