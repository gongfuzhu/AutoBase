package com.gongfuzhu.report.core.config;

import com.epam.reportportal.aspect.StepAspect;
import com.epam.reportportal.service.ReportPortal;
import com.gongfuzhu.autotools.core.reportannotation.aop.ReportAop;
import com.gongfuzhu.autotools.core.reportannotation.aop.SeleniumDriverAop;
import com.gongfuzhu.autotools.core.reportannotation.aop.TestMethodAop;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Log4j2
@Import({ReportAop.class, StepAspect.class, TestMethodAop.class, SeleniumDriverAop.class})
@Configuration
public class ReportConfig {

    
    @Bean
    public ReportPortal initRP(){
        return ReportPortal.builder().build();
    }
}
