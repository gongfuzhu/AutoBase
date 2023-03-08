package com.gongfuzhu.autotools.core.annotation.agen;

import com.epam.reportportal.annotations.attribute.Attributes;
import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.service.tree.TestItemTree;
import com.epam.reportportal.utils.AttributeParser;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.launch.Mode;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import io.reactivex.Maybe;

import javax.annotation.Nonnull;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AgenServer implements AgenService {

    private static final ThreadLocal<TestItemTree> ITEM_TREE = new InheritableThreadLocal<>();

    private static final ThreadLocal<Launch> launch = new InheritableThreadLocal<>();

    @Override
    public void startLaunch() {
        ListenerParameters listenerParameters = new ListenerParameters();
        listenerParameters.setBaseUrl("http://192.168.3.64:8080");
        listenerParameters.setApiKey("00916f8d-c29a-469e-b9b0-19bd939fb60d");
        listenerParameters.setLaunchName("第一次测试");
        listenerParameters.setProjectName("superadmin_personal");
        listenerParameters.setDescription("这是这次测试的总体描述");
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

//        创建测试树
        TestItemTree tree = new TestItemTree();

        ITEM_TREE.set(tree);
    }

    @Override
    public void finishLaunch(ItemStatus status) {

        FinishExecutionRQ rq = new FinishExecutionRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus(ItemStatus.FAILED.name());
//        rq.setDescription();
//        rq.setAttributes();
        launch.get().finish(rq);

    }

    @Override
    public void startTestSuite(String suiteName) {
        StartTestItemRQ startSuiteRq = buildStartSuiteRq(suiteName);
        Maybe<String> stringMaybe = launch.get().startTestItem(startSuiteRq);
        ITEM_TREE.get().getTestItems().put(TestItemTree.ItemTreeKey.of(suiteName), TestItemTree.createTestItemLeaf(stringMaybe));

    }

    @Override
    public void finishTestSuite(Maybe<String> itemId, String suiteStatus) {
        Launch myLaunch = launch.get();
        FinishTestItemRQ rq = buildFinishTestSuiteRq(suiteStatus);
        // TODO: 2023/2/25 需要传入launchid
        myLaunch.finishTestItem(itemId, rq);

    }

    @Override
    public void startTest(String moduleName) {
        StartTestItemRQ startTestItemRQ = buildStartTestItemRq(moduleName);
        launch.get().startTestItem(startTestItemRQ);
    }

    @Override
    public void finishTest() {

    }

    @Override
    public void startTestMethod() {

    }

    @Override
    public void finishTestMethod(String status) {

    }

    @Override
    public void finishTestMethod(ItemStatus status) {

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
//        if (null != parameters.getSkippedAnIssue()) {
//            ItemAttributesRQ skippedIssueAttribute = new ItemAttributesRQ();
//            skippedIssueAttribute.setKey("SKIPPED_ISSUE_KEY");
//            skippedIssueAttribute.setValue(parameters.getSkippedAnIssue().toString());
//            skippedIssueAttribute.setSystem(true);
//            attributes.add(skippedIssueAttribute);
//        }
//        attributes.addAll(SystemAttributesExtractor.extract(AGENT_PROPERTIES_FILE, TestNGService.class.getClassLoader()));
        return rq;
    }

    protected StartTestItemRQ buildStartSuiteRq(String suiteName) {
        StartTestItemRQ rq = new StartTestItemRQ();
        rq.setName(suiteName);
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType("SUITE");
        return rq;
    }

    @Nonnull
    protected StartTestItemRQ buildStartTestItemRq(@Nonnull String moduleName) {
        StartTestItemRQ rq = new StartTestItemRQ();
        Set<ItemAttributesRQ> attributes = rq.getAttributes() == null ? new HashSet<>() : new HashSet<>(rq.getAttributes());
        rq.setAttributes(attributes);
        rq.setName(moduleName);
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType("TEST");
        return rq;
    }

    protected FinishTestItemRQ buildFinishTestSuiteRq(String suiteStatus) {
        Date now = Calendar.getInstance().getTime();
        FinishTestItemRQ rq = new FinishTestItemRQ();
        rq.setEndTime(now);
        rq.setStatus(suiteStatus);
        return rq;
    }
}
