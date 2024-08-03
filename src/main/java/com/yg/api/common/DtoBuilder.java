package com.yg.api.common;

import com.yg.api.common.utils.DataUtil;
import com.yg.api.common.utils.ReflectUtil;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName DtoBuilder
 * @Description 将参数进行实例化
 * @Author flora
 * @Date 2024/7/20 13:22
 */
public class DtoBuilder {

    @SuppressWarnings("unchecked")
    public static <T> T buildDtoFromMap(Class<T> clazz, Map<String, Object> paramMap) {
        try {
            // 获取 Builder 实例
            Object builder = clazz.getMethod("builder").invoke(null);

            // 遍历参数映射，设置对应的 Builder 属性
            paramMap.forEach((key, value) -> {
                if (DataUtil.isValidValue(value)) {
                    setFieldProperty(builder, key, value);
                }
            });

            // 调用 build 方法构建对象
            return (T)ReflectUtil.invokeMethod(builder, "build", null);

        } catch (Exception e) {
            throw new RuntimeException("实例化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 设置field属性
     */
    public static <T> void setFieldProperty(T instance, String name, T value) {
        Method method = ReflectUtil.getMethod(instance.getClass(), name);
        Object convertedValue = DataUtil.convertValue(value, method.getParameterTypes()[0]);
        ReflectUtil.invokeMethod(instance, method, convertedValue);
    }


    private static Optional<Method> findMethod(Method[] methods, String setterName, Class<?> valueType) {
        for (Method method : methods) {
            if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                if (paramType.isAssignableFrom(valueType) || (paramType.isPrimitive() && wrapperType(paramType).equals(valueType))) {
                    return Optional.of(method);
                }
                if (Collection.class.isAssignableFrom(paramType) && Collection.class.isAssignableFrom(valueType)) {
                    return Optional.of(method);
                }
            }
        }
        return Optional.empty();
    }

    private static Class<?> wrapperType(Class<?> primitiveType) {
        if (primitiveType == int.class) return Integer.class;
        if (primitiveType == long.class) return Long.class;
        if (primitiveType == double.class) return Double.class;
        if (primitiveType == float.class) return Float.class;
        if (primitiveType == char.class) return Character.class;
        if (primitiveType == boolean.class) return Boolean.class;
        if (primitiveType == short.class) return Short.class;
        if (primitiveType == byte.class) return Byte.class;
        throw new IllegalArgumentException("No corresponding wrapper type for primitive: " + primitiveType);
    }


}
