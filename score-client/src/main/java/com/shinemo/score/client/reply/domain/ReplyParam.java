package com.shinemo.score.client.reply.domain;

import com.shinemo.client.common.BaseDO;
import lombok.Data;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Data
public class ReplyParam extends BaseDO {

    private Long commentId;

    private String comment;

    private String netType;

    private Integer showDevice = 1;

    public boolean showDeviceType() {
        if (showDevice == null || showDevice == 2) {
            return false;
        }
        return true;
    }
}
