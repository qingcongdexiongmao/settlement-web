package com.gomefinance.web.controller;

import com.gomefinance.web.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 青葱的熊猫 on 2017/5/26.
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private TestService testService;

    @RequestMapping("/testStr")
    public String testCountStr(){
        return this.testService.find();
    }
}
