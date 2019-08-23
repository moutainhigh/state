package com.shinemo.score.client.video.domain;

import com.shinemo.client.common.BaseDO;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VideoDTO extends BaseDO{

    private String videoId;
    private double score;
    private long weight;

    private String realVideoId;

}
