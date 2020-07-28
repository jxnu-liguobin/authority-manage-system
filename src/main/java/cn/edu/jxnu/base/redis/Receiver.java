package cn.edu.jxnu.base.redis;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * redis接收消息类
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午8:27:46
 * @version V1.0
 */
@Slf4j
public class Receiver {

	/**
	 * java并发包的计数器
	 */
	private CountDownLatch latch;

	/**
	 * 构造注入
	 */
	@Autowired
	public Receiver(CountDownLatch latch) {
		this.latch = latch;
	}

	/**
	 * 接收者
	 */
	public void receiveMessage(String message) {
		log.info("接收到消息： <" + message + "已经过期" + ">");
		latch.countDown();
	}
}