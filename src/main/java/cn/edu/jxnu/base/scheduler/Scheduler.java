package cn.edu.jxnu.base.scheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.edu.jxnu.base.config.shiro.RetryLimitHashedCredentialsMatcher;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务 改
 * 
 * @author 梦境迷离.
 * @time 2018年6月11日
 * @version V1.0
 *
 */
@Component
@Slf4j
public class Scheduler {

	@Autowired
	private RetryLimitHashedCredentialsMatcher credentialsMatcher;
	// public static volatile ConcurrentLinkedDeque<String> list = new
	// ConcurrentLinkedDeque<String>();
	public static volatile ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
	public static ReentrantLock lock = new ReentrantLock();

	/**
	 * ehcache的缓存过期无效，使用定时任务替代。
	 * 
	 * @author 梦境迷离
	 * @time 下午2:05:52
	 * @version V1.0
	 *
	 */
	@Scheduled(fixedRate = 60000) // 完成之后的每分钟执行一次,即存在误差1分钟，可以控制fixedRate
	public void statusCheck() {
		try {
			lock.tryLock(10, TimeUnit.SECONDS);
			log.info("每分钟执行一次。开始……");
			// 添加到阻塞双端队列
			// string = list.pollFirst();
			map.forEach((x, y) -> {
				if (credentialsMatcher.getPasswordRetryCache().get(x) != null) {
					long temp = System.currentTimeMillis() - y;
					if (temp >= 1000 * 60 * 10) {
						log.info("正在清除用户：{}的记录,锁定时间：{}", x, temp);
						map.remove(x);
						credentialsMatcher.getPasswordRetryCache().remove(x);
					}
				}
			});
			// if (string != null) {
			// if (credentialsMatcher.getPasswordRetryCache().get(string) !=
			// null) {
			// log.info("正在取出阻塞队列和缓存中的用户名");
			// credentialsMatcher.getPasswordRetryCache().remove(string);
			// }
			// }
		} catch (Exception e) {
			log.error("定时任务出错：{}", e.getMessage());
		} finally {
			lock.unlock();
		}
		log.info("每分钟执行一次。结束……");
	}

	/**
	 * 防止内存溢出
	 * 
	 * @author 梦境迷离
	 * @time 下午1:50:22
	 * @version V1.0
	 *
	 */
	@Scheduled(fixedDelay = 36000000) // 完成后的每600分钟执行一次,清空
	public void checkSize() {
		log.info("开始……");
		int size = map.size();
		if (size > 1000) {
			log.info("大于1000，清空");
			map.clear();
		}
		log.info("结束……");
	}
}