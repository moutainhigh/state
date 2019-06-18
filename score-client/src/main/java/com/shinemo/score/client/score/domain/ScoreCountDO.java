package com.shinemo.score.client.score.domain;

import com.shinemo.client.common.BaseDO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreCountDO extends BaseDO {
    private Long videoId;
    private Long score;
    private Long num;
}
