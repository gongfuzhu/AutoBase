package com.gongfuzhu.autotools.core.jmeter;

import com.epam.reportportal.listeners.ItemType;
import com.gongfuzhu.autotools.core.reportannotation.util.ReportUtil;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarLog;
import de.sstoehr.harreader.model.HarRequest;
import lombok.SneakyThrows;
import org.apache.jmeter.JMeter;
import org.apache.jmeter.assertions.JSR223Assertion;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.ViewResultsFullVisualizer;
import org.apache.jorphan.collections.HashTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

public class JmeterScript {


    @SneakyThrows
    public void fiddlerToJMeter() {

        HarReader harReader = new HarReader();
        File file = new File("D:\\sysfile\\Desktop\\sc\\建管家.har");
        Har har = harReader.readFromFile(file);

        ReportUtil.startTestIt("testName", ItemType.STEP);
        HarLog logs = har.getLog();


        JMeterUtils.setJMeterHome("D:\\apache-jmeter-5.5");
        JMeterUtils.loadJMeterProperties("D:\\apache-jmeter-5.5\\bin\\jmeter.properties");
        // 可以注释这一行，查看额外的日志，例如DEBUG级别
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();

        //测试计划树


        //循环控制器
        LoopController loopController = new LoopController();
        loopController.setLoops(1);
        loopController.setFirst(true);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        loopController.initialize();

        //线程组
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Example Thread Group");
        threadGroup.setNumThreads(1);
        threadGroup.setRampUp(1);
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

        //测试计划
        TestPlan testPlan = new TestPlan("创建JMeter脚本111");
        testPlan.setComment("ddd");
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

        HashTree jmxl = new HashTree();

        ResultCollector resultCollector = new ResultCollector();
        resultCollector.setProperty(TestElement.TEST_CLASS, ResultCollector.class.getName());
        resultCollector.setProperty(TestElement.GUI_CLASS, ViewResultsFullVisualizer.class.getName());
        resultCollector.setName("查看结果树");


        JSR223Assertion jsr223Assertion = new JSR223Assertion();
        jsr223Assertion.setName("JSR223Assertion");
        String script="  import groovy.json.JsonSlurper\n" +
                "     def headers = prev.getResponseHeaders()\n" +
                "     if (headers.contains(\"application/json\")){\n" +
                "         def response = new JsonSlurper().parseText(prev.getResponseDataAsString())\n" +
                "         log.info(\"响应内容：{}\",response)\n" +
                "         def data= response.data\n" +
                "         log.info(\"data类型：{}\",data.getClass())\n" +
                "         if (data instanceof ArrayList){\n" +
                "             if (data.size()==0){\n" +
                "                 log.info(\"数组为0时文本：{}\",response)\n" +
                "                 String message=\"\\n请求header：\"+sampler.getHeaderManager().getHeaders().toString()+\"\\n请求数据：\"+prev.getSamplerData()+\"\\n响应数据为空：\"+response\n" +
                "                 AssertionResult.setFailureMessage(message)\n" +
                "                 AssertionResult.setFailure(true)\n" +
                "             }else {\n" +
                "                 log.info(\"数组大于0时文本：{}\",response)\n" +
                "             }\n" +
                "         }else {\n" +
                "             def total = response.data.total\n" +
                "             if (null==total){\n" +
                "                 def rest= response.data\n" +
                "                 log.info(\"没有total时文本：{}\",response)\n" +
                "             }else if (total==0){\n" +
                "                 log.info(\"有total等于0时文本：{}\",response)\n" +
                "                 String message=\"\\n请求header：\"+sampler.getHeaderManager().getHeaders().toString()+\"\\n请求数据：\"+prev.getSamplerData()+\"\\n响应数据为空：\"+response\n" +
                "                 AssertionResult.setFailureMessage(message)\n" +
                "                 AssertionResult.setFailure(true)\n" +
                "             }else if (total>0){\n" +
                "                 log.info(\"有total不等于0时文本：{}\",response)\n" +
                "                 log.info(\"total值：{}\",total)\n" +
                "             }else {\n" +
                "                 log.info(\"未知类型文本：{}\",response)\n" +
                "             }\n" +
                "         }\n" +
                "\n" +
                "     }";
        jsr223Assertion.setScript("script");
        jsr223Assertion.setScriptLanguage("groovy");
//        jsr223Assertion.setCacheKey();
        jsr223Assertion.setProperty(TestElement.TEST_CLASS,JSR223Assertion.class.getName());
        jsr223Assertion.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());
        jmxl.add(resultCollector);
        jmxl.add(jsr223Assertion);
        HashTree thread = jmxl.add(testPlan, threadGroup);
        logs.getEntries().forEach(it -> {
                    HarRequest request = it.getRequest();
                    HeaderManager headerManager = new HeaderManager();

                    headerManager.setName("header");
                    headerManager.setEnabled(true);
                    headerManager.setComment("只是注释");
                    headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
                    headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());

                    request.getHeaders().forEach(harHeader -> {
                                headerManager.add(new Header(harHeader.getName(), harHeader.getValue()));
                            }
                    );


                    HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
                    httpSamplerProxy.setDomain("");
                    httpSamplerProxy.setName(request.getUrl());
                    httpSamplerProxy.setPath(request.getUrl());
                    httpSamplerProxy.setPort(80);
                    httpSamplerProxy.setMethod(it.getRequest().getMethod().name());
                    httpSamplerProxy.addArgument("", request.getPostData().getText());
                    httpSamplerProxy.setPostBodyRaw(true);
                    httpSamplerProxy.setUseKeepAlive(true);
                    httpSamplerProxy.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
                    httpSamplerProxy.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

                    thread.add(httpSamplerProxy, headerManager);

                }
        );


        SaveService.saveTree(jmxl, new FileOutputStream("D:\\sysfile\\Desktop\\sc\\example.jmx"));


    }

    public static void main(String[] args) {
        new JmeterScript().fiddlerToJMeter();
    }

    public static void main1(String[] args) throws IOException {
        initJMeter();
        String jmxPath = "D:\\sysfile\\Desktop\\sc\\线程组.jmx";
//        FileServer.getFileServer().setBaseForScript(new File(jmxPath));
        HashTree hashTree = SaveService.loadTree(new File(jmxPath));

        SaveService.saveTree(hashTree, new FileOutputStream("D:\\sysfile\\Desktop\\sc\\example.jmx"));
//        hashTree.entrySet().forEach(aa -> {
//
//            Object key = aa.getKey();
//            System.out.println(key.getClass());
//            aa.getValue().entrySet().forEach(bb -> {
//
//                Object key1 = bb.getKey();
//                System.out.println(key1.getClass());
//                bb.getValue().entrySet().forEach(cc -> {
//                    Object key2 = cc.getKey();
//                    System.out.println(key2.getClass());
//                    cc.getValue().entrySet().forEach(dd->{
//                        HeaderManager headerManager = (HeaderManager) dd.getKey();
//                        System.out.println(headerManager.getHeaders());
//
//                    });
//                });
//            });
//
//
//        });


    }

    private static void initJMeter() {

        String jmeterHome = "D:\\apache-jmeter-5.5";
        // 初始化压测引擎
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // JMeter初始化(属性、日志级别、区域设置等)
        JMeterUtils.setJMeterHome(jmeterHome);
        JMeterUtils.loadJMeterProperties(jmeterHome + File.separator + "bin" + File.separator + "jmeter.properties");
        // 可以注释这一行，查看额外的日志，例如DEBUG级别
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();

    }

    @SneakyThrows
    public void jmeterToHttp() {

        // 设置jmeterHome路径
        String jmeterHome1 = "/Users/apple/Downloads/performance/apache-jmeter-4.0";
        //File jmeterHome = new File(System.getProperty("jmeter.home"));
        File jmeterHome = new File(jmeterHome1);
        File jmxFile = new File(jmeterHome1 + "/example.jmx");

        // 分隔符
        String slash = System.getProperty("file.separator");

        // 判断jmeterHome
        if (jmeterHome.exists()) {
            File jmeterProperties = new File(jmeterHome.getPath() + slash + "bin" + slash + "jmeter.properties");
            if (jmeterProperties.exists()) {

                // 初始化压测引擎
                StandardJMeterEngine jmeter = new StandardJMeterEngine();

                // JMeter初始化(属性、日志级别、区域设置等)
                JMeterUtils.setJMeterHome(jmeterHome.getPath());
                JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
                // 可以注释这一行，查看额外的日志，例如DEBUG级别
                JMeterUtils.initLogging();
                JMeterUtils.initLocale();

                // JMeter测试计划，基本上是JOrphan HashTree
                HashTree testPlanTree = new HashTree();

                // 设置jmx脚本文件的工作目录，可以根据这个来找到参数化文件及实现其文件流。
                FileServer.getFileServer().setBaseForScript(jmxFile);

                // 加载jmx脚本，本身这个操作非常复杂。
                // jmx脚本中通常会包含参数化文件，用户自定义的参数化，Jmeter自定义函数，各种Sampler的实现，断言，甚至用户自定义的插件等等。
                // 同时还有各种监听接口的初始化。
                // 这些都是要找到实现类加载的，源码中包含非常多的实现类。
                testPlanTree = SaveService.loadTree(jmxFile);

                // 去掉没用的节点元素，替换掉可以替换的控制器,这个是递归实现的，比较复杂
                JMeter.convertSubTree(testPlanTree);

                // 在stdout中添加summary输出，得到测试进度，如:
                // summary =      2 in   1.3s =    1.5/s Avg:   631 Min:   290 Max:   973 Err:     0 (0.00%)
                Summariser summer = null;
                String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
                if (summariserName.length() > 0) {
                    summer = new Summariser(summariserName);
                }

                // 将执行结果存储到.csv文件中
                String logFile = jmeterHome + slash + "example.csv";
                ResultCollector logger = new ResultCollector(summer);
                logger.setFilename(logFile);
                testPlanTree.add(testPlanTree.getArray()[0], logger);

                // 单机执行测试计划
                jmeter.configure(testPlanTree);  // 设置回调监听器，并添加状态
                jmeter.run();

                System.out.println("生成结果文件：" + jmeterHome + slash + "example.csv");
                System.out.println("加载Jmx脚本文件：" + jmeterHome + slash + "example.jmx");
                System.exit(0);
            }
        }
        System.err.println("jmeter.home 未设置或指向不正确的位置");
        System.exit(1);


    }


}
