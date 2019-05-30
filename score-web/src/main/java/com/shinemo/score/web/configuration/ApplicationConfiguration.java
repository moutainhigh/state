package com.shinemo.score.web.configuration;

import com.shinemo.client.filter.LoggerFilter;
import com.shinemo.client.interceptor.IntranetInterceptor;
import com.shinemo.client.interceptor.LocalhostInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.Filter;

/**
 * 工程配置类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Configuration
public class ApplicationConfiguration {


    /**
     * IntranetInterceptor
     *
     * @return org.springframework.web.servlet.HandlerInterceptor
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public HandlerInterceptor intranetInterceptor() {
        return new IntranetInterceptor();
    }


    /**
     * LocalhostInterceptor
     *
     * @return org.springframework.web.servlet.HandlerInterceptor
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public HandlerInterceptor localhostInterceptor() {
        return new LocalhostInterceptor();
    }

    /**
     * LoggerFilter
     *
     * @return javax.servlet.Filter
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public Filter loggerFilter() {
        return new LoggerFilter();
    }
}
