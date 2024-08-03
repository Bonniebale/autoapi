package com.yg.api.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ReflectUtil
 * @Description 反射Util
 * @Author Flora
 * @Date 2024/7/26 15:14
 */
public class ReflectUtil {
    private static final Map<Class<?>, Map<String, Field>> fieldCache = new ConcurrentHashMap<>();// 缓存字段反射操作

    /**
     * 根据 fieldName get该实例的字段值
     */
    private static Object getFieldValueFromClassHierarchy(Object instance, String fieldName) throws NoSuchFieldException {

        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true); // 设置字段可访问
                return field.get(instance); // 返回字段的值
            } catch (NoSuchFieldException | IllegalAccessException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("No such field: " + fieldName);
    }

    /**
     * 根据 fieldName get该实例的字段值
     */
    public static <T> Object getFieldValue(T instance, String fieldName) {
        return handleFieldOperation(instance, fieldName, FieldOperation.GET, null);
    }


    /**
     * 根据 fieldName set该实例的字段值
     */
    public static <T> void setFieldValue(T instance, String fieldName, Object value) {
        handleFieldOperation(instance, fieldName, FieldOperation.SET, value);
    }

    // 处理字段操作
    private static <T> Object handleFieldOperation(T instance, String fieldName, FieldOperation operation, Object value) {
        Field field = getFieldFromClassHierarchy(instance.getClass(), fieldName);
        field.setAccessible(true);

        try {
            return switch (operation) {
                case GET -> field.get(instance);
                case SET -> {
                    if (isCompatibleType(field, value)) {
                        field.set(instance, value);
                    }
                    yield null;
                }
                default -> throw new UnsupportedOperationException("Unsupported operation: " + operation);
            };

        } catch (IllegalAccessException e) {
            throw new RuntimeException("操作字段失败: " + e.getMessage(), e);
        }

    }


    // 枚举字段操作类型
    private enum FieldOperation {
        GET, SET
    }

    /**
     * 检查值的类型是否与字段的类型兼容
     */
    private static boolean isCompatibleType(Field field, Object value) {
        if (value == null) {
            return !field.getType().isPrimitive();
        }
        return field.getType().isAssignableFrom(value.getClass());
    }


    /**
     * 在当前类以及父类中查找字段
     */
    private static Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) {
        // 使用缓存机制和递归查找父类中的字段
        Map<String, Field> fields = fieldCache.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());

        return fields.computeIfAbsent(fieldName, k -> {
            Class<?> currentClass = clazz;
            while (currentClass != null) {
                try {
                    return currentClass.getDeclaredField(k);
                } catch (NoSuchFieldException e) {
                    currentClass = currentClass.getSuperclass();
                }
            }
            throw new RuntimeException(new NoSuchFieldException("No such field: " + k));
        });
    }

    /**
     * 设置 Builder 属性
     */
    public static void setBuilderProperty(Object builder, String fieldName, Object value) {
        try {
            Method setterMethod = findSetterMethod(builder.getClass(), fieldName);
            if (setterMethod != null) {
                setterMethod.setAccessible(true);
                Object convertedValue = DataUtil.convertValue(value, setterMethod.getParameterTypes()[0]);
                setterMethod.invoke(builder, convertedValue);
            } else {
                throw new RuntimeException("找不到该字段的setter方法: " + fieldName);
            }
        } catch (Exception e) {
            throw new RuntimeException("build属性失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查找setter方法
     *
     * @param clazz 类
     * @param methodName 需要查找的方法名
     */
    public static Method findSetterMethod(Class<?> clazz, String methodName) {

        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Method method = Arrays.stream(currentClass.getDeclaredMethods())
                    .filter(m -> m.getName().equals(methodName) && m.getParameterCount() == 1)
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


}
