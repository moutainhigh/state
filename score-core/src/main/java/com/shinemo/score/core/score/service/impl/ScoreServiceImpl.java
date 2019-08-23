package com.shinemo.score.core.score.service.impl;

import com.shinemo.client.common.Errors;
import com.shinemo.client.common.ListVO;
import com.shinemo.client.common.Result;
import com.shinemo.client.exception.BizException;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.client.score.domain.ScoreCountDO;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;
import com.shinemo.score.core.score.service.ScoreService;
import com.shinemo.score.dal.score.wrapper.ScoreWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {

    private final static Long INIT_VERSION = 1L;

    private final static int MAX_TIMES = 4;

    @Resource
    private ScoreWrapper scoreWrapper;

    @Override
    public Result<ScoreDO> insertScore(ScoreDO domain) {

        Assert.notNull(domain.getVideoId(), "videoId is null");
        Assert.notNull(domain.getScore(), "score is null");
        Assert.notNull(domain.getUid(), "uid is null");
        ScoreQuery query = new ScoreQuery();
        query.setUid(domain.getUid());
        query.setVideoId(domain.getVideoId());
        query.setRealVideoId(domain.getRealVideoId());
        Result<ScoreDO> rs = scoreWrapper.get(query);
        if (!rs.hasValue()) {
            try {
                query.setRealVideoId(null);
                query.setVideoId(null);
                Result<ScoreDO> rz = scoreWrapper.getScoreByMaxNum(query);
                if (rz.hasValue()) {
                    domain.setNum(rz.getValue().getNum() + INIT_VERSION);
                } else {
                    domain.setNum(INIT_VERSION);
                }
                domain.setVersion(INIT_VERSION);
                scoreWrapper.insert(domain);//不用处理返回值 不成功肯定异常
            } catch (Exception e) {//这里异常可能会是 uid+num冲突，或者video+uid冲突 需要更新数量
                log.error("[insert] sameRequest error", e);
                return uptFourTimes(domain, true);
            }
        } else {
            ScoreDO scoreDO = new ScoreDO();
            scoreDO.setId(rs.getValue().getId());
            scoreDO.setVersion(rs.getValue().getVersion());
            scoreDO.setScore(domain.getScore());
            Result<ScoreDO> rt = scoreWrapper.update(scoreDO);
            if (!rt.hasValue()) {
                return uptFourTimes(scoreDO, false);
            }
        }
        return Result.success(domain);
    }

    private Result<ScoreDO> uptFourTimes(ScoreDO domain, boolean uptNum) {
        int i = 0;
        while (i < MAX_TIMES) {//保证一定更新成功,并发4以下,如果还失败需要查看日志例如extend 长度超过限制
            if (updateScore(domain, false)) {
                break;
            }
            i++;
        }
        if (i == MAX_TIMES) {
            log.error("[insertScore] try more 4 times ");
            return Result.error(ScoreErrors.DO_NOT_REPEAT_OPERATE);
        }
        return Result.success(domain);
    }

    @Override
    public Result<ScoreDO> getScore(ScoreQuery query) {
        return scoreWrapper.get(query);
    }

    @Override
    public Result<ListVO<ScoreDO>> findScores(ScoreQuery query) {
        return scoreWrapper.find(query);
    }

    @Override
    public Result<List<ScoreCountDO>> getScoreCounts(List<Long> videoIds) {
        return scoreWrapper.getScoreCounts(videoIds);
    }


    /**
     * 循环更新
     *
     * @param domain
     * @return
     */
    private boolean updateScore(ScoreDO domain, boolean uptNum) {

        ScoreQuery query = new ScoreQuery();
        query.setUid(domain.getUid());
        query.setVideoId(domain.getVideoId());
        query.setRealVideoId(domain.getRealVideoId());
        Result<ScoreDO> rs = scoreWrapper.get(query, Errors.FAILURE);
        if (!rs.hasValue()) {//TODO 如果不存在 这里进行num 自增然后插入 下面num代码挪上来
            throw new BizException(rs.getError());
        }
        if (uptNum) {
            query.setVideoId(null);
            Result<ScoreDO> rz = scoreWrapper.getScoreByMaxNum(query);
            if (rz.hasValue()) {
                domain.setNum(rz.getValue().getNum() + INIT_VERSION);
            } else {
                domain.setNum(INIT_VERSION);
            }
        }
        domain.setId(rs.getValue().getId());
        domain.setVersion(rs.getValue().getVersion());
        Result<ScoreDO> rt = scoreWrapper.update(domain, ScoreErrors.DO_NOT_REPEAT_OPERATE);
        if (!rt.hasValue()) {
            return false;
        }
        return true;
    }
}
