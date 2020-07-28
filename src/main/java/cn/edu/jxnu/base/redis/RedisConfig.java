package cn.edu.jxnu.base.redis;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * redis配置类 注意：不要配置redis缓存
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午8:27:46
 * @version V1.0
 */
@Configuration
@EnableAutoConfiguration
@Slf4j
public class RedisConfig {

	public final static String LISTENER_PATTERN = "__keyevent@0__:expired";

	/**
	 * 
	 * @author 梦境迷离
	 * @time 上午11:39:01
	 * @version V1.0
	 * @return Jedis 连接工厂
	 *
	 */
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	/**
	 * <p>
	 * SpringBoot提供了对Redis的自动配置功能，
	 * 在RedisAutoConfiguration中默认为我们配置了JedisConnectionFactory（客户端连接）、
	 * RedisTemplate以及StringRedisTemplate（数据操作模板），
	 * 其中StringRedisTemplate模板只针对键值对都是字符型的数据进行操作，本示例采用RedisTemplate作为数据操作模板，
	 * 该模板默认采用JdkSerializationRedisSerializer的二进制数据序列化方式，
	 * 为了方便演示本示例采用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值，
	 * 使用StringRedisSerializer来序列化和反序列化redis的key值。
	 * 
	 * @time 2018年4月10日 下午8:43:32
	 *       </p>
	 * 
	 * @version V1.0
	 * @param factory
	 * @return RedisTemplate
	 */
	@Bean
	RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		/**
		 * 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值，但是不能进行value的增加，需要改为StringRedisSerializer
		 */
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(mapper);
		template.setKeySerializer(serializer);
		template.setValueSerializer(new StringRedisSerializer());
		/**
		 * 使用StringRedisSerializer来序列化和反序列化redis的key值
		 */
		template.afterPropertiesSet();
		log.info("redisTemplate:" + template.toString());
		return template;

	}

	/*
	 * Redis消息监听器容器 这个容器加载了RedisConnectionFactory和消息监听器
	 */
	@Bean
	RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory());
		container.addMessageListener(listenerAdapter, new PatternTopic(LISTENER_PATTERN));
		return container;
	}

	/*
	 * 将Receiver注册为一个消息监听器，并指定消息接收的方法（receiveMessage）
	 * 如果不指定消息接收的方法，消息监听器会默认的寻找Receiver中的handleMessage这个方法作为消息接收的方法
	 */
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	/*
	 * Receiver实例
	 */
	@Bean
	Receiver receiver(CountDownLatch latch) {
		return new Receiver(latch);
	}

	/*
	 * 一个，即模拟锁
	 */
	@Bean
	CountDownLatch latch() {
		return new CountDownLatch(1);
	}

}