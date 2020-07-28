//package cn.edu.jxnu.base;
//
//import org.springframework.boot.bind.RelaxedPropertyResolver;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//
///**
// * 获得环境变量
// * 
// * @author 梦境迷离
// * @version V1.0
// *
// */
//@Configuration
//public class MyEnvironmentAware implements EnvironmentAware {
//
//	/**
//	 * 设置环境的时候，获取属性
//	 */
//	@Override
//	public void setEnvironment(Environment env) {
//		// 通过 environment 获取到系统属性.
//		System.out.println(env.getProperty("JAVA_HOME"));
//		// 通过 environment 同样能获取到application.properties配置的属性.
//		System.out.println(env.getProperty("spring.datasource.master.url"));
//		System.out.println(env.getProperty("spring.datasource.cluster.url"));
//		// 获取到前缀是"spring.datasource." 的属性列表值.
//		RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
//		System.out.println("spring.datasource.master.driver-class-name="
//				+ relaxedPropertyResolver.getProperty("master.driver-class-name"));
//	}
//
//}