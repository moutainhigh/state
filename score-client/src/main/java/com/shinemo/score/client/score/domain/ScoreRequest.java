package com.shinemo.score.client.score.domain;

import com.shinemo.client.common.BaseDO;
import com.shinemo.score.client.video.domain.VideoExtend;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScoreRequest extends BaseDO {

    private String videoId;
    private Integer flag;
    private String videoName;
    private String extend;
    private String comment;
    private Integer score;
    private String netType;

    // 1.显示机型 2.不显示
    private Integer showDevice = 1;
    // 完整机型
    private String fullDevice;
    // 真实ip,华为校验移动网络后返回的值
    private String ip;

}
