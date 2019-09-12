package com.shinemo.score.core.configuration;

import com.shinemo.score.client.comment.facade.AdminCommentFacadeService;
import com.shinemo.score.client.comment.facade.CommentFacadeService;
import com.shinemo.jce.spring.AaceProviderBean;
import com.shinemo.score.client.like.facade.LikeFacadeService;
import com.shinemo.score.client.reply.facade.ReplyFacadeService;
import com.shinemo.score.client.score.facade.ScoreFacadeService;
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


    @Bean(initMethod = "init")
    @DependsOn("commentFacadeService")
    public AaceProviderBean providerCommentFacadeService(@Qualifier("commentFacadeService") CommentFacadeService commentFacadeService) {
        AaceProviderBean aaceProviderBean = new AaceProviderBean();
        aaceProviderBean.setInterfaceName(CommentFacadeService.class.getName());
        aaceProviderBean.setTarget(commentFacadeService);
        return aaceProviderBean;
    }

    @Bean(initMethod = "init")
    @DependsOn("scoreFacadeService")
    public AaceProviderBean providerScoreFacadeService(@Qualifier("scoreFacadeService") ScoreFacadeService scoreFacadeService) {
        AaceProviderBean aaceProviderBean = new AaceProviderBean();
        aaceProviderBean.setInterfaceName(ScoreFacadeService.class.getName());
        aaceProviderBean.setTarget(scoreFacadeService);
        return aaceProviderBean;
    }

    @Bean(initMethod = "init")
    @DependsOn("likeFacadeService")
    public AaceProviderBean providerLikeFacadeService(@Qualifier("likeFacadeService") LikeFacadeService likeFacadeService) {
        AaceProviderBean aaceProviderBean = new AaceProviderBean();
        aaceProviderBean.setInterfaceName(LikeFacadeService.class.getName());
        aaceProviderBean.setTarget(likeFacadeService);
        return aaceProviderBean;
    }

    @Bean(initMethod = "init")
    @DependsOn("replyFacadeService")
    public AaceProviderBean providerReplyFacadeService(@Qualifier("replyFacadeService") ReplyFacadeService replyFacadeService) {
        AaceProviderBean aaceProviderBean = new AaceProviderBean();
        aaceProviderBean.setInterfaceName(ReplyFacadeService.class.getName());
        aaceProviderBean.setTarget(replyFacadeService);
        return aaceProviderBean;
    }

    @Bean(initMethod = "init")
    @DependsOn("adminCommentFacadeService")
    public AaceProviderBean providerAdminCommentFacadeService(@Qualifier("adminCommentFacadeService") AdminCommentFacadeService adminCommentFacadeService) {
        AaceProviderBean aaceProviderBean = new AaceProviderBean();
        aaceProviderBean.setInterfaceName(AdminCommentFacadeService.class.getName());
        aaceProviderBean.setTarget(adminCommentFacadeService);
        return aaceProviderBean;
    }
}