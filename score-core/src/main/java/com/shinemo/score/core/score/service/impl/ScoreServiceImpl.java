package com.shinemo.score.core.score.service.impl;

import com.shinemo.client.common.Result;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.core.score.service.ScoreService;
import com.shinemo.score.dal.score.wrapper.ScoreWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService{

    @Resource
    private ScoreWrapper scoreWrapper;

    @Override
    public Result<ScoreDO> insertScore(ScoreDO domain) {
        try {
            Result<ScoreDO> rs = scoreWrapper.insert(domain);
        } catch (Exception e) {//并发插入冲突
            log.error("[insertScore] exception domain:"+ GsonUtil.toJson(domain),e);
            updateScore(domain);
        }
        return null;
    }


    /**
     * 循环更新
     * @param domain
     * @return
     */
    private boolean updateScore(ScoreDO domain) {
        return true;
    }
}
