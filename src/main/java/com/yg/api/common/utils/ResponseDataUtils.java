package com.yg.api.common.utils;

import io.restassured.path.json.JsonPath;
import org.junit.platform.commons.util.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * @ClassName ResponseDataUtils
 * @Description TODO
 * @Author flora
 * @Date 2024/7/25 14:06
 */
public class ResponseDataUtils {

    // 自定义异常类
    public static class DataValidationException extends RuntimeException {
        public DataValidationException(String message) {
            super(message);
        }
    }

    public static JsonPath checkQueryResponseData(JsonPath response) {
        return response;
    }

    public static Object checkResponseData(JsonPath response, String path, String errorMessage) {

        if (StringUtils.isBlank(path)) {
            return response;
        }

        var data = response.get(path);

        if (!DataUtil.isValidValue(data)) {
            throw new DataValidationException(Optional.ofNullable(errorMessage)
                    .orElse("该路径 '" + path + "' 下的数据为空."));
        }

        return data;
    }

    public static Object checkResponseData(Map<String, Object> response, String key, String returnPath, String errorMessage) {
        // 获取嵌套值
        Object data = getNestedValue(response, key);

        // 检查 data 是否为空
        if (data == null || (data instanceof String && ((String)data).isEmpty())) {
            throw new IllegalArgumentException(errorMessage != null ? errorMessage : "Empty data error");
        }

        // 根据 returnPath 返回不同部分的数据
        if (returnPath != null && !returnPath.isEmpty()) {
            return getNestedValue(response, returnPath);
        }

        return response;
    }

    public static Object getNestedValue(Map<String, Object> dictionary, String paths) {
        String[] keys = paths.split("\\.");
        Object current = dictionary;

        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<?, ?>)current).get(key);
            } else {
                return null;
            }
        }

        return current;
    }
}
