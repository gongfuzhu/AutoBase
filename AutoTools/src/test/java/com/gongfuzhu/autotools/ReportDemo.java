package com.gongfuzhu.autotools;

import com.epam.reportportal.annotations.Step;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.message.HashMarkSeparatedMessageParser;
import com.epam.reportportal.message.ReportPortalMessage;
import com.epam.reportportal.message.TypeAwareByteSource;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.service.ReportPortalClient;
import com.epam.reportportal.service.tree.TestItemTree;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.launch.Mode;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.json.Json;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Log4j2
public class ReportDemo {

    public static void main(String[] args) {
        ReportDemo reportDemo = new ReportDemo();
        new ReportDemo().demo();
        reportDemo.test1();
//        new ReportDemo().demo2();
    }


    public void demo2(){

        ItemAttributesRQ itemAttributesRQ = new ItemAttributesRQ();
        itemAttributesRQ.setSystem(false);
        itemAttributesRQ.setKey("属性");
        itemAttributesRQ.setValue("属性值1111111111");


        ListenerParameters listenerParameters = new ListenerParameters();
        listenerParameters.setBaseUrl("http://192.168.8.55:8080");
        listenerParameters.setApiKey("1f3e3263-8402-4330-a696-32eb0b20ea69");
        listenerParameters.setLaunchName("第一次测试_LaunchName");
        listenerParameters.setProjectName("superadmin_personal");
        listenerParameters.setDescription("这是这次测试的总体描述");
        listenerParameters.setAttributes(Set.of(itemAttributesRQ));
        listenerParameters.setHttpLogging(false);
        listenerParameters.setLaunchRunningMode(Mode.DEFAULT);
        listenerParameters.setEnable(true);

        ReportPortal reportPortal = ReportPortal.builder()
                .withParameters(listenerParameters)
                .build();
        StartLaunchRQ startLaunchRQ = buildStartLaunchRq(listenerParameters);
        startLaunchRQ.setStartTime(Calendar.getInstance().getTime());


        Launch launch = reportPortal.newLaunch(startLaunchRQ);
//      1.  创建launch  代表一次代码执行
        Maybe<String> start = launch.start();



    }

    @SneakyThrows
    public void demo() {
        ItemAttributesRQ itemAttributesRQ = new ItemAttributesRQ();
        itemAttributesRQ.setSystem(false);
        itemAttributesRQ.setKey("属性");
        itemAttributesRQ.setValue("属性值");

        ItemAttributesRQ startLaunchRQ = new ItemAttributesRQ();
        startLaunchRQ.setSystem(false);
        startLaunchRQ.setKey("launchRQ");
        startLaunchRQ.setValue("value--gongfuzhu");

        ListenerParameters listenerParameters = new ListenerParameters();
        listenerParameters.setBaseUrl("http://192.168.8.55:8080");
        listenerParameters.setApiKey("9b577604-4df6-4205-ad62-3327b2969624");
        listenerParameters.setLaunchName("listener_LaunchName");
        listenerParameters.setProjectName("superadmin_personal");
        listenerParameters.setDescription("这是这次测试的总体描述");
        listenerParameters.setAttributes(Set.of(itemAttributesRQ));
        listenerParameters.setHttpLogging(false);
        listenerParameters.setLaunchRunningMode(Mode.DEFAULT);
        listenerParameters.setEnable(true);
        listenerParameters.setCallbackReportingEnabled(true);

        ReportPortal reportPortal = ReportPortal.builder()
                .withParameters(listenerParameters)
                .build();
        StartLaunchRQ launchRQ = new StartLaunchRQ();
        launchRQ.setName("launchRQ_launchName111");
        launchRQ.setStartTime(Calendar.getInstance().getTime());
        launchRQ.setAttributes(Set.of(startLaunchRQ));
        launchRQ.setMode(listenerParameters.getLaunchRunningMode());
        launchRQ.setRerun(listenerParameters.isRerun());
        launchRQ.setDescription("這個是描述launch_name");
//        launchRQ.setRerunOf("");



        Launch launch = reportPortal.newLaunch(launchRQ);
//      1.  创建launch  代表一次代码执行
        Maybe<String> start = launch.start();

////        创建测试树
        TestItemTree ITEM_TREE = new TestItemTree();
        ITEM_TREE.setLaunchId(start);


////       2. 创建测试单元
        StartTestItemRQ SUITE = new StartTestItemRQ();
        SUITE.setType("SUITE");
        SUITE.setName("这是第一个_sutie");
        SUITE.setStartTime(Calendar.getInstance().getTime());
        SUITE.setDescription("这个是单元测试类似testNG的suite");
        Maybe<String> suiteId1 = launch.startTestItem(SUITE);

        ITEM_TREE.getTestItems().put(TestItemTree.ItemTreeKey.of("测试单元名称"), TestItemTree.createTestItemLeaf(suiteId1));


        StartTestItemRQ SUITE2 = new StartTestItemRQ();
        SUITE2.setType("SUITE");
        SUITE2.setName("这是第二个suite");
        SUITE2.setStartTime(Calendar.getInstance().getTime());
        SUITE2.setDescription("这是第一个suite的描述");
        Maybe<String> suiteId2 = launch.startTestItem(SUITE2);

//        创建测试
        StartTestItemRQ testItemRQ = new StartTestItemRQ();
        testItemRQ.setType("TEST");
        testItemRQ.setName("测试用例名称");
        testItemRQ.setStartTime(Calendar.getInstance().getTime());
        testItemRQ.setDescription("这个是测试用例描述");

        Maybe<String> testId = launch.startTestItem(suiteId1, testItemRQ);
//        Maybe<String> testId2 = launch.startTestItem(suiteId2, testItemRQ);


        // 消息
        ReportPortalClient client = reportPortal.getClient();
        SaveLogRQ saveLogRQ = new SaveLogRQ();
        saveLogRQ.setItemUuid(testId.blockingGet());
        saveLogRQ.setLaunchUuid(start.blockingGet());
        saveLogRQ.setLogTime(Calendar.getInstance().getTime());
        saveLogRQ.setLevel("error");
        saveLogRQ.setMessage("这是日志消息-没有附件");
        client.log(saveLogRQ).subscribe();

        Json json = new Json();
        System.out.println(json.toJson(saveLogRQ));


        // 图片
        FileInputStream fileInputStream = new FileInputStream("D:\\sysfile\\Pictures\\1212.jpg");


        String read="RP_ME1SSAGE#BASE64#" + Base64.getEncoder().encodeToString(fileInputStream.readAllBytes()) + "#这是描述";
        System.out.println(read);


        SaveLogRQ.File fileBase = new SaveLogRQ.File();
        ReportPortalMessage parse = new HashMarkSeparatedMessageParser().parse(read);


        TypeAwareByteSource data = parse.getData();
        String message = parse.getMessage();

        fileBase.setContent(data.read());
        fileBase.setName("1212.jpg");
        fileBase.setContentType(data.getMediaType());
        System.out.println(data.read().toString());

        SaveLogRQ logRequest = new SaveLogRQ();
        logRequest.setLogTime(Calendar.getInstance().getTime());
        logRequest.setFile(fileBase);
        logRequest.setItemUuid(testId.blockingGet());
        logRequest.setLaunchUuid(start.blockingGet());
        logRequest.setLevel("info");
        logRequest.setMessage(message+"log.base64-有附件");
        client.log(logRequest).subscribe();

//        log.info("这个是测试");



        String fileFb="RP_MESSAGE#FILE#"+new File("D:\\sysfile\\Pictures\\1212.jpg").getAbsolutePath()+"#这是file";
        System.out.println(fileFb);
        ReportPortalMessage parse2 = new HashMarkSeparatedMessageParser().parse(fileFb);

        SaveLogRQ.File fileF = new SaveLogRQ.File();
        fileF.setName("1212.jpg");
        fileF.setContentType(parse2.getData().getMediaType());
        fileF.setContent(parse2.getData().read());

        SaveLogRQ logRequest2 = new SaveLogRQ();
        logRequest2.setFile(fileF);
        logRequest2.setLogTime(Calendar.getInstance().getTime());
        logRequest2.setFile(fileBase);
        logRequest2.setItemUuid(testId.blockingGet());
        logRequest2.setLaunchUuid(start.blockingGet());
        logRequest2.setLevel("INFO");
        logRequest2.setMessage("info.file，有附件");
        Disposable subscribe = client.log(logRequest2).subscribe();
        System.out.println(subscribe.toString());

//        ReportPortal.emitLog(it->{
//
//            SaveLogRQ dd = new SaveLogRQ();
//            dd.setMessage("拉姆达表达式");
//            dd.setLevel("info");
//            dd.setItemUuid(it);
//            dd.setLogTime(Calendar.getInstance().getTime());
//            System.out.println(dd);
//
//            return dd;
//        });

//        new TestItemRequest();
//        ItemTreeReporter.sendLog(reportPortal.getClient(),"error","这是消息体",Calendar.getInstance().getTime(),launch.getLaunch(), testId2.)



        // 提交测试结果和日志
//
        ReportPortal.emitLog("发送日志，我在哪里", "", Calendar.getInstance().getTime());

        Optional.ofNullable(ITEM_TREE.getTestItems().get(TestItemTree.ItemTreeKey.of("这是测试用例标题"))).ifPresent((suiteLeaf) -> {
            ConcurrentHashMap<TestItemTree.ItemTreeKey, TestItemTree.TestItemLeaf> testClassesMapping = new ConcurrentHashMap(3);
            TestItemTree.TestItemLeaf put = testClassesMapping.put(TestItemTree.ItemTreeKey.of("这个又是什么"), TestItemTree.createTestItemLeaf(testId, new ConcurrentHashMap()));

            suiteLeaf.getChildItems().put(TestItemTree.ItemTreeKey.of("用例下的名称？"), TestItemTree.createTestItemLeaf(testId, testClassesMapping));
        });


//
//        Date now = Calendar.getInstance().getTime();
//
//        FinishTestItemRQ finishTestItemRQ = new FinishTestItemRQ();
//        finishTestItemRQ.setEndTime(now);
//        finishTestItemRQ.setStatus("PASSED");
//
//        launch.finishTestItem(start,finishTestItemRQ);
//


        System.out.println("Launch started, sleeping...");
//        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        System.out.println("Finishing launch.");


        FinishExecutionRQ rq = new FinishExecutionRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus("FAILED");


        launch.finish(rq);

    }

    protected static StartLaunchRQ buildStartLaunchRq(ListenerParameters parameters) {
        StartLaunchRQ rq = new StartLaunchRQ();
        rq.setName(parameters.getLaunchName());
        rq.setStartTime(Calendar.getInstance().getTime());
        Set<ItemAttributesRQ> attributes = new HashSet<>(parameters.getAttributes());
        rq.setAttributes(attributes);
        rq.setMode(parameters.getLaunchRunningMode());
        rq.setRerun(parameters.isRerun());
        if (isNotBlank(parameters.getRerunOf())) {
            rq.setRerunOf(parameters.getRerunOf());
        }
        if (isNotBlank(parameters.getDescription())) {
            rq.setDescription(parameters.getDescription());
        }
        if (null != parameters.getSkippedAnIssue()) {
            ItemAttributesRQ skippedIssueAttribute = new ItemAttributesRQ();
            skippedIssueAttribute.setKey("SKIPPED_ISSUE_KEY");
            skippedIssueAttribute.setValue(parameters.getSkippedAnIssue().toString());
            skippedIssueAttribute.setSystem(true);
            attributes.add(skippedIssueAttribute);
        }
//        attributes.addAll(SystemAttributesExtractor.extract(AGENT_PROPERTIES_FILE, TestNGService.class.getClassLoader()));
        return rq;
    }

    @Step
    public void test1(){


    }

}
