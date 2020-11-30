/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.shiro;

import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

/**
 * Shiro管理器配置
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public class ShiroManager {

  /**
   * 保证实现了Shiro内部lifecycle函数的bean执行
   *
   * @return LifecycleBeanPostProcessor
   */
  @Bean(name = "lifecycleBeanPostProcessor")
  @ConditionalOnMissingBean
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  /** @return DefaultAdvisorAutoProxyCreator */
  @Bean(name = "defaultAdvisorAutoProxyCreator")
  @ConditionalOnMissingBean
  @DependsOn("lifecycleBeanPostProcessor")
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator =
        new DefaultAdvisorAutoProxyCreator();
    defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    return defaultAdvisorAutoProxyCreator;
  }

  /**
   * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持; Controller才能使用
   *
   * @param securityManager securityManager
   * @return AuthorizationAttributeSourceAdvisor
   */
  @Bean
  @ConditionalOnMissingBean
  public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
      SessionsSecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
    aasa.setSecurityManager(securityManager);
    return new AuthorizationAttributeSourceAdvisor();
  }
}
