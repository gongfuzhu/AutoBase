package com.gongfuzhu.report.core.service;

import com.epam.reportportal.formatting.http.converters.DefaultHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingCookieConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingUriConverter;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.restassured.ReportPortalRestAssuredLoggingFilter;
import com.gongfuzhu.autotools.core.reportannotation.Report;
import com.gongfuzhu.autotools.core.reportannotation.TestMethod;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.stereotype.Service;

@Service
public class RestfullService {

    @TestMethod(testName = "form",desc = "登录")
    public void demo() {

        Response post = RestAssured.given()
                .formParam("grant_type", "password")
                .formParam("username", "superadmin")
                .formParam("password", "erebus")
                .contentType(ContentType.URLENC)
//                .header("Authorization","Basic dWk6dWltYW4=")
                .post("uat/sso/oauth/token");
        post.then().statusCode(200);
        System.out.println(post.body().print());


    }
    @TestMethod(testName = "json")
    public void json() {

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


        Response post = RestAssured.given()
                .formParam("loginType", "Phone")
                .formParam("loginName", "15923800654")
                .formParam("app", "teachermanager")
                .contentType(ContentType.URLENC)
                .header("key","value")
                .post("ucenter/user/getLoginToken");
        System.out.println(post.body().print());


    }


}

