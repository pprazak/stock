package cz.rohlik.service.stock.config;

import cz.rohlik.service.stock.job.OrderExpirationJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Value("${scheduler.order-expiration-cron:0 * * * * ?}")
    private String cronExpression;

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(OrderExpirationJob.class)
                .withIdentity("orderExpirationJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("orderExpirationTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }
}
