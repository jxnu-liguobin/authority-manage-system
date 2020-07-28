package cn.edu.jxnu.base.common;

import java.io.Serializable;

import lombok.Data;

/**
 * Json 统一返回消息类
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午4:51:45.
 * @version V1.0
 */
@Data
public class JsonResult implements Serializable {
	private static final long serialVersionUID = -1491499610244557029L;
	/**
	 * 成功代码
	 */
	public static int CODE_SUCCESS = 0;
	/**
	 * 失败代码
	 */
	public static int CODE_FAILURED = -1;
	/**
	 * 携带数据
	 */
	public static String[] NOOP = new String[] {};

	/**
	 * 处理状态：0: 成功
	 */
	private int code;
	/**
	 * 返回消息
	 */
	private String message;
	/**
	 * 返回数据
	 */
	private Object data;

	/**
	 * 默认构造
	 * 
	 * @time 2018年4月10日 下午4:54:20.</br>
	 * @version V1.0</br>
	 * @param code
	 * @param message
	 * @param data</br>
	 */
	private JsonResult(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * 处理成功，并返回数据
	 * 
	 * @param data
	 *            数据对象
	 * @return data
	 */
	public static final JsonResult success(Object data) {
		return new JsonResult(CODE_SUCCESS, "操作成功", data);
	}

	/**
	 * 处理成功
	 * 
	 * @param message
	 *            消息
	 * @return data
	 */
	public static final JsonResult success() {
		return new JsonResult(CODE_SUCCESS, "操作成功", NOOP);
	}

	/**
	 * 处理成功
	 * 
	 * @param message
	 *            消息
	 * @return data
	 */
	public static final JsonResult success(String message) {
		return new JsonResult(CODE_SUCCESS, message, NOOP);
	}

	/**
	 * 处理成功
	 * 
	 * @param message
	 *            消息
	 * @param data
	 *            数据对象
	 * @return data
	 */
	public static final JsonResult success(String message, Object data) {
		return new JsonResult(CODE_SUCCESS, message, data);
	}

	/**
	 * 处理失败，并返回数据（一般为错误信息）
	 * 
	 * @param code
	 *            错误代码
	 * @param message
	 *            消息
	 * @return data
	 */
	public static final JsonResult failure(int code, String message) {
		return new JsonResult(code, message, NOOP);
	}

	/**
	 * 处理失败
	 * 
	 * @param message
	 *            消息
	 * @return data
	 */
	public static final JsonResult failure(String message) {
		return failure(CODE_FAILURED, message);
	}

	/**
	 * 测试用
	 * 
	 * @time 2018年4月10日 下午4:55:06.</br>
	 * @version V1.0</br>
	 * @return String</br>
	 */
	@Override
	public String toString() {
		return "JsonResult [code=" + code + ", message=" + message + ", data=" + data + "]";
	}

}
