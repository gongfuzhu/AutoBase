package com.gongfuzhu.autotools.core.jmeter;

import lombok.SneakyThrows;
import org.apache.jmeter.assertions.JSR223Assertion;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.CookiePanel;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.ViewResultsFullVisualizer;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;

import java.io.File;

/**
 *
 *         public void fiddlerToJMeter() {
 *         String jmeterHome = "D:\\apache-jmeter-5.5";
 *         // 初始化压测引擎
 *
 *         // JMeter初始化(属性、日志级别、区域设置等)
 *         JMeterUtils.setJMeterHome(jmeterHome);
 *         JMeterUtils.loadJMeterProperties(jmeterHome + File.separator + "bin" + File.separator + "jmeter.properties");
 *         // 可以注释这一行，查看额外的日志，例如DEBUG级别
 *         JMeterUtils.initLogging();
 *         JMeterUtils.initLocale();
 *
 *         HarReader harReader = new HarReader();
 *         File file = new File("D:\\sysfile\\Desktop\\sc\\建管家.har");
 *         Har har = harReader.readFromFile(file);
 *
 *         ReportUtil.startTestIt("testName", ItemType.STEP);
 *         HarLog logs = har.getLog();
 *
 *         HashTree  jmxl= new HashTree();
 *         HashTree thread = jmxl.add(JMeterComponent.creatTestPlan(), JMeterComponent.creatThreadGroup());
 *         jmxl.add(jmxl.getArray()[0],JMeterComponent.creatHeaderManager());
 *         jmxl.add(jmxl.getArray()[0],JMeterComponent.creatResultCollector());
 *         jmxl.add(jmxl.getArray()[0],JMeterComponent.creatJSR223Assertion("11111111111111111"));
 *         jmxl.add(jmxl.getArray()[0],JMeterComponent.creatCookieManager());
 *
 *
 *         logs.getEntries().forEach(it -> {
 *                     HarRequest request = it.getRequest();
 *
 *                     HeaderManager headerManager = JMeterComponent.creatHeaderManager();
 *                     request.getHeaders().forEach(harHeader -> {
 *                                 headerManager.add(new Header(harHeader.getName(), harHeader.getValue()));
 *                             }
 *                     );
 *
 *                     try {
 *                         URL url = new URL(request.getUrl());
 *                         HTTPSamplerProxy httpSamplerProxy = JMeterComponent.creatHTTPSamplerProxy();
 *                         httpSamplerProxy.setDomain(url.getHost());
 *                         httpSamplerProxy.setName(url.getPath());
 *                         httpSamplerProxy.setPath(url.getPath());
 *                         httpSamplerProxy.setProtocol(url.getProtocol());
 *                         httpSamplerProxy.setMethod(it.getRequest().getMethod().name());
 *                         httpSamplerProxy.addArgument("", request.getPostData().getText());
 *                         httpSamplerProxy.setPostBodyRaw(true);
 *                         httpSamplerProxy.setUseKeepAlive(true);
 *
 *                         thread.add(httpSamplerProxy, headerManager);
 *
 *
 *                     } catch (MalformedURLException e) {
 *                         throw new RuntimeException(e);
 *                     }
 *
 *
 *                 }
 *         );
 *
 *
 *         SaveService.saveTree(jmxl, new FileOutputStream("D:\\sysfile\\Desktop\\sc\\example.jmx"));
 *
 *
 *     }
 *
 */
public class JMeterComponent {


    @SneakyThrows
    public static void main(String[] args) {

        JMeterComponent.creatTestPlan("D:\\apache-jmeter-5.5");
        ResultCollector resultCollector = new ResultCollector();
        resultCollector.setFilename("");
        ReportGenerator reportGenerator = new ReportGenerator("D:\\scan\\script\\report\\demo.jmx20230531172217\\demo.jmx20230531172217.jtl", resultCollector);
        reportGenerator.generate();
    }
    //生成报告
    @SneakyThrows
    public static  void reportGenerator(){

        ReportGenerator reportGenerator = new ReportGenerator("", null);

    }


        public static TestPlan creatTestPlan(String jmeterPath){


            JMeterUtils.setJMeterHome(jmeterPath);
            JMeterUtils.loadJMeterProperties(jmeterPath + File.separator + "bin" + File.separator + "jmeter.properties");
            // 可以注释这一行，查看额外的日志，例如DEBUG级别
            JMeterUtils.initLogging();
            JMeterUtils.initLocale();

            TestPlan testPlan = new TestPlan("创建JMeter脚本");
            testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
            testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
            testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

            return testPlan;
        }

        public static ThreadGroup creatThreadGroup(){

            //循环控制器
            LoopController loopController = new LoopController();
            loopController.setLoops(1);
            loopController.setFirst(true);
            loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
            loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
            loopController.initialize();

            ThreadGroup threadGroup = new ThreadGroup();
            threadGroup.setName("线程组");
            threadGroup.setNumThreads(1);
            threadGroup.setRampUp(1);
            threadGroup.setSamplerController(loopController);
            threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
            threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

            return threadGroup;
        }

        public static   HeaderManager creatHeaderManager(){
            HeaderManager headerManager = new HeaderManager();
            headerManager.setName("HTTP信息头管理器");
            headerManager.setEnabled(true);
//            headerManager.setComment("只是注释");
            headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
            headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());


            return headerManager;

        }
        public static HTTPSamplerProxy creatHTTPSamplerProxy(){

            HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
            httpSamplerProxy.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
            httpSamplerProxy.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

            return httpSamplerProxy;

        }

        public static ResultCollector creatResultCollector(){

            ResultCollector resultCollector = new ResultCollector();
            resultCollector.setProperty(TestElement.GUI_CLASS, ViewResultsFullVisualizer.class.getName());
            resultCollector.setProperty(TestElement.TEST_CLASS, ResultCollector.class.getName());
            resultCollector.setEnabled(true);
            resultCollector.setFilename("");
            resultCollector.setName("查看结果树");
            return resultCollector;


        }

        public static JSR223Assertion creatJSR223Assertion(String script){
            JSR223Assertion jsr223Assertion = new JSR223Assertion();
            jsr223Assertion.setName("JSR223 断言");
            jsr223Assertion.setProperty("script",script);
            jsr223Assertion.setProperty("scriptLanguage","groovy");
            jsr223Assertion.setProperty(TestElement.TEST_CLASS, JSR223Assertion.class.getName());
            jsr223Assertion.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());
            return jsr223Assertion;
        }

        public static CookieManager creatCookieManager(){

            CookieManager cookieManager = new CookieManager();
            cookieManager.setProperty(TestElement.GUI_CLASS, CookiePanel.class.getName());
            cookieManager.setProperty(TestElement.TEST_CLASS, CookieManager.class.getName());
            cookieManager.setClearEachIteration(false);
            cookieManager.setControlledByThread(false);
            cookieManager.setName("HTTP Cookie管理器");

            return cookieManager;

        }


    }