package com.shinemo.score.dal.configuration;

import com.shinemo.myconf.client.jdbc.druid.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * dal 配置类
 *
 * @author zhangyan
 * @date 2018-05-29
 */
@Configuration
@MapperScan(basePackages = {
        "com.shinemo.score.dal.user.mapper",
})
@ComponentScan(basePackages = {
        "com.shinemo.score.dal.user.wrapper",
})
@EnableTransactionManagement(proxyTargetClass = true)
public class DalConfiguration {

    @Resource
    private ShineMoProperties shineMoProperties;

    /**
     * 实例化 dataSource
     *
     * @return javax.sql.DataSource
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() {
        ShineMoProperties.Application application = shineMoProperties.getApplication();
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDbName(application.getDatabaseName());
        return dataSource;
    }
}
