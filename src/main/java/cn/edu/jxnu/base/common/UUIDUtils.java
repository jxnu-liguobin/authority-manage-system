package cn.edu.jxnu.base.common;

import java.util.UUID;

/**
 * 生成唯一id工具 用于图书ID
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午4:56:04.
 * @version V1.0
 */
public class UUIDUtils {

	/**
	 * 静态工具方法
	 * 
	 * @time 2018年4月10日 下午4:56:23.</br>
	 * @version V1.0</br>
	 * @return</br>
	 */
	public static String makeID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
