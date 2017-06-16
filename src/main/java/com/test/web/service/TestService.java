package com.test.web.service;

import com.test.web.properties.TestProperties;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by 青葱的熊猫 on 2017/5/26.
 * 测试Services层
 */
@Service
@Transactional
public class TestService {

    private Gson gson = new Gson();

    @Autowired
    private TestProperties testProperties;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String find(){
        logger.error("日志输出");
        return this.jdbcTemplate.queryForList(this.testProperties.FIND_COUNT_SQL, String.class).toString();
    }

    public String findList(){
        logger.error("日志输出");
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(this.testProperties.FIND_LIST_SQL);
        return this.gson.toJson(list);
    }
}
