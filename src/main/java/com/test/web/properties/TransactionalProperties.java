package com.test.web.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by qingcongxiongmao on 2018/5/20.
 */
@Component
//@ConfigurationProperties(prefix = "TRANSACTIONAL")
@PropertySource("classpath:properties/sql/transactional.properties")
public class TransactionalProperties {

    @Value("${UPDATE_TEST_SQL}")
    public String UPDATE_TEST_SQL;

    @Value("${UPDATE_TESTOUTPUBLICNOTR_SQL}")
    public String UPDATE_TESTOUTPUBLICNOTR_SQL;

    @Value("${UPDATE_TESTPRINOTR_SQL}")
    public String UPDATE_TESTPRINOTR_SQL;

    @Value("${UPDATE_TESTPUBLICNEWTR_SQL}")
    public String UPDATE_TESTPUBLICNEWTR_SQL;

}
