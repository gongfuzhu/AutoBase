package com.gongfuzhu.report.core.step;

import com.epam.reportportal.annotations.ParameterKey;
import com.epam.reportportal.annotations.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class Restfull {

    @Step
    public String login(@ParameterKey Map<String, Object> map) {

        RequestSpecification given = RestAssured.given();

        map.forEach((key, value) -> {
            given.formParam(key, value);

        });
        Response authorization = given.contentType(ContentType.URLENC)
                .header("Authorization", "Basic dWk6dWltYW4=")
                .post("uat/sso/oauth/token");


        String accessToken = authorization.path("access_token");


        return accessToken;


    }

    @Step
    public List launchList(String token) {
        Response response = RestAssured
                .given()
                .formParam("page.page", "1")
                .formParam("page.size", "100")
//                .formParam("page.sort", "startTime%2Cnumber%2CDESC")
                .header("Authorization", "bearer " + token)
                .contentType(ContentType.URLENC)
                .get("api/v1/superadmin_personal/launch");


        List list = response.path("content");
        return list;


    }


}
