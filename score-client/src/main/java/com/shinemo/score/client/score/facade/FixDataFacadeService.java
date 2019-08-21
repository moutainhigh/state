package com.shinemo.score.client.score.facade;

import com.shinemo.client.common.Result;

public interface FixDataFacadeService{

    Result<Void> fixVideo();

    Result<Void> initScore();

    Result<Void> addOnlineScore();

    Result<Void> fixNum();
}
