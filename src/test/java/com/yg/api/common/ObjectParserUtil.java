package com.yg.api.common;

import java.lang.reflect.Field;

/**
 * @ClassName ObjectParserUtil
 * @Description TODO
 * @Author flora
 * @Date 2024/7/18 22:06
 */
public class ObjectParserUtil {
    public static <T> T parse(Class<T> clazz, String... keyValuePairs) {
        try {
            T object = clazz.getDeclaredConstructor().newInstance();

            for (String pair : keyValuePairs) {
                String[] parts = pair.split("="); // 拆分键值对
                if (parts.length == 2) {
                    String fieldName = parts[0].trim();
                    String value = parts[1].trim();

                    // 使用反射查找并设置字段值
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        field.set(object, Integer.parseInt(value));
                    } else if (field.getType() == String.class) {
                        field.set(object, value);
                    } // 可以根据需要添加更多类型的支持

                    field.setAccessible(false);
                }
            }

            return object;
        } catch (Exception e) {
            e.printStackTrace(); // 实际应用中应该处理异常
            return null;
        }
    }
}
