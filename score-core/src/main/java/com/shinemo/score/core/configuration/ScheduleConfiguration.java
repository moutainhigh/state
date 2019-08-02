package com.shinemo.score.core.configuration;



import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class ScheduleConfiguration {



    public FactoryBean<JobDetail> methodInvokingJobDetailFactoryBean(String beanName) {
        MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        methodInvokingJobDetailFactoryBean.setTargetBeanName(beanName);
        methodInvokingJobDetailFactoryBean.setTargetMethod("execute");
        methodInvokingJobDetailFactoryBean.setConcurrent(false);
        return methodInvokingJobDetailFactoryBean;
    }

    public FactoryBean<CronTrigger> cronTriggerFactoryBean(JobDetail jobDetail, String cronExpression) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setCronExpression(cronExpression);
        return cronTriggerFactoryBean;
    }

    @Bean(name = "calculationScoreHourDetail")
    public FactoryBean<JobDetail> calculationScoreDetail() {
        return methodInvokingJobDetailFactoryBean("calculationScoreHourTask");
    }

    @Bean(name = "calculationScoreDayDetail")
    public FactoryBean<JobDetail> calculationScoreDayDetail() {
        return methodInvokingJobDetailFactoryBean("calculationScoreDayTask");
    }


    @Bean(name = "calculationScoreHourTrigger")
    @DependsOn({"calculationScoreHourDetail"})
    public FactoryBean<CronTrigger> calculationScoreHourTrigger(@Qualifier("calculationScoreHourDetail") JobDetail jobDetail) {
        return cronTriggerFactoryBean(jobDetail, "0 0 0/1 * * ?");
    }

    @Bean(name = "calculationScoreDayTrigger")
    @DependsOn({"calculationScoreDayDetail"})
    public FactoryBean<CronTrigger> calculationScoreDayTrigger(@Qualifier("calculationScoreDayDetail") JobDetail jobDetail) {
        return cronTriggerFactoryBean(jobDetail, "0 0 1 * * ?");
    }

    @Bean(name = "calculationScoreMinuteDetail")
    public FactoryBean<JobDetail> calculationScoreMinuteDetail() {
        return methodInvokingJobDetailFactoryBean("calculationScoreMinuteTask");
    }


    @Bean(name = "calculationScoreMinuteTrigger")
    @DependsOn({"calculationScoreMinuteDetail"})
    public FactoryBean<CronTrigger> calculationScoreMinuteTrigger(@Qualifier("calculationScoreMinuteDetail") JobDetail jobDetail) {
        return cronTriggerFactoryBean(jobDetail, "0 0/5 * * * ?");
    }


    @Bean(name = "calculationScheduler")
    @DependsOn({"calculationScoreMinuteTrigger"})
    public FactoryBean<Scheduler> orderScheduler(@Qualifier("calculationScoreDayTrigger") CronTrigger calculationScoreDayTrigger,
                                                 @Qualifier("calculationScoreHourTrigger") CronTrigger calculationScoreHourTrigger) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(calculationScoreDayTrigger,calculationScoreHourTrigger);
        return schedulerFactoryBean;
    }
}
