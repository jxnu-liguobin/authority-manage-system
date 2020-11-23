package cn.edu.jxnu.base.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Configuration
@Import(ShiroManager.class)
@Slf4j
public class ShiroConfig {

    /**
     * 注入MyRealm
     *
     * @return Realm
     */
    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    public Realm realm(CacheManager ehCacheManager) {
        MyRealm myRealm = new MyRealm();
        myRealm.setCacheManager(ehCacheManager);// 密码凭证
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher(ehCacheManager));
        return myRealm;
    }

    /**
     * 密码验证
     *
     * @return 自定义密码验证
     */
    @Bean("credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher(CacheManager CacheManager) {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(CacheManager);
        return credentialsMatcher;
    }

    /**
     * 用户授权信息Cache
     *
     * @return CacheManager
     */
    @Bean(name = "shiroCacheManager")
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 缓存用户密码
     *
     * @return ehCache缓存管理器
     *
     */
    // @Bean(name = "ehCacheManager")
    // public EhCacheManager EHcacheManager() {
    // EhCacheManager eh = new EhCacheManager();
    // eh.setCacheManagerConfigFile("classpath:ehcache.xml");
    // return eh;
    // }

    /**
     * 授权管理器
     *
     * @return DefaultSecurityManager
     */
    @Bean(name = "securityManager")
    @ConditionalOnMissingBean
    public SessionsSecurityManager securityManager() {
        DefaultSecurityManager sm = new DefaultWebSecurityManager();
        ThreadContext.bind(sm);
        sm.setCacheManager(cacheManager());
        sm.setRememberMeManager(rememberMeManager());// 注入记住我
        return sm;
    }

    /**
     * shiro拦截器
     *
     * @param securityManager
     * @param realm
     * @return ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
    @DependsOn("securityManager")
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SessionsSecurityManager securityManager, Realm realm) {
        securityManager.setRealm(realm);
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/admin/login");
        shiroFilter.setSuccessUrl("/admin/index");
        shiroFilter.setUnauthorizedUrl("/assets/401.html");
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put("/assets/**", "anon");
        filterChainDefinitionMap.put("/admin/regist", "anon");// 添加
        filterChainDefinitionMap.put("/admin/login", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/isTrue", "anon"); // 验证码异步验证

        // 个人信息
        filterChainDefinitionMap.put("/admin/info/**", "anon");
        // 自主还书
        filterChainDefinitionMap.put("/admin/borrow/**", "anon");
        filterChainDefinitionMap.put("/admin/user/index", "perms[system:user:index]");
        filterChainDefinitionMap.put("/admin/user/add", "perms[system:user:add]");
        filterChainDefinitionMap.put("/admin/user/edit*", "perms[system:user:edit]");
        filterChainDefinitionMap.put("/admin/user/deleteBatch", "perms[system:user:deleteBatch]");
        filterChainDefinitionMap.put("/admin/user/grant/**", "perms[system:user:grant]");
        filterChainDefinitionMap.put("/admin/user/resume/**", "perms[system:user:resume]");
        // 注册账号验证和添加账号验证
        filterChainDefinitionMap.put("/admin/user/isExist/**", "anon");
        filterChainDefinitionMap.put("/admin/user/isAvailable/**", "anon");
        filterChainDefinitionMap.put("/admin/user/isAllTrue/**", "anon");

        filterChainDefinitionMap.put("/admin/role/index", "perms[system:role:index]");
        filterChainDefinitionMap.put("/admin/role/add", "perms[system:role:add]");
        filterChainDefinitionMap.put("/admin/role/edit*", "perms[system:role:edit]");
        filterChainDefinitionMap.put("/admin/role/deleteBatch", "perms[system:role:deleteBatch]");
        filterChainDefinitionMap.put("/admin/role/grant/**", "perms[system:role:grant]");

        filterChainDefinitionMap.put("/admin/resource/index", "perms[system:resource:index]");
        filterChainDefinitionMap.put("/admin/resource/add", "perms[system:resource:add]");
        filterChainDefinitionMap.put("/admin/resource/edit*", "perms[system:resource:edit]");
        filterChainDefinitionMap.put("/admin/resource/deleteBatch", "perms[system:resource:deleteBatch]");

        filterChainDefinitionMap.put("/druid/", "perms[system:resource:druid]");// druid
        filterChainDefinitionMap.put("/admin/memorandum/*", "perms[system:memorandum:memorandum]");// 系统记录,只使用一个拦截url
        // 添加过滤条件
        filterChainDefinitionMap.put("/admin/books/book_management", "perms[system:books:book_management]");

        filterChainDefinitionMap.put("/admin/**", "user"); // 默认所有均可依靠cookie,本项目隐藏bug，cookie太大，无法保存在浏览器本地
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilter;
    }

    /**
     * 产生cookie
     */
    @Bean
    @ConditionalOnMissingBean
    public SimpleCookie rememberMeCookie() {
        log.info("ShiroConfiguration.rememberMeCookie()");
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // <!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        simpleCookie.setComment("my cookie comment");
        return simpleCookie;
    }

    /**
     * cookie管理对象
     */
    @Bean
    @ConditionalOnMissingBean
    public CookieRememberMeManager rememberMeManager() {
        log.info("method invoke: ShiroConfiguration.rememberMeManager");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

}
