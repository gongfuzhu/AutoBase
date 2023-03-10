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
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Log4j2
public class ReportPortalServer {


    public Launch initReport(@Nonnull final ReportPortal reportPortal,String desc) {

        StartLaunchRQ startRq = buildStartLaunchRq(reportPortal.getParameters());
        startRq.setStartTime(Calendar.getInstance().getTime());
        Optional.of(desc).ifPresent(it->{startRq.setDescription(desc);
        });
        return reportPortal.newLaunch(startRq);

    }




    public void finishLaunch(ItemStatus itemStatus) {
        FinishExecutionRQ rq = new FinishExecutionRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus(itemStatus.name());
        Launch.currentLaunch().finish(rq);
    }

    public Maybe<String> startLaunch(){
        return Launch.currentLaunch().start();
    }


    public Maybe<String> startTestSuite(String suiteName, String suiteDesc) {
        StartTestItemRQ rq = buildStartItemRq(suiteName, ItemType.SUITE);
        rq.setDescription(suiteDesc);
        Launch myLaunch = Launch.currentLaunch();
        final Maybe<String> item = myLaunch.startTestItem(rq);
        return item;
    }


    public void finishTestSuite(ItemStatus status) {
        Maybe<String> rpId = Launch.currentLaunch().getStepReporter().getParent();
        Launch myLaunch = Launch.currentLaunch();
        if (null != rpId) {
            FinishTestItemRQ rq = buildFinishTestSuiteRq(status);
            myLaunch.finishTestItem(rpId, rq);
        }else {

            log.warn("rpId等于空");

        }
    }


    public Maybe<String> startTest(StartTestItemRQ testItemRQ) {
        Launch myLaunch = Launch.currentLaunch();
        Maybe<String> rpId = myLaunch.getStepReporter().getParent();
        final Maybe<String> testID = myLaunch.startTestItem(rpId, testItemRQ);
        return testID;
    }


    public void finishTest(ItemStatus testContext ) {
        Maybe<String> rpId = Launch.currentLaunch().getStepReporter().getParent();
        FinishTestItemRQ rq = buildFinishTestRq(testContext);
        Launch.currentLaunch().finishTestItem(rpId, rq);
    }


    public static void sendLog(String desc, LogLevel level) {
        ItemTreeReporter.sendLog(Launch.currentLaunch().getClient(), level.name(), desc, Calendar.getInstance().getTime(), Launch.currentLaunch().getLaunch(), TestItemTree.createTestItemLeaf(Launch.currentLaunch().getStepReporter().getParent()));
    }

    public static void sendLog( String desc, LogLevel level, File file) {
        ItemTreeReporter.sendLog(Launch.currentLaunch().getClient(), level.name(), desc, Calendar.getInstance().getTime(), file, Launch.currentLaunch().getLaunch(), TestItemTree.createTestItemLeaf(Launch.currentLaunch().getStepReporter().getParent()));


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
        rq.setLaunchUuid(Launch.currentLaunch().getLaunch().blockingGet());
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
