package com.shinemo.score.client.comment.domain;

import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-08-21
 */
@Data
public class SensitiveRequest {

    /**
     * 内容文本
     */
    private String txt;

    /**
     * 敏感词替换符号,默认*
     */
    private String replaceChar = "*";
}
