package com.shinemo.score.client.error;

import com.shinemo.client.common.ErrorInfo;

/**
 * @author wenchao.li
 * @since 2019-06-06
 */
public interface ScoreErrors {


    ErrorInfo MAPPER_NULL_COUNT = new ErrorInfo(80001, "MAPPER_NULL_COUNT", "mapper is null");
    ErrorInfo QUERY_NULL_COUNT = new ErrorInfo(80002, "QUERY_NULL_COUNT", "query is null");
    ErrorInfo SQL_ERROR_COUNT = new ErrorInfo(80003, "SQL_ERROR_COUNT", "sql failed execute");
    ErrorInfo MAPPER_NULL_FIND = new ErrorInfo(80004, "MAPPER_NULL_FIND", "mapper is null");
    ErrorInfo QUERY_NULL_FIND = new ErrorInfo(80005, "QUERY_NULL_FIND", "query is null");
    ErrorInfo SQL_ERROR_FIND = new ErrorInfo(80006, "SQL_ERROR_FIND", "sql failed execute");
    ErrorInfo MAPPER_NULL_GET = new ErrorInfo(80007, "MAPPER_NULL_GET", "mapper is null");
    ErrorInfo QUERY_NULL_GET = new ErrorInfo(80008, "QUERY_NULL_GET", "query is null");
    ErrorInfo SQL_ERROR_GET = new ErrorInfo(80009, "SQL_ERROR_GET", "sql failed execute");
    ErrorInfo MAPPER_NULL_INSERT = new ErrorInfo(80010, "MAPPER_NULL_INSERT", "mapper is null");
    ErrorInfo ENTITY_NULL_INSERT = new ErrorInfo(80011, "ENTITY_NULL_INSERT", "entity is null");
    ErrorInfo SQL_ERROR_INSERT = new ErrorInfo(80012, "SQL_ERROR_INSERT", "sql failed execute");
    ErrorInfo MAPPER_NULL_UPDATE = new ErrorInfo(80013, "MAPPER_NULL_UPDATE", "mapper is null");
    ErrorInfo ENTITY_NULL_UPDATE = new ErrorInfo(80014, "ENTITY_NULL_UPDATE", "entity is null");
    ErrorInfo SQL_ERROR_UPDATE = new ErrorInfo(80015, "SQL_ERROR_UPDATE", "sql failed execute");
    ErrorInfo SQL_ERROR_BATCH_INSERT = new ErrorInfo(80016, "SQL_ERROR_BATCH_INSERT", "sql failed execute");
    ErrorInfo SQL_ERROR_SUM = new ErrorInfo(80017, "SQL_ERROR_SUM", "sql failed execute");

    ErrorInfo COMMENT_NOT_EXIST = new ErrorInfo(100000L, "COMMENT_NOT_EXIST", "评论不存在");
    ErrorInfo REPLY_NOT_EXIST = new ErrorInfo(100001L, "REPLY_NOT_EXIST", "回复不存在");
    ErrorInfo LIKE_LOG_NOT_EXIST = new ErrorInfo(100002L, "LIKE_LOG_NOT_EXIST", "赞记录不存在");
    ErrorInfo DO_NOT_REPEAT_LIKE = new ErrorInfo(100003L, "DO_NOT_REPEAT_LIKE", "请勿重复点赞");
    ErrorInfo HAS_NOT_LIKE = new ErrorInfo(100004L, "HAS_NOT_LIKE", "您还未点赞");
    ErrorInfo VIDEO_NOT_EXIST = new ErrorInfo(100005L, "VIDEO_NOT_EXIST", "视频不存在");
    ErrorInfo DO_NOT_REPEAT_OPERATE = new ErrorInfo(100006L, "DO_NOT_REPEAT_OPERATE", "请勿重复操作");
    ErrorInfo COMMENT_IS_CLOSED = new ErrorInfo(100007L, "COMMENT_IS_CLOSE", "评论关闭");

    ErrorInfo PARAM_ERROR = new ErrorInfo(300001L, "PARAM_ERROR", "参数错误");


}
