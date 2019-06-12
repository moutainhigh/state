package com.shinemo.score.core.configuration;

import com.shinemo.client.async.InternalEventBus;
import com.shinemo.score.dal.configuration.ShineMoProperties;
import com.shinemo.client.mail.SendMailService;
import com.shinemo.client.mail.SendMailServiceWrapper;
import com.shinemo.client.util.EnvUtil;
import com.shinemo.jce.common.config.CenterConfig;
import com.shinemo.jce.common.config.ConsumerConfig;
import com.shinemo.jce.common.config.ProviderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * core 配置类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Configuration
public class CoreConfiguration {

    @Resource
    private ShineMoProperties shineMoProperties;

    /**
     * 实例化 provider 配置
     *
     * @return com.shinemo.jce.common.config.ProviderConfig
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean(initMethod="init")
    public ProviderConfig providerConfig() {
        ShineMoProperties.Provider provider = shineMoProperties.getJce().getProvider();
        ProviderConfig config = new ProviderConfig();
        config.setPort(provider.getPort());
        config.setProxy(provider.getProxy());
        return config;
    }

    /**
     * 实例化 consumer 配置
     *
     * @return com.shinemo.jce.common.config.ConsumerConfig
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean(initMethod="init")
    public ConsumerConfig consumerConfig() {
        ShineMoProperties.Consumer consumer = shineMoProperties.getJce().getConsumer();
        ConsumerConfig config = new ConsumerConfig();
        config.setUrl(consumer.getUrl());
        config.setUrlMap(consumer.getUrlMap());
        return config;
    }

    /**
     * 实例化 center 配置
     *
     * @param name
     * @return com.shinemo.jce.common.config.CenterConfig
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean(initMethod="init")
    public CenterConfig centerConfig(@Value("${spring.application.name}") String name) {
        ShineMoProperties.Center center = shineMoProperties.getJce().getCenter();
        CenterConfig config = new CenterConfig();
        config.setIpAndPort(center.getHost());
        config.setAppName(name);
        return config;
    }

    /**
     * EnvUtil
     *
     * @param envName
     * @return com.shinemo.client.util.EnvUtil
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public EnvUtil envUtil(@Value("${spring.profiles.active}") String envName) {
        ShineMoProperties.Application application = shineMoProperties.getApplication();
        EnvUtil envUtil = new EnvUtil();
        envUtil.setEnv(application.getEnv());
        envUtil.setEnvName(envName);
        return envUtil;
    }

    /**
     * 发邮件类
     *
     * @param applicationName
     * @return com.shinemo.client.mail.SendMailServiceWrapper
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public SendMailServiceWrapper sendMailServiceWrapper(@Value("${spring.application.name}") String applicationName) {
        SendMailServiceWrapper sendMailServiceWrapper = new SendMailServiceWrapper();
        sendMailServiceWrapper.setSendMailService(new SendMailService());
        sendMailServiceWrapper.setApplicationName(applicationName);
        sendMailServiceWrapper.setEmails(shineMoProperties.getNotificationEmail());
        return sendMailServiceWrapper;
    }

    @Bean
    public InternalEventBus internalEventBus(){
        return new InternalEventBus();
    }
}
