package com.example.demo.config;

import com.example.demo.entity.QS3File;
import com.example.demo.enumCustom.S3EntityType;
import com.example.demo.repository.S3FileRespository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulConfig implements SchedulingConfigurer {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                ()-> {
                    QS3File qs3File = QS3File.s3File;
                    jpaQueryFactory
                            .delete(qs3File)
                            .where(qs3File.s3EntityType.eq(S3EntityType.NON_USED))
                            .execute();
                },
                triggerContext -> new CronTrigger("0 0 6 * * ?").nextExecutionTime(triggerContext) //매일 오전 6시
        );
    }

    @Bean
    public Executor taskExecutor() {
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);
        executor.setRemoveOnCancelPolicy(true);
        return executor;
    }
}
