package com.gomefinance.web.service;

import com.gomefinance.properties.TestProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 青葱的熊猫 on 2017/5/26.
 * 测试Services层
 */
@Service
@Transactional
public class TestService {

    @Autowired
    private TestProperties testProperties;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String find(){
        logger.error("日志输出");
        return this.jdbcTemplate.queryForObject(this.testProperties.FIND_COUNT_SQL, String.class).toString();
    }
}
