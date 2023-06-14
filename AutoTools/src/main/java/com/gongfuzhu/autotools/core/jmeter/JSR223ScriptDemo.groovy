package com.gongfuzhu.autotools.core.jmeter

import org.apache.jmeter.assertions.AssertionResult
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult
import org.apache.jmeter.protocol.http.sampler.HTTPSampler
import org.apache.jmeter.threads.JMeterContext
import org.apache.jmeter.threads.JMeterVariables
import org.slf4j.Logger
import org.slf4j.LoggerFactory
//API文档
//https://jmeter.apache.org/api/org/apache/jmeter/samplers/Sampler.html
//https://jmeter.apache.org/api/org/apache/jmeter/threads/JMeterContext.html
//https://jmeter.apache.org/api/org/apache/jmeter/threads/JMeterVariables.html
//https://jmeter.apache.org/api/org/apache/jmeter/samplers/SampleResult.html
//https://jmeter.apache.org/api/org/apache/jmeter/assertions/AssertionResult.html

def sampler = new HTTPSampler() /*Jmeter sampler*/
Logger log = LoggerFactory.getLogger("demo");   /*日志*/
def ctx = new JMeterContext()   /*非线程安全，仅适用于单线程*/
def vars = new JMeterVariables() /*vars*/
def props = new Properties()    /*props*/
def Label = "当前sampler名字"
def FileName = "当前脚本名字（如有）"
def SampleResult = new HTTPSampleResult()   /*SampleResult，出现于sampler*/
def prev  = new HTTPSampleResult() /*上一个SampleResult，出现于后置处理器*/
def OUT = System.out
def Parameters,args = "将参数传递给脚本中的参数";
def AssertionResult  = new AssertionResult("demo")  /*断言*/






log.info(vars.println())

vars.get("thirdOrderNo")

vars.entrySet() //查看线程组内所有的变量
props.getProperties() //查看所有的全局变量
OUT.print("OUT区别"+vars.entrySet())
OUT.close()


//kailin
import groovy.json.JsonSlurper


def headers = prev.getResponseHeaders()
def code = prev.getResponseCode()
if (!code.equals("200")){
    String message="响应状态码："+code
    AssertionResult.setFailureMessage(message)
    AssertionResult.setFailure(true)

}
if (headers.contains("application/json")){
    def response = new JsonSlurper().parseText(prev.getResponseDataAsString())
    log.info("响应内容：{}",response)
    def data= response.data
    log.info("data类型：{}",data.getClass())
    if (data instanceof ArrayList){
        if (data.size()==0){
            log.info("数组为0时文本：{}",response)
            String message="\n请求header："+sampler.getHeaderManager().getHeaders().toString()+"\n请求数据："+prev.getSamplerData()+"\n响应数据为空："+response
            AssertionResult.setFailureMessage(message)
            AssertionResult.setFailure(true)
        }else {
            log.info("数组大于0时文本：{}",response)
        }
    }else {
        def total = response.data.total
        if (null==total){
            def rest= response.data
            log.info("没有total时文本：{}",response)
        }else if (total==0){
            log.info("有total等于0时文本：{}",response)
            String message="\n请求header："+sampler.getHeaderManager().getHeaders().toString()+"\n请求数据："+prev.getSamplerData()+"\n响应数据为空："+response
            AssertionResult.setFailureMessage(message)
            AssertionResult.setFailure(true)
        }else if (total>0){
            log.info("有total不等于0时文本：{}",response)
            log.info("total值：{}",total)
        }else {
            log.info("未知类型文本：{}",response)
        }
    }

}



