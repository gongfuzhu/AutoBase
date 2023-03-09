package com.gongfuzhu.autotools.core.annotation.agen;

import com.gongfuzhu.autotools.core.annotation.aop.ReportAop;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import javax.annotation.Resource;
@Component
public class RequestCompletedListener implements ApplicationListener<ServletRequestHandledEvent> {


    @Resource
    private ReportAop reportAop;
    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        event.getMethod();
        event.getRequestUrl();

//        reportAop.resetNo();
    }
}
