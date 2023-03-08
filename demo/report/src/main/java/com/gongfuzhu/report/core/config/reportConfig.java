package com.gongfuzhu.report.core.config;

import com.epam.reportportal.aspect.StepAspect;
import com.gongfuzhu.autotools.core.annotation.agen.ReportPortalServer;
import com.gongfuzhu.autotools.core.annotation.aop.ReportAop;
import com.gongfuzhu.autotools.core.annotation.aop.TestAop;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Log4j2
@Import({ReportAop.class, StepAspect.class, TestAop.class})
@Component
public class reportConfig {

    
    @Bean
    private ReportPortalServer initRP(){

        log.info("前面你被执行了");
        return ReportPortalServer.reportPortal("http://222.180.202.110:4141", "9b577604-4df6-4205-ad62-3327b2969624", "superadmin_personal", "xx-系统", "这是xx系统描述或备注");
    }
}
