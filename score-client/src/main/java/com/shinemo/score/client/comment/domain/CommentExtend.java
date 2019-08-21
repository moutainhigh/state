package com.shinemo.score.client.comment.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wenchao.li
 * @since 2019-08-21
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class CommentExtend {

    /**
     * 敏感词处理后内容
     */
    private String sensitiveContent;

}
