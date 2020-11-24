/* 梦境迷离 (C)2020 */
package cn.edu.jxnu.base.utils;

import java.io.Serializable;
import lombok.Data;

/**
 * Json 统一返回消息类
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
@Data
public class JsonResult implements Serializable {

    private static final long serialVersionUID = -1491499610244557029L;
    /** 成功代码 */
    public static int CODE_SUCCESS = 0;
    /** 失败代码 */
    public static int CODE_FAILURED = -1;
    /** 携带数据 */
    public static String[] NOOP = new String[] {};

    /** 处理状态：0: 成功 */
    private int code;
    /** 返回消息 */
    private String message;
    /** 返回数据 */
    private Object data;

    /**
     * 默认构造
     *
     * @param code
     * @param message
     * @param data
     */
    private JsonResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 处理成功，并返回数据
     *
     * @param data 数据对象
     * @return data
     */
    public static JsonResult success(Object data) {
        return new JsonResult(CODE_SUCCESS, "操作成功", data);
    }

    /**
     * 处理成功
     *
     * @return data
     */
    public static JsonResult success() {
        return new JsonResult(CODE_SUCCESS, "操作成功", NOOP);
    }

    /**
     * 处理成功
     *
     * @param message 消息
     * @return data
     */
    public static JsonResult success(String message) {
        return new JsonResult(CODE_SUCCESS, message, NOOP);
    }

    /**
     * 处理成功
     *
     * @param message 消息
     * @param data 数据对象
     * @return data
     */
    public static JsonResult success(String message, Object data) {
        return new JsonResult(CODE_SUCCESS, message, data);
    }

    /**
     * 处理失败，并返回数据（一般为错误信息）
     *
     * @param code 错误代码
     * @param message 消息
     * @return data
     */
    private static JsonResult failure(int code, String message) {
        return new JsonResult(code, message, NOOP);
    }

    /**
     * 处理失败
     *
     * @param message 消息
     * @return data
     */
    public static JsonResult failure(String message) {
        return failure(CODE_FAILURED, message);
    }

    /**
     * 测试用
     *
     * @return String
     */
    @Override
    public String toString() {
        return "JsonResult [code=" + code + ", message=" + message + ", data=" + data + "]";
    }
}
