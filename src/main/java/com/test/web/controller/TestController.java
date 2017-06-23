package com.test.web.controller;

import com.test.Task.OneTaskAsync;
import com.test.web.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by 青葱的熊猫 on 2017/5/26.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private OneTaskAsync oneTaskAsync;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/testStr")
    public void testCountStr(HttpServletRequest req){
        final AsyncContext ctx = req.startAsync();
        this.oneTaskAsync.submit(ctx);
        logger.info("已调用异步方法");
    }

    @RequestMapping("/testListStr")
    public String testListStr(){
        return this.testService.findList();
    }

    @RequestMapping("/testMap")
    @ResponseBody
    public List<Map<String, Object>> testMap() throws InterruptedException {
        return this.testService.findMap();
    }

    @RequestMapping("/testTask")
    public void testTask(HttpServletRequest req){
        final AsyncContext ctx = req.startAsync();
        this.oneTaskAsync.submit(ctx, new Callable(){
            public Object call(){
                return testService.findList();
            }
        });
    }

}
