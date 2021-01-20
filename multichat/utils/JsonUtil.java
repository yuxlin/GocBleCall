package cn.kaer.multichat.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author wanghx
 * @date 2019/10/8
 * @description
 */
public class JsonUtil {
    public static <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text,clazz);
    }

    public static String toString(Object info) {
        return JSON.toJSONString(info);
    }
}
