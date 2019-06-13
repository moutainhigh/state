package com.shinemo.score.core.score.service.impl;

import com.shinemo.client.common.Result;
import com.shinemo.client.exception.BizException;
import com.shinemo.client.util.GsonUtil;
import com.shinemo.score.client.error.ScoreErrors;
import com.shinemo.score.client.score.domain.ScoreDO;
import com.shinemo.score.client.score.query.ScoreQuery;
import com.shinemo.score.core.score.service.ScoreService;
import com.shinemo.score.dal.score.wrapper.ScoreWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;


@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService{

    private final static Long INIT_VERSION = 1L;

    private final static int MAX_TIMES = 4;

    @Resource
    private ScoreWrapper scoreWrapper;

    @Override
    public Result<ScoreDO> insertScore(ScoreDO domain) {

        Assert.notNull(domain.getVideoId(),"videoId is null");
        Assert.notNull(domain.getScore(),"score is null");
        Assert.notNull(domain.getUid(),"uid is null");
        ScoreQuery query = new ScoreQuery();
        query.setUid(domain.getUid());
        query.setVideoId(domain.getVideoId());
        Result<ScoreDO> rs = scoreWrapper.get(query);
        if(!rs.hasValue()){
            try {
                query.setVideoId(null);
                Result<Long> rz = scoreWrapper.count(query);
                if(rz.hasValue()){
                    domain.setNum(rz.getValue()+INIT_VERSION);
                }else{
                    domain.setNum(INIT_VERSION);
                }
                domain.setVersion(INIT_VERSION);
                Result<ScoreDO> rt = scoreWrapper.insert(domain);
            } catch (Exception e){
                int i = 0;
                while (i < MAX_TIMES){//保证一定更新成功,并发4以下,如果还失败需要查看日志例如extend 长度超过限制
                    if(updateScore(domain)){
                        break;
                    }
                    i ++;
                }
                if(i == MAX_TIMES){
                    log.error("[insertScore] try more 4 times ",e);
                    return Result.error(ScoreErrors.DO_NOT_REPEAT_OPERATE);
                }
            }
        }
        return Result.success(domain);
    }

    @Override
    public Result<ScoreDO> getScore(ScoreQuery query) {
        return scoreWrapper.get(query);
    }


    /**
     * 循环更新
     * @param domain
     * @return
     */
    private boolean updateScore(ScoreDO domain){

        ScoreQuery query = new ScoreQuery();
        query.setUid(domain.getUid());
        query.setVideoId(domain.getVideoId());
        Result<ScoreDO> rs = scoreWrapper.get(query);
        if(!rs.hasValue()){
            throw new BizException(rs.getError());
        }
        query.setVideoId(null);
        Result<Long> rz = scoreWrapper.count(query);
        if(rz.hasValue()){
            domain.setNum(rz.getValue()+INIT_VERSION);
        }else{
            domain.setNum(INIT_VERSION);
        }
        domain.setId(rs.getValue().getId());
        domain.setVersion(rs.getValue().getVersion());
        Result<ScoreDO> rt = scoreWrapper.update(domain, ScoreErrors.DO_NOT_REPEAT_OPERATE);
        if(!rt.hasValue()){
            return false;
        }
        return true;
    }
}
