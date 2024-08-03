package com.yg.api.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.platform.commons.util.StringUtils;

import java.util.*;

/**
 * @ClassName DataUtil
 * @Description TODO
 * @Author flora
 * @Date 2024/7/25 16:06
 */
public class DataUtil {

    /**
     * 判断值是否不为null且不为空
     */
    public static boolean isValidValue(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof String val) {
            return StringUtils.isNotBlank(val);
        }
        if (value instanceof Collection<?> collection) {
            return CollectionUtils.isNotEmpty(collection);
        }
        return true;
    }

    /**
     * 转换值
     *
     * @param value 值
     * @param targetType 目标类型
     */
    public static Object convertValue(Object value, Class<?> targetType) {
        if (targetType.isInstance(value)) {
            return value;
        }
        if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(value.toString());
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(value.toString());
        }
        if (targetType == List.class && value instanceof String) {
            return Arrays.asList(((String)value).split(","));
        }
        throw new IllegalArgumentException("Unsupported target type: " + targetType);
    }

    @SafeVarargs
    public static <K, V> Map<K, V> createMap(Map.Entry<K, V>... entries) {
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
