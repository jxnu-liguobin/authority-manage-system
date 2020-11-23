package cn.edu.jxnu.base.utils;

import java.util.UUID;

/**
 * 生成唯一id工具 用于图书ID
 *
 * @author 梦境迷离
 * @version V2.0 2020年11月20日
 */
public class UUIDUtils {

    /**
     * 静态工具方法
     *
     * @return String
     */
    public static String makeID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
