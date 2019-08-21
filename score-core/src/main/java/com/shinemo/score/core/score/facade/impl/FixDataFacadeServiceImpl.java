package com.shinemo.score.core.score.facade.impl;

import com.shinemo.client.common.Result;
import com.shinemo.score.client.score.facade.FixDataFacadeService;
import com.shinemo.score.dal.score.mapper.VideoTmpMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class FixDataFacadeServiceImpl implements FixDataFacadeService {

    @Resource
    private VideoTmpMapper videoTmpMapper;


    @Override
    public Result<Void> fixVideo() {
        return null;
    }

    @Override
    public Result<Void> initScore() {
        return null;
    }

    @Override
    public Result<Void> addOnlineScore() {
        return null;
    }

    @Override
    public Result<Void> fixNum() {
        return null;
    }
}
