package com.shinemo.score.web.controller;

import com.shinemo.client.common.Result;
import com.shinemo.client.common.WebResult;
import com.shinemo.score.client.comment.domain.CalculationEnum;
import com.shinemo.score.client.score.facade.CalculationFacadeService;
import com.shinemo.score.core.comment.cache.CommentCache;
import com.shinemo.score.core.score.service.InnerService;
import com.shinemo.score.core.task.CalculationScoreDayTask;
import com.shinemo.score.core.task.CalculationScoreHourTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author wenchao.li
 * @since 2019-06-13
 */
@RestController
@RequestMapping("backdoor")
@Slf4j
public class BackdoorController {

    @Resource
    private CalculationFacadeService calculationFacadeService;

    @Resource
    private CommentCache commentCache;

    @Resource
    private InnerService innerService;

    private static final Executor poolExecutor = Executors.newFixedThreadPool(2);

    @Resource
    private CalculationScoreDayTask calculationScoreDayTask;

    @Resource
    private CalculationScoreHourTask calculationScoreHourTask;

    /**
     * 刷新评论缓存
     *
     * @param ids ","分割的id列表
     * @return
     */
    @GetMapping("/comment/refresh")
    public String refresh(HttpServletRequest request, @RequestParam("ids") String ids) {
        String ip = request.getRemoteAddr();
        if (!ip.equals("127.0.0.1") && !ip.equals("0:0:0:0:0:0:0:1")) {
            return "error";
        }

        if (StringUtils.isNotBlank(ids)) {
            String[] splits = ids.split(",");
            for (String v : splits) {
                commentCache.refresh(Long.valueOf(v));
            }
        }
        return "success";
    }

    @GetMapping("/calculatTest")
    public String calculatTest(HttpServletRequest request, Long id) {

        String ip = request.getRemoteAddr();
        if (!ip.equals("127.0.0.1") && !ip.equals("0:0:0:0:0:0:0:1")) {
            return "error";
        }
        if(id==1){
            calculationScoreDayTask.execute();
        }else{
            calculationScoreHourTask.execute();
        }
        return "success";
    }

    @GetMapping("/calculat")
    public String calculat(HttpServletRequest request, Long id) {
        String ip = request.getRemoteAddr();
        if (!ip.equals("127.0.0.1") && !ip.equals("0:0:0:0:0:0:0:1")) {
            return "error";
        }
        Result<Void>  rs = calculationFacadeService.calculationByTime(null,null, CalculationEnum.all,id);
        if(!rs.isSuccess()){
            log.error("[calculationByTime] error:{}",rs);
        }
        return "success";
    }


    @GetMapping("/fixNum")
    public String fixNum(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (!ip.equals("127.0.0.1") && !ip.equals("0:0:0:0:0:0:0:1")) {
            return "error";
        }
        try {
            innerService.fixScoreNum();
        } catch (Exception e) {
            log.error("[fixNum] error",e);
            return "false";
        }
        return "success";
    }




}
