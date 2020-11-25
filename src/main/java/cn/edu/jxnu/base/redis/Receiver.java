/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.redis;

import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * redis接收消息类
 *
 * @author 梦境迷离
 * @version V1.0
 */
@Slf4j
public class Receiver {

    /** java并发包的计数器 */
    private CountDownLatch latch;

    /**
     * 构造注入
     *
     * @param latch CountDownLatch
     */
    @Autowired
    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    /**
     * 接收者
     *
     * @param message message
     */
    public void receiveMessage(String message) {
        log.info("接收到消息： <" + message + "已经过期" + ">");
        latch.countDown();
    }
}
