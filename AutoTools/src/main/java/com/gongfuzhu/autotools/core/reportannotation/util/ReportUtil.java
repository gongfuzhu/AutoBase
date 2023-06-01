package com.gongfuzhu.autotools.core.reportannotation.util;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.listeners.ItemType;
import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import io.reactivex.Maybe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;

import static java.util.Optional.ofNullable;

public class ReportUtil {


    public static void startTestIt(String itemName, ItemType itemType) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> {

            StartTestItemRQ startTestItemRQ = buildStartItemRq(itemName, itemType);
            Maybe<String> parent = l.getStepReporter().getParent();
            if (null == parent) {
                l.startTestItem(startTestItemRQ);
                return;
            }
            l.startTestItem(parent, startTestItemRQ);


        });


    }


    public static void startTestIt(StartTestItemRQ startTestItemRQ) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> {

            Maybe<String> parent = l.getStepReporter().getParent();
            if (null == parent) {
                l.startTestItem(startTestItemRQ);
                return;
            }
            l.startTestItem(parent, startTestItemRQ);


        });


    }

    public static void stopTestIt(ItemStatus status) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> {

            Maybe<String> rpId = l.getStepReporter().getParent();
            FinishTestItemRQ rq = buildFinishTestRq(status);
            l.finishTestItem(rpId, rq);


        });


    }

    public static void stopTestIt(FinishTestItemRQ finishTestItemRQ) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> {
            Maybe<String> rpId = l.getStepReporter().getParent();
            l.finishTestItem(rpId, finishTestItemRQ);

        });


    }


    public static void startStep(String stepName) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().startNestedStep(buildStartItemRq(stepName, ItemType.STEP)));

    }


    public static void stopStep(Throwable throwable) {
        if (null == throwable) {

            ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().finishNestedStep());
        } else {
            ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().finishNestedStep(throwable));
        }

    }

    public static StartTestItemRQ buildStartItemRq(String itemName, ItemType itemType) {
        StartTestItemRQ rq = new StartTestItemRQ();
        rq.setLaunchUuid(Launch.currentLaunch().getLaunch().blockingGet());
        rq.setName(itemName);
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType(itemType.name());
        return rq;
    }

    public static StartTestItemRQ buildStartItemRq(String itemName, @Nullable String desc, ItemType itemType) {
        StartTestItemRQ rq = new StartTestItemRQ();
        rq.setLaunchUuid(Launch.currentLaunch().getLaunch().blockingGet());
        rq.setName(itemName);
        rq.setDescription(desc);
        rq.setStartTime(Calendar.getInstance().getTime());
        rq.setType(itemType.name());
        return rq;
    }

    public static FinishTestItemRQ buildFinishTestRq(ItemStatus itemStatus) {
        FinishTestItemRQ rq = new FinishTestItemRQ();
        rq.setEndTime(Calendar.getInstance().getTime());
        rq.setStatus(itemStatus.name());
        return rq;
    }


}
