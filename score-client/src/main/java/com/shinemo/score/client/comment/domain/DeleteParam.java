package com.shinemo.score.client.comment.domain;

import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-08-21
 */
@Data
public class DeleteParam {

    // 评论或回复id
    private Long id;

    /**
     * 删除类型 1.评论 2.回复
     * 目前只有删除接口需要
     */
    private Integer type;
}
