package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.WebResult;
import com.shinemo.score.client.score.domain.ScoreRequest;
import com.shinemo.score.client.score.facade.ScoreFacadeService;
import org.springframework.stereotype.Service;

/**
 * @author wenchao.li
 * @since 2019-06-12
 */
@Service("scoreFacadeService")
public class ScoreFacadeServiceImpl implements ScoreFacadeService {


    @Override
    public WebResult<Void> submitScore(ScoreRequest request) {

        // 评分
        if (request.getScore() != null && request.getScore() > 0) {
        } else {
            // 评论或回复


        }

        return null;
    }
}
