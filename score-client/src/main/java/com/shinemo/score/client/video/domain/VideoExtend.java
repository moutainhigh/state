package com.shinemo.score.client.video.domain;

import com.shinemo.client.common.BaseDO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoExtend extends BaseDO {

    private String poster;
    private String introduce;
    private String type;
    private String mainActor;

}
