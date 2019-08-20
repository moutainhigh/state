package com.shinemo.score.client.comment.domain;

import com.shinemo.client.common.Flag;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wenchao.li
 * @since 2019-08-20
 */
@AllArgsConstructor
@Getter
public enum CommentFlag implements Flag.BaseEnum<CommentFlag> {

    HAS_SENSITIVE(1, Flag.FLAG_MASK_1, "是否含有敏感词");

    private final int index;
    private final long mask;
    private final String desc;

    public static CommentFlag getByIndex(final int index) {
        for (CommentFlag type : CommentFlag.values()) {
            if (type.index == index) {
                return type;
            }
        }
        return null;
    }
}
