package com.shinemo.score.web.controller;

import com.shinemo.client.common.Errors;
import com.shinemo.client.common.Result;
import com.shinemo.client.util.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 错误处理
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Slf4j
@ControllerAdvice
public class ErrorController {

    /**
     * 默认错误处理类
     *
     * @param e
     * @return com.shinemo.client.common.Result&lt;T&gt;
     * @author zhangyan
     * @date 2018-05-29
     **/
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public <T> Result<T> defaultErrorHandler(HttpServletRequest request, Throwable e) {
        Result<T> result = Result.error(Errors.SERVICE_ERROR);
        log.error(String.format("[defaultErrorHandler] url:%s, result:%s", request.getRequestURL(), result), e);
        if (!EnvUtil.isOnline()) {
            result.setThrowable(e);
        }
        return result;
    }
}
