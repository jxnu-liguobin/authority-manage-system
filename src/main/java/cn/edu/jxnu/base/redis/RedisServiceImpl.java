package cn.edu.jxnu.base.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 梦境迷离
 * @version V1.0
 * @time 2018年4月10日
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

	@Autowired
	private RedisTemplate<Object, ?> redisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 添加map
	 * 
	 * @time 下午10:10:22
	 * 
	 * @version V1.0
	 * @param key
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean putMap(String key, Map<Integer, List<String>> map) throws Exception {

		try {
			HashOperations<Object, Integer, List<String>> hashOps = redisTemplate.opsForHash();
			hashOps.putAll(key, map);
		} catch (Exception e) {
			log.error("缓存map失败key错误信息:{}", e.getMessage());
			throw new Exception("缓存map失败key[" + key + ",error[" + e.getMessage() + "]", e);
		}
		return true;
	}

	/**
	 * 查询哈希表 hKey 中给定域 hashKey 的值.
	 * 
	 * @tim 下午9:56:00
	 * 
	 * @version V1.0
	 * @param hKey
	 * @param hashKey
	 * @return 给定K的V
	 */
	@Override
	public List<String> hashGet(String hKey, Integer hashKey) {
		HashOperations<Object, Integer, List<String>> hashOps = redisTemplate.opsForHash();
		List<String> i = hashOps.get(hKey, hashKey);
		return i;
	}

	/**
	 * 获取所有的KV散列值
	 * 
	 * @time 下午9:47:06
	 * 
	 * @version V1.0
	 * @param key
	 * @return map集合
	 * @throws Exception
	 */
	@Override
	public Map<Integer, List<String>> hashGetAll(String key) throws Exception {
		Map<Integer, List<String>> map = null;
		try {
			HashOperations<Object, Integer, List<String>> hashOps = redisTemplate.opsForHash();
			map = hashOps.entries(key);
		} catch (Exception e) {
			log.error("获取map失败错误信息：{}", e.getMessage());
			throw new Exception("根据key[" + key + "获取map失败，,error[" + e.getMessage() + "]", e);
		}
		return map;
	}

	/**
	 * 添加键值对到哈希表key中
	 * 
	 * @time 下午9:50:04
	 * 
	 * @version V1.0
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	@Override
	public void hashPushHashMap(String key, Integer hashKey, List<String> value) {

		HashOperations<Object, Integer, List<String>> hashOps = redisTemplate.opsForHash();
		hashOps.put(key, hashKey, value);
	}

	/**
	 * 获取哈希表key中的所有域
	 *
	 * @time 下午9:50:41
	 * 
	 * @version V1.0
	 * @param key
	 * @return map的key set集合
	 */
	@Override
	public Set<Object> hashGetAllHashKey(String key) {
		return null;
	}

	/**
	 * 删除一个或多个哈希字段
	 * 
	 * @time 下午10:07:08
	 * 
	 * @version V1.0
	 * @param key
	 * @param hashKeys
	 * @return 成功删除的个数
	 */
	@Override
	public Long hashDeleteHashKey(String key, Object... hashKeys) {
		long result = 0l;
		HashOperations<Object, Integer, List<String>> hashOps = redisTemplate.opsForHash();
		if (hashOps.hasKey(key, hashKeys)) {
			result = hashOps.delete(key, hashKeys);
		}
		return result;
	}

	@Override
	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	@Override
	public void set(String key, String value, long liveTime) {
		stringRedisTemplate.opsForValue().set(key, value); // expired无法监听这个
		stringRedisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
	}

	@Override
	public void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	@Override
	public boolean del(String key) {
		try {
			stringRedisTemplate.delete(key);
		} catch (Exception e) {
			log.info("redis删除key失败");
		}
		return true;
	}

}