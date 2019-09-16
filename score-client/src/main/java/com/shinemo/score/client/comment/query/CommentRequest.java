package com.shinemo.score.client.comment.query;


import com.shinemo.client.common.BaseDO;
import com.shinemo.client.common.FlagHelper;
import com.shinemo.score.client.comment.domain.CommentFlag;
import com.shinemo.score.client.reply.domain.ReplyDO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wenchao.li
 * @since 2019-06-06
 */
@Getter
@Setter
public class CommentRequest extends BaseDO {

    private Long commentId;
    /**
     * 删除状态 1-正常 0-删除
     */
    private Integer status;
    /**
     * 视频id
     */
    private String videoId;
    /**
     * 视频类型
     */
    private Integer videoType;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 赞数量操作 1.添加 2.删除
     */
    private Integer likeAction;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 头像
     */
    private String avatarUrl;
    /**
     * 设备
     */
    private String device;
    /**
     * 网络类型
     */
    private String netType;
    /**
     * 排序权重
     */
    private Integer sort;
    /**
     * 扩展字段
     */
    private String extend;
    /**
     * 最近的回复(最近3条)
     */
    private List<ReplyDO> historyReply;
    /**
     * 版本号
     */
    private Long version;

    /**
     * 回复数量递增
     */
    private boolean incrReplyNum;
    /**
     * 回复数量递增
     */
    private boolean subReplyNum;

    /**
     * 完整机型
     */
    private String fullDevice;
    /**
     * 审核状态
     */
    private Integer verifyStatus;
    /**
     * 打标
     * @see CommentFlag
     */
    private Long flag;

    private FlagHelper commentFlag = FlagHelper.build();

    private String ip;

    private String realVideoId;
}
