package com.gongfuzhu.report.core.config;

import com.epam.reportportal.aspect.StepAspect;
import com.gongfuzhu.pcore.mvc.MVCConfiguration;
import com.gongfuzhu.pcore.mvc.MVCResponseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({MVCConfiguration.class, MVCResponseConfiguration.class})
@Configuration
public class MVCConfig {
}
