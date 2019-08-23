package com.shinemo.score.client.comment.query;

import com.shinemo.client.common.BaseDO;
import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Data
public class CommentParam extends BaseDO {

    private String comment;
    private String videoId;
    private String netType;
    private Integer videoType;

    private Integer commentId;

    // 1.显示机型 2.不显示
    private Integer showDevice;

    // 完整机型
    private String fullDevice;

    // 真实ip
    private String ip;

    private String realVideoId;

    public boolean showDeviceType() {
        if (showDevice == null || showDevice == 2) {
            return false;
        }
        return true;
    }
}
