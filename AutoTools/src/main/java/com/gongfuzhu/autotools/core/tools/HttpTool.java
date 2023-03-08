package com.gongfuzhu.autotools.core.tools;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpTool {

    public static String getRequest(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }
    }

    public static String postJsonRequest(String url, String jsonPayload) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonPayload);
        request.setEntity(entity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }


    }

    public static String postFormRequest(String url, String formPayload) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        StringEntity entity = new StringEntity(formPayload);
        request.setEntity(entity);
        request.setHeader("Accept", "*/*");
        request.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }


    }

}



