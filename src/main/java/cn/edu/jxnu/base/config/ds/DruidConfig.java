package cn.edu.jxnu.base.config.ds;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;

/**
 * druid 配置
 * 
 * 属性太多，偷懒没实现动态
 * 
 * 提示：
 * 1、继承AbstractRoutingDataSource路由
 * 2、ThreadLocal存数据源
 * 3、构建数据源并初始化
 * 4、AOP拦截注解+注解标记方法
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午4:59:48.
 * @version V1.0
 */
@Configuration
public class DruidConfig {
	/**
	 * 也可以通过继承StatViewServlet 使用@WebFilter实现
	 * 
	 * @time 2018年4月10日 下午4:59:57.
	 * @version V1.0
	 * @return ServletRegistrationBean
	 */
	@Bean
	public ServletRegistrationBean statViewServle() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		// 白名单：1
		servletRegistrationBean.addInitParameter("allow", "192.168.1.1,127.0.0.1");
		// IP黑名单 (存在共同时，deny优先于allow)
		// servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
		// 登录查看信息的账号密码.
		// servletRegistrationBean.addInitParameter("loginUsername", "druid");
		// servletRegistrationBean.addInitParameter("loginPassword",
		// "druid123");
		// 是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	/**
	 * 不拦截的
	 * 
	 * @time 2018年4月10日 下午5:00:08.
	 * @version V1.0
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean statFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.my,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

	/**
	 * 配置Spring监控
	 * 
	 * @time 2018年4月10日 下午5:00:18.
	 * @version V1.0
	 * @return DruidStatInterceptor
	 */
	@Bean(name = "druid-stat-interceptor") // 取名字是为了下面使用
	public DruidStatInterceptor getDruidStatInterceptor() {
		DruidStatInterceptor druidStatInterceptor = new DruidStatInterceptor();
		return druidStatInterceptor;

	}

	/**
	 * 必须使用Qualifier 否则注入失败
	 * 
	 * @time 2018年4月10日 下午5:00:31.
	 * @version V1.0
	 * @return BeanNameAutoProxyCreator
	 */
	@Bean
	@Qualifier("druid-stat-interceptor") // 注入上面的bean
	public BeanNameAutoProxyCreator getBeanNameAutoProxyCreator() {
		BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
		beanNameAutoProxyCreator.setInterceptorNames("druid-stat-interceptor"); // 关联
		beanNameAutoProxyCreator.setBeanNames("*Service", "*Dao");
		beanNameAutoProxyCreator.setProxyTargetClass(true);
		return beanNameAutoProxyCreator;
	}

}