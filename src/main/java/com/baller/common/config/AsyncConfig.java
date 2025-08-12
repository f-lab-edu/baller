package com.baller.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean("sseListenerPool")
    public Executor sseListenerPool() {
        return Executors.newFixedThreadPool(4, r -> {
            Thread t = new Thread(r, "sse-listener");
            t.setDaemon(true);
            return t;
        });
    }

}
