package com.yg.api.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.platform.commons.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    @SafeVarargs
    public static <K, V> Map<K, V> createMap(Map.Entry<K, V>... entries) {
        Map<K, V> map = new HashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
