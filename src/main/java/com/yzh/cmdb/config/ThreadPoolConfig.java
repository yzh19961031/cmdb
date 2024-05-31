package com.yzh.cmdb.config;

import cn.hutool.core.thread.NamedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池配置
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {

    /**
     * 项目共用线程池
     */
    public static final String CMDB_EXECUTOR = "cmdbExecutor";


    @Override
    public Executor getAsyncExecutor() {
        return cmdbExecutor();
    }

    @Bean(CMDB_EXECUTOR)
    @Primary
    public Executor cmdbExecutor() {
        return new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(100),
                new NamedThreadFactory("cmdb-executor-", true),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
