package com.test.web.service;

import com.test.web.properties.TransactionalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by qingcongxiongmao on 2018/5/21.
 */
@Service
public class TransactionalNewServices {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionalProperties transactionalProperties;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 新开事务公开方法
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testPublicNewTr(String setp){
        logger.info("新开事务公开方法-事务开始");
        this.jdbcTemplate.update(this.transactionalProperties.UPDATE_TESTPUBLICNEWTR_SQL);
        logger.info("新开事务公开方法-事务结束");
        //内部异常回滚，接口方式调用的新事务方法外部也回滚
        if(setp.equals("2"))
            throw new RuntimeException("new testPublicNewTr");
    }

    /**
     * 无事务公开方法
     */
    public void testPublicNewNoTr(){
        logger.info("新开事务公开方法-事务开始");
        this.jdbcTemplate.update(this.transactionalProperties.UPDATE_TESTPUBLICNEWTR_SQL);
        logger.info("新开事务公开方法-事务结束");
    }

}
