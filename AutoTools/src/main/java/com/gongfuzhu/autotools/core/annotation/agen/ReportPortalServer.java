package com.gongfuzhu.autotools.core.annotation.agen;

import com.epam.reportportal.aspect.StepAspect;
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
import com.epam.ta.reportportal.ws.model.launch.Mode;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import io.reactivex.Maybe;
import io.reactivex.internal.operators.maybe.MaybeCache;
import lombok.Getter;
import org.springframework.context.annotation.Import;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static com.gongfuzhu.autotools.core.annotation.agen.ItemTreeUtils.createKey;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ReportPortalServer {

    public static final ThreadLocal<ReportPortalServer> CURRENT_ReportPortalServer = new InheritableThreadLocal<>();
    public final TestItemTree ITEM_TREE = new TestItemTree();
    private final MemoizingSupplier<Launch> launch;
    private volatile Thread shutDownHook;

    private final AtomicBoolean isLaunchFailed = new AtomicBoolean();

    public ReportPortalServer(@Nonnull final ReportPortal reportPortal) {
        this.launch = new MemoizingSupplier<>(() -> {
            StartLaunchRQ startRq = buildStartLaunchRq(reportPortal.getParameters());
            startRq.setStartTime(Calendar.getInstance().getTime());
            Launch newLaunch = reportPortal.newLaunch(startRq);
            shutDownHook = getShutdownHook(() -> newLaunch);
            Runtime.getRuntime().addShutdownHook(shutDownHook);

            CURRENT_ReportPortalServer.set(this);
            return newLaunch;
        });
    }



    public void startLaunch() {
        Maybe<String> launchId = launch.get().start();
        ITEM_TREE.setLaunchId(launchId);
    }

    public void finishLaunch() {
        FinishExecutionRQ rq = new FinishExecutionRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus(isLaunchFailed.get() ? ItemStatus.FAILED.name() : ItemStatus.PASSED.name());
        launch.get().finish(rq);
        launch.reset();
        Runtime.getRuntime().removeShutdownHook(shutDownHook);
    }

    private void addToTree(String key, Maybe<String> item) {
        ITEM_TREE.getTestItems().put(createKey(key), TestItemTree.createTestItemLeaf(item));
    }

    private void addToTree(String key, Maybe<String> item, Maybe<String> parentItem) {
        ITEM_TREE.getTestItems().put(createKey(key), TestItemTree.createTestItemLeaf(parentItem, item));
    }


    public Maybe<String> startTestSuite(String suiteName, String suiteDesc, String suitKey) {
        StartTestItemRQ rq = buildStartItemRq(suiteName, suiteDesc, ItemType.SUITE);
        Launch myLaunch = launch.get();
        final Maybe<String> item = myLaunch.startTestItem(rq);
        addToTree(suitKey, item, ITEM_TREE.getLaunchId());
        return item;
    }


    public void finishTestSuite(ItemStatus status, String suitKey) {
        TestItemTree.TestItemLeaf testItemLeaf = ITEM_TREE.getTestItems().get(createKey(suitKey));
        Maybe<String> rpId = testItemLeaf.getItemId();
        Launch myLaunch = launch.get();
        if (null != rpId) {
            FinishTestItemRQ rq = buildFinishTestSuiteRq(status);
            myLaunch.finishTestItem(rpId, rq);
        }
        removeFromTree(suitKey);
    }


    public Maybe<String> startTest(String testName, String testDesc, String testKey, String parentKey) {
        TestItemTree.TestItemLeaf prentLeaf = ITEM_TREE.getTestItems().get(createKey(parentKey));
        StartTestItemRQ rq = buildStartItemRq(testName, testDesc, ItemType.TEST);
        Launch myLaunch = launch.get();
        final Maybe<String> testID = myLaunch.startTestItem(prentLeaf.getItemId(), rq);
        addToTree(testKey, testID, prentLeaf.getItemId());
        return testID;
    }


    public void finishTest(ItemStatus testContext, String testKey) {
        TestItemTree.TestItemLeaf testItemLeaf = ITEM_TREE.getTestItems().get(createKey(testKey));
        FinishTestItemRQ rq = buildFinishTestRq(testContext);
        launch.get().finishTestItem(testItemLeaf.getItemId(), rq);
        removeFromTree(testKey);
    }


    public void sendLog(Maybe<String> itemId, String desc, LogLevel level) {
        ItemTreeReporter.sendLog(launch.get().getClient(), level.name(), desc, Calendar.getInstance().getTime(), launch.get().getLaunch(), TestItemTree.createTestItemLeaf(itemId));
    }

    public void sendLog(Maybe<String> itemId, String desc, LogLevel level, File file) {
        ItemTreeReporter.sendLog(launch.get().getClient(), level.name(), desc, Calendar.getInstance().getTime(), file, launch.get().getLaunch(), TestItemTree.createTestItemLeaf(itemId));


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

    protected StartTestItemRQ buildStartItemRq(String itemName, String desc, ItemType itemType) {
        StartTestItemRQ rq = new StartTestItemRQ();
        rq.setName(itemName);
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType(itemType.name());
        rq.setDescription(desc);
        return rq;
    }

    protected FinishTestItemRQ buildFinishTestRq(ItemStatus itemStatus) {
        FinishTestItemRQ rq = new FinishTestItemRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus(itemStatus.name());
        return rq;
    }

    private static Thread getShutdownHook(final Supplier<Launch> launch) {
        return new Thread(() -> {
            FinishExecutionRQ rq = new FinishExecutionRQ();
            rq.setEndTime(Calendar.getInstance().getTime());
            launch.get().finish(rq);
        });
    }

    private void removeFromTree(String itemesKey) {
        ITEM_TREE.getTestItems().remove(createKey(itemesKey));
    }


}
