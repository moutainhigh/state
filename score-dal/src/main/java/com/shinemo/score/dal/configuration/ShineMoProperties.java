package com.shinemo.score.dal.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * ShineMo 配置文件
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shinemo")
@EnableConfigurationProperties(ShineMoProperties.class)
public class ShineMoProperties {
    /**
     * 应用相关配置
     */
    @Valid
    @NotNull
    private Application application;
    /**
     * jce 相关配置
     */
    @Valid
    @NotNull
    private Jce jce;
    /**
     * 通知邮箱列表
     */
    @NotNull
    private List<String> notificationEmail;

    /**
     * 初始化权重
     */
    @NotNull
    private Long initWeight;

    /**
     * 初始化分数
     */
    @NotNull
    private Long initScore;


    /**
     * 应用相关配置
     *
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Data
    public static class Application {
        /**
         * 环境
         *
         * @see com.shinemo.client.util.EnvUtil#ENV_DAILY
         * @see com.shinemo.client.util.EnvUtil#ENV_DEV
         * @see com.shinemo.client.util.EnvUtil#ENV_PREF
         * @see com.shinemo.client.util.EnvUtil#ENV_SANDBOX
         * @see com.shinemo.client.util.EnvUtil#ENV_ONLINE
         */
        @NotNull
        private Integer env;
        /**
         * 数据库名称
         */
        @NotNull
        private String databaseName;
        /**
         * 域名
         */
        @NotNull
        private String domain;
    }

    /**
     * jce 相关配置
     *
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Data
    public static class Jce {
        /**
         * center 相关配置
         */
        @Valid
        @NotNull
        private Center center;
        /**
         * provider 相关配置
         */
        @Valid
        @NotNull
        private Provider provider;
        /**
         * consumer 相关配置
         */
        @Valid
        @NotNull
        private Consumer consumer;
    }

    /**
     * center 相关配置
     *
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Data
    public static class Center {
        /**
         * center host
         */
        @NotNull
        private String host;
    }

    /**
     * provider 相关配置
     *
     * @author zhangyan
     * @date 2018-05-29
     */
    @Data
    public static class Provider {
        /**
         * 监听端口号
         */
        private Integer port;
        /**
         * proxy
         */
        private String proxy;
    }

    /**
     * consumer 相关配置
     *
     * @author zhangyan
     * @date 2018-05-29
     */
    @Data
    public static class Consumer {
        /**
         * url 和 urlMap 二选一
         */
        @NotNull
        private String url;
        /**
         * url 和 urlMap 二选一
         */
        private Map<String, String> urlMap;
        /**
         * consumer proxy
         */
        @Valid
        @NotNull
        private Proxy proxy;
    }

    /**
     * consumer proxy
     *
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Data
    public static class Proxy {
        /**
         * entpay 的 proxy
         */
        @NotNull
        private String entpay;

        @NotNull
        private String mgsuggest;

        @NotNull
        private String muic;

        @NotNull
        private String barrage;

    }
}
