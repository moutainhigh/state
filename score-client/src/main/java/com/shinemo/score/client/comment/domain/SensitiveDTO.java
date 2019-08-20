package com.shinemo.score.client.comment.domain;

import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-08-20
 */
@Data
public class SensitiveDTO {
    /**
     * 是否含有敏感词
     */
    private boolean hasSensitiveWord;

    /**
     * 敏感词处理后内容
     */
    private String sensitiveContent;

}
