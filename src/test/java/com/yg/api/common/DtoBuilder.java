package com.yg.api.common;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.platform.commons.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName DtoBuilder
 * @Description TODO
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
                if (isValidValue(value)) {
                    setBuilderProperty(builder, key, value);
                }
            });

            // 调用 build 方法构建对象
            Method buildMethod = builder.getClass().getMethod("build");
            buildMethod.setAccessible(true);
            return (T) buildMethod.invoke(builder);

        } catch (Exception e) {
            throw new RuntimeException("实例化失败: " + e.getMessage(), e);
        }
    }

    // 判断值是否有效
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

    // set
    public static void setBuilderProperty(Object builder, String fieldName, Object value) {
        try {
            Method setterMethod = findSetterMethod(builder.getClass(), fieldName);
            if (setterMethod != null) {
                setterMethod.setAccessible(true);
                Object convertedValue = convertValue(value, setterMethod.getParameterTypes()[0]);
                setterMethod.invoke(builder, convertedValue);
            } else {
                throw new RuntimeException("No setter method found for field: " + fieldName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set property on builder: " + e.getMessage(), e);
        }
    }

    // 查找setter方法
    public static Method findSetterMethod(Class<?> builderClass, String fieldName) {
        System.out.println("setterName:"+fieldName);
        Class<?> currentClass = builderClass;

        while (currentClass != null) {
            Method method = Arrays.stream(currentClass.getDeclaredMethods())
                    .filter(m -> m.getName().equals(fieldName) && m.getParameterCount() == 1)
                    .findFirst()
                    .orElse(null);
            if (method != null) {
                method.setAccessible(true);
                return method;
            }
            currentClass = currentClass.getSuperclass(); // 继续在父类中查找
        }

        return null;
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

    private static Object convertValue(Object value, Class<?> targetType) {
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
