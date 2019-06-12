package com.shinemo.score.client.video.domain;


import com.shinemo.buy.client.order.domain.OrderFlag;
import com.shinemo.client.common.BaseEnum;
import com.shinemo.client.common.Flag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  VideoFlag implements Flag.BaseEnum<VideoFlag> {

    GRADE(1, 1L, "是否可以打分");

    private final int index;
    private final long mask;
    private final String desc;

    public static VideoFlag getByIndex(final int index) {
        for (VideoFlag type : VideoFlag.values()) {
            if (type.index == index) {
                return type;
            }
        }
        return null;
    }
}
