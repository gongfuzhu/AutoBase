package com.gongfuzhu.report.core.step;

import com.epam.reportportal.annotations.Step;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class TestSetp {

    @Step(description = "這是描述")
    public void test(){
        log.info("dddddddddddddddddddddddddddddddddddddd-步驟内");

    }

}
