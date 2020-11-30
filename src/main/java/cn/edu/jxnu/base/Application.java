/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * 主启动类
 *
 * @author 梦境迷离.
 * @version V2.0 2020年11月20日
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@Slf4j
@EnableWebFlux
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    log.debug("启动成功");
  }
}
