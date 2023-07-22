package com.gongfuzhu.autotools.core.reportannotation.util;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.listeners.ItemType;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.service.step.StepRequestUtils;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import io.reactivex.Maybe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;

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


    /**
     * 开始步骤
     * @param stepName  步骤名称
     */

    public static void startStep(String stepName) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().startNestedStep(StepRequestUtils.buildStartStepRequest(stepName, "")));

    }

    /**
     * 开始步骤
     * @param stepName 步骤名称
     * @param desc 描述
     */
    public static void startStep(String stepName,String desc) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().startNestedStep(StepRequestUtils.buildStartStepRequest(stepName, desc)));

    }



    /**
     * 结束步骤
     * @param status 状态
     */
    public static void stopTestIt(ItemStatus status) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().finishNestedStep(status));

    }

    /**
     * 结束步骤
     * @param throwable 异常信息
     */
    public static void stopTestIt(Throwable throwable) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> l.getStepReporter().finishNestedStep(throwable));


    }

    /**
     * 结束步骤
     * @param finishTestItemRQ
     */
    public static void stopTestIt(FinishTestItemRQ finishTestItemRQ) {

        ofNullable(Launch.currentLaunch()).ifPresent(l -> {
            Maybe<String> rpId = l.getStepReporter().getParent();
            l.finishTestItem(rpId, finishTestItemRQ);

        });


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

        return StepRequestUtils.buildFinishTestItemRequest(itemStatus);
    }


    public static void sendLog(String desc, LogLevel level) {
        ReportPortal.emitLog(desc, level.name(), new Date());
    }

}
