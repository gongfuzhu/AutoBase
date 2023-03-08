package com.gongfuzhu.autotools.core.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@Log4j2
public class Testcontroller {


    @RequestMapping("log")
    public void test(){
        log.info("这是日志1");

    }
}
