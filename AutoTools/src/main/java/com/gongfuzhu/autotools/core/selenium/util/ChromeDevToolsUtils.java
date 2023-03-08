package com.gongfuzhu.autotools.core.selenium.util;

import com.google.common.net.MediaType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.devtools.v108.emulation.Emulation;
import org.openqa.selenium.devtools.v108.fetch.Fetch;
import org.openqa.selenium.devtools.v108.fetch.model.RequestId;
import org.openqa.selenium.devtools.v108.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v108.log.Log;
import org.openqa.selenium.devtools.v108.network.model.ConnectionType;
import org.openqa.selenium.devtools.v108.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v108.network.model.ResponseReceived;
import org.openqa.selenium.devtools.v108.performance.Performance;
import org.openqa.selenium.devtools.v108.performance.model.Metric;
import org.openqa.selenium.devtools.v108.network.Network;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.openqa.selenium.remote.http.Contents.utf8String;

// https://rahulshettyacademy.com/blog/index.php/2021/11/04/selenium-4-key-feature-network-interception/
@Log4j2
public class ChromeDevToolsUtils {




    // 模拟网速
    public static void networkSimulation(WebDriver webDriver) {
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        DevTools devTools = ((HasDevTools) webDriver).getDevTools();
        devTools.createSession();
        Boolean send = devTools.send(Network.canEmulateNetworkConditions());
        if (send) {

            devTools.send(Network.emulateNetworkConditions(false, 0, 1024, 1024, Optional.empty()));

        } else {
            log.info("浏览器不支持网络模拟");
        }


    }


    /**
     * 网络监听
     *
     * @param webDriver
     */

    public static void networkMonitoring(WebDriver webDriver) {

        DevTools devTool = ((HasDevTools) webDriver).getDevTools();

        devTool.createSession();
        devTool.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTool.addListener(Network.requestWillBeSent(),
                requestWillBeSent -> {
                    String method = requestWillBeSent.getRequest().getMethod().toLowerCase();
                    if (method.equals("post")) {

                        log.info("******************************************请求*************************************************");
                        log.info("请求(post) url：{}", requestWillBeSent.getRequest().getUrl());
                        log.info("请求参数：{}", requestWillBeSent.getRequest().getPostData().get());
                        log.info("请求头：{}", requestWillBeSent.getRequest().getHeaders().toString());


                    } else if (method.equals("get")) {
                        log.info("请求(get)url：{}", requestWillBeSent.getRequest().getUrl());
                        log.info("请求头：{}", requestWillBeSent.getRequest().getHeaders().toString());

                    }


                }

        );
        devTool.addListener(Network.responseReceived(), responseReceived -> {
                    log.info("******************************************响应*************************************************");
                    log.info("响应URL：{}", responseReceived.getResponse().getUrl());
                    log.info("响应参数：{}", devTool.send(Network.getResponseBody(responseReceived.getRequestId())).getBody());
                    log.info("响应头：{}", responseReceived.getResponse().getHeaders().toString());
                }


        );


    }

    public static void networkMonitoring(WebDriver webDriver, Consumer<RequestWillBeSent> request, Consumer<ResponseReceived> response) {

        DevTools devTool = ((HasDevTools) webDriver).getDevTools();
        devTool.createSession();

        devTool.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTool.addListener(Network.requestWillBeSent(), request);
        devTool.addListener(Network.responseReceived(), response);


    }

    /**
     * 获取指定请求的出入参（待验证）
     *
     * @param webDriver
     * @param urlPattern
     */
    public static void urlBody(WebDriver webDriver, String urlPattern) {
        DevTools devTools = ((ChromeDriver) webDriver).getDevTools();
        devTools.createSessionIfThereIsNotOne();


        List<RequestPattern> requestPatterns = List.of(new RequestPattern(Optional.of(urlPattern), Optional.empty(), Optional.empty()), new RequestPattern(Optional.of(urlPattern), Optional.empty(), Optional.empty()));
        devTools.send(Fetch.enable(Optional.of(requestPatterns), Optional.empty()));
        devTools.addListener(Fetch.requestPaused(), request -> {
            RequestId requestId = request.getRequestId();
            Optional<String> postData = request.getRequest().getPostData();
            log.info("请求数据：{}", postData.get());
            System.out.println(String.format("请求数据：%s", postData.get()));
            devTools.send(Fetch.continueRequest(request.getRequestId(),
                    Optional.of(request.getRequest().getUrl()),
                    Optional.of(request.getRequest().getMethod()),
                    Optional.of(request.getRequest().getPostData().get()),
                    Optional.of(request.getResponseHeaders().get()),
                    Optional.of(true)));
            Fetch.GetResponseBodyResponse send = devTools.send(Fetch.getResponseBody(requestId));
            log.info("响应数据：{}", send.getBody());


        });


    }

    /**
     * 监听console
     */
    public void consoleLinsen(ChromeDriver driver) {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Log.enable());
        devTools.addListener(Log.entryAdded(),
                logEntry -> {
                    log.info("text：{}", logEntry.getText());
                    log.info("log:{}" + logEntry.getText());
                    log.info("level:{}" + logEntry.getLevel());
                });
    }

    /**
     * 获取性能指标
     *
     * @param driver
     * @return
     */

    public List<Metric> performanceMetricsExample(ChromeDriver driver) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Performance.enable(Optional.empty()));
        List<Metric> metricList = devTools.send(Performance.getMetrics());


//        for(Metric m : metricList) {
//            System.out.println(m.getName() + " = " + m.getValue());
//        }

        return metricList;
    }


    public void phone(ChromeDriver driver) {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
// iPhone 11 Pro dimensions
        devTools.send(Emulation.setDeviceMetricsOverride(375,
                812,
                50,
                true,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()));

    }

    public static void networ(ChromeDriver driver) {
        NetworkInterceptor interceptor = new NetworkInterceptor(
                driver,
                Route.matching(req -> req.getUri().equals(""))
                        .to(() -> req -> new HttpResponse()
                                .setStatus(200)
                                .addHeader("Content-Type", MediaType.HTML_UTF_8.toString())
                                .setContent(utf8String("Creamy, delicious cheese!"))));

        driver.get("https://example-sausages-site.com");

        String source = driver.getPageSource();

//        assertThat(source).contains("delicious cheese!");
    }


    public static ArrayList<JavascriptException> jsExceptionsExample(ChromeDriver driver) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        ArrayList<JavascriptException> jsExceptionsList = new ArrayList<>();
        Consumer<JavascriptException> addEntry = jsExceptionsList::add;
        devTools.getDomains().events().addJavascriptExceptionListener(addEntry);

//        driver.get("<your site url>");
//
//        WebElement link2click = driver.findElement(By.linkText("<your link text>"));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
//                link2click, "onclick", "throw new Error('Hello, world!')");
//        link2click.click();
//
//        for (JavascriptException jsException : jsExceptionsList) {
//            System.out.println("JS exception message: " + jsException.getMessage());
//            System.out.println("JS exception system information: " + jsException.getSystemInformation());
//            jsException.printStackTrace();
//        }

        return jsExceptionsList;
    }


    private void request(RequestWillBeSent requestWillBeSent) {

        log.info("请求id：{}", requestWillBeSent.getRequestId());
        log.info("请求url：{}", requestWillBeSent.getRequest().getUrl());
        log.info("请求头：{}", requestWillBeSent.getRequest().getHeaders().toJson());
        log.info("请求内容：{}", requestWillBeSent.getRequest().getPostData().toString());


    }


}

