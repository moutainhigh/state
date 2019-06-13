package com.shinemo.score.web.controller;

import com.shinemo.client.common.WebResult;
import com.shinemo.score.core.comment.cache.CommentCache;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wenchao.li
 * @since 2019-06-13
 */
@RestController
@RequestMapping("backdoor")
public class BackdoorController {

    @Resource
    private CommentCache commentCache;

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


}
