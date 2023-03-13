package com.gongfuzhu.report;

import com.epam.reportportal.formatting.http.converters.DefaultHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingCookieConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingUriConverter;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.restassured.ReportPortalRestAssuredLoggingFilter;
import com.gongfuzhu.autotools.core.reportannotation.Report;
import com.gongfuzhu.report.core.controller.Testcontroller;
import com.gongfuzhu.report.core.service.RestfullService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReportApplicationTests {


    @Autowired
    Testcontroller testcontroller;
    @Test
    void contextLoads() {
        testcontroller.re();

//        testcontroller.log1();
    }



}
