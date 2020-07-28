package cn.edu.jxnu.base.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RedisTemplate
 * 
 * @author 梦境迷离
 * @version V1.0
 * @time 2018年4月10日
 */
public interface RedisService {

	/**
	 * 缓存map
	 * 
	 * @time 2018年4月10日 下午9:46:02
	 * 
	 * @version V1.0
	 * @param key
	 * @param map
	 * @return
	 * @throws Exception
	 */
	boolean putMap(String key, Map<Integer, List<String>> map) throws Exception;

	/**
	 * 查询哈希表 hKey 中给定域 hashKey 的值.
	 * 
	 * @time 2018年4月10日 下午9:46:34
	 * @version V1.0
	 * @param hKey
	 * @param hashKey
	 * @return
	 */
	List<String> hashGet(String hKey, Integer hashKey);

	/**
	 * 获取所有的KV散列值
	 * 
	 * @time 2018年4月10日 下午9:47:06
	 * @version V1.0
	 * @param key
	 * @return
	 * @throws Exception
	 */
	Map<Integer, List<String>> hashGetAll(String key) throws Exception;

	/**
	 * <p>
	 * 哈希表 hKey 中的域 hashKey 的值加上增量 delta 。 增量也可以为负数，相当于对给定域进行减法操作。 如果 key
	 * 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。 对一个储存字符串值的域
	 * field 执行 HINCRBY 命令将造成一个错误。
	 * </p>
	 * 
	 * @time 2018年4月10日 下午9:47:35
	 * @version V1.0
	 * @param hKey
	 * @param hashKey
	 * @param i
	 *            指定的增量
	 * @return 返回 hashKey的value增加之后 的值
	 */
	// Long hashIncrementLongOfHashMap(String hKey, Integer hashKey, Integer i);

	/**
	 * 添加键值对到哈希表key中
	 * 
	 * @time 2018年4月10日 下午9:50:04
	 * @version V1.0
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void hashPushHashMap(String key, Integer hashKey, List<String> value);

	/**
	 * 获取哈希表key中的所有域
	 * 
	 * @time 2018年4月10日 下午9:50:41
	 * @version V1.0
	 * @param key
	 * @return map的values set集合
	 */
	Set<Object> hashGetAllHashKey(String key);

	/**
	 * 删除一个或多个哈希字段
	 * 
	 * @time 2018年4月10日 下午9:52:15
	 * @version V1.0
	 * @param key
	 * @param hashKeys
	 * @return 返回删除成功的个数
	 */
	Long hashDeleteHashKey(String key, Object... hashKeys);

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	boolean del(String key);

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	String get(String key);

	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 *            单位秒
	 */
	void set(String key, String value, long liveTime);

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param value
	 */
	void set(String key, String value);

}
