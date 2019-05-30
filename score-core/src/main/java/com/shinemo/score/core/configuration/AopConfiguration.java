package com.shinemo.score.core.configuration;

import com.shinemo.client.aop.log.FacadeExceptionAop;
import com.shinemo.client.aop.log.PrintParamResult;
import com.shinemo.client.aop.performance.PerformanceInner;
import com.shinemo.client.aop.performance.PerformanceOuter;
import com.shinemo.client.aop.util.SpringAopUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopConfiguration implements BeanDefinitionRegistryPostProcessor {

    /**
     * 性能监控入口的 AOP
     *
     * @return com.shinemo.client.aop.performance.PerformanceOuter
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public PerformanceOuter performanceOuter() {
        return new PerformanceOuter();
    }

    /**
     * 性能监控内部方法调用的 AOP
     *
     * @return com.shinemo.client.aop.performance.PerformanceOuter
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public PerformanceInner performanceInner() {
        return new PerformanceInner();
    }

    /**
     * 打印出入参的 AOP
     *
     * @return com.shinemo.client.aop.log.PrintParamResult
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public PrintParamResult printParamResult() {
        return new PrintParamResult();
    }

    /**
     * 拦截 facade 异常的 AOP
     *
     * @return com.shinemo.client.aop.log.FacadeExceptionAop
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public FacadeExceptionAop facadeExceptionAop() {
        return new FacadeExceptionAop();
    }

    /**
     * 定义 Pointcut
     *
     * @return org.springframework.aop.Pointcut
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    public Pointcut applicationFacadePointcut() {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression("execution(* (com.shinemo.score.client..facade.*FacadeService+).*(..))");
        return aspectJExpressionPointcut;
    }

    /**
     * 定义 Advisor
     *
     * @param performanceOuter
     * @param pointcut
     * @return org.springframework.aop.Advisor
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    @DependsOn({"performanceOuter", "applicationFacadePointcut"})
    public Advisor performanceOuterAdvisor(@Qualifier("performanceOuter") PerformanceOuter performanceOuter,
                                           @Qualifier("applicationFacadePointcut") Pointcut pointcut) {
        DefaultBeanFactoryPointcutAdvisor advisor = new DefaultBeanFactoryPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(performanceOuter);
        // advisor.setOrder(1000);
        return advisor;
    }

    /**
     * 定义 Advisor
     *
     * @param performanceInner
     * @return org.springframework.aop.Advisor
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Bean
    @DependsOn({"performanceInner"})
    public Advisor performanceInnerAdvisor(@Qualifier("performanceInner") PerformanceInner performanceInner) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("execution(* com.shinemo.score.core..*.*(..))");
        advisor.setAdvice(performanceInner);
        // advisor.setOrder(1000);
        return advisor;
    }

    /**
     * 自定义 Bean
     *
     * @param registry
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        SpringAopUtils.registerAround(
                "around",
                "printParamResult",
                Ordered.HIGHEST_PRECEDENCE,
                "applicationFacadePointcut",
                registry);
        SpringAopUtils.registerAround(
                "around",
                "facadeExceptionAop",
                Ordered.HIGHEST_PRECEDENCE,
                "applicationFacadePointcut",
                registry);
    }

    /**
     * 自定义 BeanFactory
     *
     * @param beanFactory
     * @return void
     * @author zhangyan
     * @date 2018-05-29
     **/
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

}
