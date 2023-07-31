package com.example.demo.config;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;

public class SchedulConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

    }

    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5); // 최소로 대기하는 스레드 갯수
        taskExecutor.setMaxPoolSize(10); // 최대로 요청할 수 있는 스레드 갯수 최대가 넘으면 큐에 저장되서 기다림
        taskExecutor.setQueueCapacity(25); // 큐에 대기될 수 있는 최대수 여기서 넘으면 ThreadPoolExecutor 오류 발생
        taskExecutor.initialize(); //
        return taskExecutor;
    }
}
