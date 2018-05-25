package com.test.web.controller;

import com.test.web.service.TransactionalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qingcongxiongmao on 2018/5/20.
 */
@RestController
@RequestMapping("/tran")
public class TransactionalController {

    @Autowired
    private TransactionalServices transactionalServices;

    @RequestMapping("/testTransactional")
    public void testTransactional(String num, String setp){
        if(num.equals("1")){
            this.transactionalServices.testOutPublic();
        }else if(num.equals("2")){
            this.transactionalServices.testOutPublicNOTr();
        }else if(num.equals("3")){
            this.transactionalServices.testOutPublicNoTrToPubTr(setp);
        }else if(num.equals("4")){
            this.transactionalServices.testOutPublicTrToAll();
        }else if(num.equals("5")){
            this.transactionalServices.testOutPublicTrToNewTr();
        }else if(num.equals("6")){
            this.transactionalServices.testOutPublicTrToNewSerTr(setp);
        }else if(num.equals("7")){
            this.transactionalServices.testOutPublicTrToNewSerNoTr();
        }else if(num.equals("8")){
            this.transactionalServices.testPublicToNoPubNewTr();
        }else if(num.equals("9")){
            this.transactionalServices.testPublicToPubNesTr(setp);
        }

    }


}
