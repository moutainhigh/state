package com.shinemo.score.core.configuration;

import com.shinemo.score.client.user.facade.UserInfoFacadeService;
import com.shinemo.jce.spring.AaceProviderBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * provider 配置类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Configuration
public class ProviderConfiguration {

    /**
     * UserInfoFacadeService 提供 jce 方式调用
     *
     * @param userInfoFacadeService
     * @return com.shinemo.jce.spring.AaceProviderBean
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean(initMethod="init")
    @DependsOn("userInfoFacadeService")
    public AaceProviderBean providerUserInfoFacadeService(@Qualifier("userInfoFacadeService") UserInfoFacadeService userInfoFacadeService) {
        AaceProviderBean aaceProviderBean = new AaceProviderBean();
        aaceProviderBean.setInterfaceName(UserInfoFacadeService.class.getName());
        aaceProviderBean.setTarget(userInfoFacadeService);
        // aaceProviderBean.setRpcType(RpcType.ACE.getName());
        return aaceProviderBean;
    }
}