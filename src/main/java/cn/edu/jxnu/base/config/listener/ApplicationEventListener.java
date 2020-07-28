package cn.edu.jxnu.base.config.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * 应用监听器
 * 
 * @author 梦境迷离 
 * @time 2018年4月10日 下午5:05:24
 * @version V1.0
 */
@Slf4j
public class ApplicationEventListener implements ApplicationListener<ApplicationEvent> {

	/**
	 * 应用程序启动过程监听
	 * 
	 * @time 2018年4月10日 下午5:05:33
	 * @version V1.0
	 * @param event
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// 在这里可以监听到Spring Boot的生命周期
		if (event instanceof ApplicationEnvironmentPreparedEvent) { // 初始化环境变量
			log.debug("初始化环境变量");
		} else if (event instanceof ApplicationPreparedEvent) { // 初始化完成
			log.debug("初始化环境变量完成");
		} else if (event instanceof ContextRefreshedEvent) { // 应用刷新，当ApplicationContext初始化或者刷新时触发该事件。
			log.debug("应用刷新");
		} else if (event instanceof ApplicationReadyEvent) {// 应用已启动完成
			log.debug("应用已启动完成");
		} else if (event instanceof ContextStartedEvent) { // 应用启动，Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的
															// Start()方法开始/重新开始容器时触发该事件。
			log.debug("应用启动");
		} else if (event instanceof ContextStoppedEvent) { // 应用停止，Spring2.5新增的事件，当容器调用ConfigurableApplicationContext
															// 的Stop()方法停止容器时触发该事件。
			log.debug("应用停止");
		} else if (event instanceof ContextClosedEvent) { // 应用关闭，当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有
															// 单例Bean都被销毁。
			log.debug("应用关闭");
		} else {
		}
	}

}