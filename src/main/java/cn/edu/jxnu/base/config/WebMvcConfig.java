package cn.edu.jxnu.base.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

import cn.edu.jxnu.base.config.intercepter.CommonIntercepter;

/**
 * SpringBoot自定义的web配置
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午4:57:30.
 * @version V1.0
 */
@SuppressWarnings("deprecation")
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	@Autowired
	private CommonIntercepter commonIntercepter;

	/**
	 * fastJson相关设置
	 * 
	 * @time 2018年4月10日 下午4:57:40.
	 * 
	 * @version V1.0
	 * @return FastJsonConfig
	 */
	private FastJsonConfig getFastJsonConfig() {

		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		List<SerializerFeature> serializerFeatureList = new ArrayList<SerializerFeature>();
		serializerFeatureList.add(SerializerFeature.PrettyFormat);
		serializerFeatureList.add(SerializerFeature.WriteMapNullValue);
		serializerFeatureList.add(SerializerFeature.WriteNullStringAsEmpty);
		serializerFeatureList.add(SerializerFeature.WriteNullListAsEmpty);
		serializerFeatureList.add(SerializerFeature.DisableCircularReferenceDetect);
		SerializerFeature[] serializerFeatures = serializerFeatureList
				.toArray(new SerializerFeature[serializerFeatureList.size()]);
		fastJsonConfig.setSerializerFeatures(serializerFeatures);

		return fastJsonConfig;
	}

	/**
	 * fastJson相关设置
	 * 
	 * @time 2018年4月10日 下午4:57:52.
	 * 
	 * @version V1.0
	 * @return FastJsonHttpMessageConverter4
	 */
	private FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter() {

		FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter4();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.parseMediaType("text/html;charset=UTF-8"));
		supportedMediaTypes.add(MediaType.parseMediaType("application/json"));
		fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
		fastJsonHttpMessageConverter.setFastJsonConfig(getFastJsonConfig());

		return fastJsonHttpMessageConverter;
	}

	/**
	 * 添加fastJsonHttpMessageConverter到converters
	 * 
	 * @time 2018年4月10日 下午4:58:02.
	 * 
	 * @version V1.0
	 * @param converters
	 *            消息转化器
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(fastJsonHttpMessageConverter());
	}

	/**
	 * 添加拦截器
	 * 
	 * @time 2018年4月10日 下午4:58:10.
	 * 
	 * @version V1.0
	 * @param registry
	 *            拦截器注册
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(commonIntercepter).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	/**
	 * Spring
	 * 提供了FilterRegistrationBean类，此类提供setOrder方法，可以为filter设置排序值，让spring在注册web
	 * filter之前排序后再依次注册。
	 * 
	 * @time 2018年4月10日 下午4:58:19.
	 * 
	 * @version V1.0
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean registFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new OpenEntityManagerInViewFilter());
		registration.addUrlPatterns("/*");
		registration.setOrder(1);
		return registration;
	}

	/**
	 * 域名直接访问主页
	 */
	public static String LOGIN_USER = "loginUser";

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/admin/login");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}

}
