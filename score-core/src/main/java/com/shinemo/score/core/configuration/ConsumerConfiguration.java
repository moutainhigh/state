package com.shinemo.score.core.configuration;

import com.shinemo.barrage.client.barrage.facade.BarrageFacadeService;
import com.shinemo.jce.spring.AaceConsumerBean;
import com.shinemo.management.client.config.facade.SystemConfigFacadeService;
import com.shinemo.mgsuggest.client.facade.DistributeConfigFacadeService;
import com.shinemo.muic.client.token.facade.TokenFacadeService;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.score.dal.configuration.ShineMoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * consumer 配置类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Configuration
public class ConsumerConfiguration {

    @Resource
    private ShineMoProperties shineMoProperties;

    // /**
    //  * 通过 jce 远程调用 UserInfoFacadeService 的接口
    //  *
    //  * @return com.shinemo.jce.spring.AaceConsumerBean
    //  * @author zhangyan
    //  * @date 2018-05-29
    //  **/
    // @Bean(initMethod="init")
    // public AaceConsumerBean consumerUserInfoFacadeService() {
    //     ShineMoProperties.Proxy proxy = shineMoProperties.getJce().getConsumer().getProxy();
    //     AaceConsumerBean aaceConsumerBean = new AaceConsumerBean();
    //     aaceConsumerBean.setProxy(proxy.getEntpay());
    //     aaceConsumerBean.setInterfaceName(UserInfoFacadeService.class.getName());
    //     // aaceConsumerBean.setRegisterInterfaceName("xxx");
    //     // aaceConsumerBean.setAceType(AceType.HTTP.getName());
    //     // aaceConsumerBean.setRpcType(RpcType.ACE.getName());
    //     // aaceConsumerBean.setTimeout(5000);
    //     return aaceConsumerBean;
    // }


    @Bean(initMethod = "init")
    public AaceConsumerBean distributeConfigFacadeService() {
        ShineMoProperties.Proxy proxy = shineMoProperties.getJce().getConsumer().getProxy();
        AaceConsumerBean aaceConsumerBean = new AaceConsumerBean();
        aaceConsumerBean.setProxy(proxy.getMgsuggest());
        aaceConsumerBean.setInterfaceName(DistributeConfigFacadeService.class.getName());
        return aaceConsumerBean;
    }


    @Bean(initMethod = "init")
    public AaceConsumerBean tokenFacadeService() {
        ShineMoProperties.Proxy proxy = shineMoProperties.getJce().getConsumer().getProxy();
        AaceConsumerBean aaceConsumerBean = new AaceConsumerBean();
        aaceConsumerBean.setProxy(proxy.getMuic());
        aaceConsumerBean.setInterfaceName(TokenFacadeService.class.getName());
        return aaceConsumerBean;
    }

    @Bean(initMethod = "init")
    public AaceConsumerBean barrageFacadeService() {
        ShineMoProperties.Proxy proxy = shineMoProperties.getJce().getConsumer().getProxy();
        AaceConsumerBean aaceConsumerBean = new AaceConsumerBean();
        aaceConsumerBean.setProxy(proxy.getBarrage());
        aaceConsumerBean.setInterfaceName(BarrageFacadeService.class.getName());
        return aaceConsumerBean;
    }


    @Bean(initMethod = "init")
    public AaceConsumerBean systemConfigFacadeService() {
        ShineMoProperties.Proxy proxy = shineMoProperties.getJce().getConsumer().getProxy();
        AaceConsumerBean aaceConsumerBean = new AaceConsumerBean();
        aaceConsumerBean.setProxy(proxy.getManagement());
        aaceConsumerBean.setInterfaceName(SystemConfigFacadeService.class.getName());
        return aaceConsumerBean;
    }





}