package com.gongfuzhu.report.core.service;

import com.epam.reportportal.formatting.http.converters.DefaultHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingCookieConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingHttpHeaderConverter;
import com.epam.reportportal.formatting.http.converters.SanitizingUriConverter;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.restassured.ReportPortalRestAssuredLoggingFilter;
import com.gongfuzhu.autotools.core.reportannotation.TestMethod;
import com.gongfuzhu.report.core.step.Restfull;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@Service
@Log4j2
public class RestfullService {

    @Autowired
    Restfull restfull;
    @TestMethod(testName = "获取launch 列表",desc = "接口测试")
    public void demo() {


        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("grant_type","password");
        stringObjectHashMap.put("username","superadmin");
        stringObjectHashMap.put("password","erebus");
        String token = restfull.login(stringObjectHashMap);

        log.info("token:{}",token);

        List list = restfull.launchList(token);

        list.forEach(it->{
            log.info(it);

        });


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

