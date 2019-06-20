package com.shinemo.score.test.core.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * 测试启动类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@SpringBootApplication(scanBasePackages = {
        "com.shinemo.score.dal.configuration",
        "com.shinemo.score.core.configuration",
})
@PropertySource("classpath:conf/application-dev.properties")
public class ApplicationTest {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationTest.class, args);
    }
}
