package com.gongfuzhu.report.core.service;

import com.epam.reportportal.formatting.http.converters.DefaultHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingCookieConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingUriConverter;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.restassured.ReportPortalRestAssuredLoggingFilter;
import com.gongfuzhu.autotools.core.reportannotation.Report;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
public class RestfullService {

    @Report
    public  void demo() {

        RestAssured.reset();
        RestAssured.baseURI = "https://gw.aiyilearning.com/";
        RestAssured.filters(new ReportPortalRestAssuredLoggingFilter(
                42,
                LogLevel.INFO,
                SanitizingHttpHeaderConverter.INSTANCE,
                DefaultHttpHeaderConverter.INSTANCE,
                SanitizingCookieConverter.INSTANCE,
                SanitizingUriConverter.INSTANCE
        ));


        Response post = RestAssured.given().params(
                "loginType", "Phone",
                "loginName", "15923800654",
                "app", "teachermanager").contentType(ContentType.URLENC).post();
        System.out.println(post.body().print());


    }


}

