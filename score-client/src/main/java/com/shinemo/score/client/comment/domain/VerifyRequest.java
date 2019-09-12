package com.shinemo.score.client.comment.domain;

import com.shinemo.client.common.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VerifyRequest extends BaseDO {

    private List<Long> commentIds;
    private Integer verifyStatus;
}
