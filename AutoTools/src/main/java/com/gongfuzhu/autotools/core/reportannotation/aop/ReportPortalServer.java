package com.gongfuzhu.autotools.core.reportannotation.aop;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.listeners.ItemType;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.service.tree.ItemTreeReporter;
import com.epam.reportportal.service.tree.TestItemTree;
import com.epam.reportportal.utils.MemoizingSupplier;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import io.reactivex.Maybe;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Log4j2
public class ReportPortalServer {

    private static final ThreadLocal<ReportPortalServer> CURRENT_ReportPortalServer = new InheritableThreadLocal<>();

    private final @Getter MemoizingSupplier<Launch> launch;
    private volatile Thread shutDownHook;

    private final AtomicBoolean isLaunchFailed = new AtomicBoolean();


    private Thread getShutdownHook(final Supplier<Launch> launch) {
        return new Thread(() -> {
            FinishExecutionRQ rq = new FinishExecutionRQ();
            rq.setEndTime(Calendar.getInstance().getTime());
            launch.get().finish(rq);
        });
    }


    public ReportPortalServer(@Nonnull final ReportPortal reportPortal,ListenerParameters parameters) {
        this.launch = new MemoizingSupplier<>(() -> {
            StartLaunchRQ startRq = buildStartLaunchRq(parameters);
            startRq.setStartTime(Calendar.getInstance().getTime());
            Launch newLaunch = reportPortal.newLaunch(startRq);
            shutDownHook = getShutdownHook(() -> newLaunch);
            Runtime.getRuntime().addShutdownHook(shutDownHook);
            return newLaunch;
        });
        CURRENT_ReportPortalServer.set(this);
    }


    public static ReportPortalServer currentLaunch() {
        return CURRENT_ReportPortalServer.get();
    }


    public Maybe<String> startLaunch() {
        return launch.get().start();
    }

    public void finishLaunch() {
        FinishExecutionRQ rq = new FinishExecutionRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus(isLaunchFailed.get() ? ItemStatus.FAILED.name() : ItemStatus.PASSED.name());
        launch.get().finish(rq);
        launch.reset();
        Runtime.getRuntime().removeShutdownHook(shutDownHook);
    }

    public void finishLaunch(ItemStatus itemStatus) {
        FinishExecutionRQ rq = new FinishExecutionRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus(itemStatus.name());
        launch.get().finish(rq);
        launch.reset();
        Runtime.getRuntime().removeShutdownHook(shutDownHook);
    }


    public Maybe<String> startTestSuite(String suiteName, String suiteDesc) {
        StartTestItemRQ rq = buildStartItemRq(suiteName, ItemType.SUITE);
        rq.setDescription(suiteDesc);
        Launch myLaunch = launch.get();
        final Maybe<String> item = myLaunch.startTestItem(rq);
        return item;
    }


    public void finishTestSuite(ItemStatus status) {
        Maybe<String> rpId = launch.get().getStepReporter().getParent();
        Launch myLaunch = launch.get();
        if (null != rpId) {
            FinishTestItemRQ rq = buildFinishTestSuiteRq(status);
            myLaunch.finishTestItem(rpId, rq);
        }
    }


    public Maybe<String> startTest(StartTestItemRQ testItemRQ) {
        Launch myLaunch = launch.get();
        Maybe<String> rpId = myLaunch.getStepReporter().getParent();
        final Maybe<String> testID = myLaunch.startTestItem(rpId, testItemRQ);
        return testID;
    }
    public Maybe<String> startTest(String testName ,String desc,String caseId) {
        StartTestItemRQ testItemRQ = buildStartItemRq(testName, ItemType.STEP);
        testItemRQ.setDescription(desc);
        testItemRQ.setTestCaseId(caseId);
        testItemRQ.setCodeRef(caseId);
        Launch myLaunch = launch.get();
        Maybe<String> rpId = myLaunch.getStepReporter().getParent();
        final Maybe<String> testID = myLaunch.startTestItem(rpId, testItemRQ);
        return testID;
    }


    public void finishTest(ItemStatus testContext) {
        Maybe<String> rpId = launch.get().getStepReporter().getParent();
        FinishTestItemRQ rq = buildFinishTestRq(testContext);
        launch.get().finishTestItem(rpId, rq);
    }


    public static void sendLog(String desc, LogLevel level) {
        ReportPortal.emitLog(desc, level.name(), new Date());
    }

    public static void sendLog(String desc, LogLevel level, File file) {
        ReportPortal.emitLog(desc, level.name(), new Date(), file);


    }


    protected StartLaunchRQ buildStartLaunchRq(ListenerParameters parameters) {
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
            skippedIssueAttribute.setKey("跳过");
            skippedIssueAttribute.setValue(parameters.getSkippedAnIssue().toString());
            skippedIssueAttribute.setSystem(true);
            attributes.add(skippedIssueAttribute);
        }
        return rq;
    }

    protected FinishTestItemRQ buildFinishTestSuiteRq(ItemStatus status) {
        Date now = Calendar.getInstance().getTime();
        FinishTestItemRQ rq = new FinishTestItemRQ();
        rq.setEndTime(now);
        rq.setStatus(status.name());
        return rq;
    }

    protected StartTestItemRQ buildStartItemRq(String itemName, ItemType itemType) {
        StartTestItemRQ rq = new StartTestItemRQ();
        rq.setLaunchUuid(launch.get().getLaunch().blockingGet());
        rq.setName(itemName);
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType(itemType.name());
        return rq;
    }

    protected FinishTestItemRQ buildFinishTestRq(ItemStatus itemStatus) {
        FinishTestItemRQ rq = new FinishTestItemRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus(itemStatus.name());
        return rq;
    }


}
