package com.shinemo.score.client.score.domain;

import com.shinemo.client.common.BaseDO;
import com.shinemo.client.common.ListVO;
import com.shinemo.score.client.comment.domain.CommentVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MyScoreDTO extends BaseDO{

    private Long number;
    private String videoId;
    private Double score;
    private List<CommentVO> comments;

}
