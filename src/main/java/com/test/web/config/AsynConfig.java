package com.test.web.config;

import com.test.web.properties.TaskOneProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


/**
 * Created by 青葱的熊猫 on 2017/6/19.
 * 线程池配置管理
 */
@Configuration
@EnableAsync
public class AsynConfig {

    @Autowired
    private TaskOneProperties config;

    @Bean
    public Executor oneTaskAsyncPool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(config.getMaxPoolSize());
        executor.setCorePoolSize(config.getCorePoolSize());
        executor.setQueueCapacity(config.getQueueCapacity());
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        //线程前缀
        executor.setThreadNamePrefix("myTaskAsyncPool");
/*        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());*/
        executor.initialize();
        return executor;
    }
}
