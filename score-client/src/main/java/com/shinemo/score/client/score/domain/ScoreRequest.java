package com.shinemo.score.client.score.domain;

import com.shinemo.client.common.BaseDO;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScoreRequest extends BaseDO{

    private String videoId;
    private Integer flag;
    private String videoName;
    private String extend;
    private String comment;
    private Integer score;
    private String netType;

}
