package com.shinemo.score.web.application;

import com.shinemo.score.core.word.SensitiveWordInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@SpringBootApplication(scanBasePackages = {
        "com.shinemo.score.dal.configuration",
        "com.shinemo.score.core.configuration",
        "com.shinemo.score.web",
})
public class Application extends SpringBootServletInitializer {

    private static final Class<Application> application = Application.class;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.application);
    }

    public static void main(String[] args) throws Exception {
        // app_env 在 10.0.10.42 的 publish.site 表中
        // -Dapp_env=-1 -Dconfig_url=http://10.0.10.62/myconf/conf/dispatch -Dapp_name=score
        // java -Dapp_env=-1 -Dconfig_url=http://10.0.10.62/myconf/conf/dispatch -Dapp_name=score -jar score-web/target/score.jar

        // 初始化敏感词库
        SensitiveWordInit.initKeyWord();

        SpringApplication.run(application, args);
    }

}
