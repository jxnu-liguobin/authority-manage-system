/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RedisTemplate
 *
 * @author 梦境迷离
 * @version V1.0
 */
public interface RedisService {

  /**
   * 缓存map
   *
   * @param key key
   * @param map map
   * @return 是否成功
   * @throws Exception 异常
   */
  boolean putMap(String key, Map<Integer, List<String>> map) throws Exception;

  /**
   * 查询哈希表 hKey 中给定域 hashKey 的值.
   *
   * @param hKey hKey
   * @param hashKey hashKey
   * @return list
   */
  List<String> hashGet(String hKey, Integer hashKey);

  /**
   * 获取所有的KV散列值
   *
   * @param key key
   * @return map
   * @throws Exception 异常
   */
  Map<Integer, List<String>> hashGetAll(String key) throws Exception;

  /**
   * 哈希表 hKey 中的域 hashKey 的值加上增量 delta 。 增量也可以为负数，相当于对给定域进行减法操作。 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY
   * 命令。 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
   *
   * @param hKey key
   * @param hashKey hashKey
   * @param i 指定的增量
   * @return 返回 hashKey的value增加之后 的值
   */
  // Long hashIncrementLongOfHashMap(String hKey, Integer hashKey, Integer i);

  /**
   * 添加键值对到哈希表key中
   *
   * @param key key
   * @param hashKey hashKey
   * @param value value
   */
  void hashPushHashMap(String key, Integer hashKey, List<String> value);

  /**
   * 获取哈希表key中的所有域
   *
   * @param key key
   * @return map的values set集合
   */
  Set<Object> hashGetAllHashKey(String key);

  /**
   * 删除一个或多个哈希字段
   *
   * @param key key
   * @param hashKeys hashKeys
   * @return 返回删除成功的个数
   */
  Long hashDeleteHashKey(String key, Object... hashKeys);

  /**
   * 通过key删除
   *
   * @param key key
   * @return 是否删除成功
   */
  boolean del(String key);

  /**
   * 获取redis value (String)
   *
   * @param key key
   * @return String
   */
  String get(String key);

  /**
   * 添加key value 并且设置存活时间
   *
   * @param key key
   * @param value value
   * @param liveTime 单位秒
   */
  void set(String key, String value, long liveTime);

  /**
   * 添加key value
   *
   * @param key key
   * @param value value
   */
  void set(String key, String value);
}
