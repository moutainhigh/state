package com.shinemo.score.client.reply.domain;

import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-08-21
 */
@Data
public class ReplyExtend {

    /**
     * 敏感词处理后的内容
     */
    private String sensitiveContent;
}
