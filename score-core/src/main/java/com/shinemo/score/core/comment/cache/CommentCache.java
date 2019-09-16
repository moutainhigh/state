package com.shinemo.score.core.comment.cache;

import com.shinemo.client.common.Result;
import com.shinemo.management.client.config.domain.SystemConfig;
import com.shinemo.management.client.config.domain.SystemConfigEnum;
import com.shinemo.management.client.config.domain.SystemConfigRequest;
import com.shinemo.management.client.config.facade.SystemConfigFacadeService;
import com.shinemo.my.redis.service.RedisService;
import com.shinemo.score.client.comment.domain.CommentDO;
import com.shinemo.score.core.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wenchao.li
 * @since 2019-06-10
 */
@Component
@Slf4j
public class CommentCache {

    @Resource
    private CommentService commentService;

    @Resource
    private RedisService redisService;

    @Resource
    private SystemConfigFacadeService systemConfigFacadeService;

    // comment redis key
    private final static String COMMENT_KEY = "migu_comment_%s";
    // comment redis key expired time 1天
    private final static Integer COMMENT_KEY_EXPIRE = 60 * 60 * 24;

    private final static  String COMMON_SWITCH_KEY = "migu_common_switch";

    private final static  String COMMON_MODEL_KEY = "migu_model_switch";

    public void delCommentConfig(){
        redisService.del(COMMON_MODEL_KEY);
    }

    public Integer getCommentConfig(){
        Integer ret = redisService.get(COMMON_MODEL_KEY,Integer.class);
        if(ret == null){
            SystemConfigRequest request = new SystemConfigRequest();
            request.setSysKey(SystemConfigEnum.COMMENT_VERIFY_FIRST.getKey());
            Result<SystemConfig> result = systemConfigFacadeService.getSysConfig(request);
            if(!result.hasValue()){
                log.error("[getSysConfig] error ret:{}",result);
                return SystemConfigEnum.COMMENT_VERIFY_LAST.getId();
            }
            redisService.set(COMMON_MODEL_KEY,result.getValue(),60*60*60);
        }
        return ret;
    }

    public Boolean getCommonSwitch(){
        Boolean ret = redisService.get(COMMON_SWITCH_KEY,Boolean.class);
        return ret;
    }

    public void setCommonSwitch(Boolean commonSwitch){
        redisService.set(COMMON_SWITCH_KEY,commonSwitch,60*10);
    }


    public CommentDO get(Long key) {
        try {
            // 先走redis
            CommentDO comment = redisService.get(keyFormat(key), CommentDO.class);
            if (comment != null) {
                return comment;
            }
            // 最终走db
            comment = commentService.getByIdFromDB(key);
            // 加入缓存
            put(comment);
            return comment;
        } catch (Exception e) {
            log.error("[commentCache] get cache has ex", e);
            return null;
        }
    }

    public void put(CommentDO commentDO) {
        redisService.set(keyFormat(commentDO.getId()), commentDO, COMMENT_KEY_EXPIRE);
    }

    // 清除key
    public void remove(Long key) {
        redisService.del(keyFormat(key));
    }

    // 刷新缓存
    public void refresh(Long key) {

        // 先删除
        remove(key);

        put(commentService.getByIdFromDB(key));
    }


    private String keyFormat(Long key) {
        return String.format(COMMENT_KEY, key);
    }
}
