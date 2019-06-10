package com.shinemo.score.client.comment.domain;

import com.shinemo.client.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
@AllArgsConstructor
public enum LikeTypeEnum implements BaseEnum<LikeTypeEnum> {

    ADD(1, "点赞"),
    REMOVE(2, "取消赞"),;

    private @Getter int id;
    private @Getter String name;
}
