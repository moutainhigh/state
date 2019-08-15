package com.shinemo.score.core.reply.facade.impl;

import com.shinemo.client.common.WebResult;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.jce.Constant;
import com.shinemo.jce.common.config.JceHolder;
import com.shinemo.score.client.reply.domain.ReplyDO;
import com.shinemo.score.client.reply.domain.ReplyDTO;
import com.shinemo.score.client.reply.domain.ReplyParam;
import com.shinemo.score.client.reply.facade.ReplyFacadeService;
import com.shinemo.score.client.reply.query.ReplyRequest;
import com.shinemo.score.core.reply.service.ReplyService;
import com.shinemo.ygw.client.migu.UserExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Service("replyFacadeService")
public class ReplyFacadeServiceImpl implements ReplyFacadeService {

    private Logger logger = LoggerFactory.getLogger("access");

    @Resource
    private ReplyService replyService;

    @Override
    public WebResult<ReplyDTO> submit(ReplyParam param) {

        UserExtend extend = GsonUtil.fromGson2Obj(JceHolder.get(Constant.USER_EXTEND), UserExtend.class);
        logger.info("[submit]param:{},token:{},header:{}", param, extend);

        Assert.notNull(extend, "您尚未登录");


        ReplyRequest request = new ReplyRequest();
        request.setNetType(param.getNetType());
        request.setUid(extend.getUid());
        request.setAvatarUrl(extend.getUserPortrait());
        request.setContent(param.getComment());
        if (param.showDeviceType()) {
            request.setDevice(extend.getDeviceModel());
        }
        request.setName(extend.getUserName());
        request.setMobile(extend.getMobile());
        request.setCommentId(param.getCommentId());

        ReplyDO replyDO = replyService.create(request);

        ReplyDTO dto = new ReplyDTO();
        dto.setReplyId(replyDO.getId());

        return WebResult.success(dto);
    }
}
