package com.shinemo.score.client.score.domain;

import com.shinemo.client.common.BaseDO;
import com.shinemo.score.client.video.domain.VideoExtend;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
public class MyScoreRequest extends BaseDO{

    private String videoId;
    private String videoName;
    private VideoExtend extend;

}
