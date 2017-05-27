package com.gomefinance.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by 青葱的熊猫 on 2017/5/27.
 * 资源文件映射，例如用来管理SQL等
 */
@Component
@PropertySource("classpath:properties/sql/test.properties")
public class TestProperties {

    @Value("${FIND_COUNT_SQL}")
    public String FIND_COUNT_SQL;


}
