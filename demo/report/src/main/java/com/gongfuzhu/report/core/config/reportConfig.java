package com.gongfuzhu.report.core.config;

import com.epam.reportportal.aspect.StepAspect;
import com.epam.reportportal.service.ReportPortal;
import com.gongfuzhu.autotools.core.annotation.agen.ReportPortalServer;
import com.gongfuzhu.autotools.core.annotation.aop.ReportAop;
import com.gongfuzhu.autotools.core.annotation.aop.TestAop;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Log4j2
@Import({ReportAop.class, StepAspect.class, TestAop.class})
@Configuration
public class reportConfig {

    
    @Bean
    public ReportPortal initRP(){


        log.info("前面你被执行了");
        return ReportPortal.builder().build();
    }
}
