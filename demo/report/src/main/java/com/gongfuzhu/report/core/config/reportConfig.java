package com.gongfuzhu.report.core.config;

import com.epam.reportportal.aspect.StepAspect;
import com.epam.reportportal.service.ReportPortal;
import com.gongfuzhu.autotools.core.annotation.agen.ReportPortalServer;
import com.gongfuzhu.autotools.core.annotation.aop.ReportAop;
import com.gongfuzhu.autotools.core.annotation.aop.SeleniumTestAop;
import com.gongfuzhu.autotools.core.annotation.aop.TestAop;
import com.gongfuzhu.autotools.core.selenium.InitiWebDriver;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Log4j2
@Import({ReportAop.class, StepAspect.class, TestAop.class, SeleniumTestAop.class})
@Configuration
public class reportConfig {

    
    @Bean
    public ReportPortal initRP(){
        return ReportPortal.builder().build();
    }
}
