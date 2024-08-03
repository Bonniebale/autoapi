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
    public static <T> void setFieldValue(T instance, String name, T value) {
        handleFieldOperation(instance, name, FieldOperation.SET, value);
    }

    // 处理字段操作
    private static <T> Object handleFieldOperation(T instance, String name, FieldOperation operation, T value) {
        Field field = getFieldFromClassHierarchy(instance.getClass(), name);
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

    // 在当前类以及父类中查找字段
    private static Field getFieldFromClassHierarchy(Class<?> clazz, String name) {
        // 使用缓存机制和递归查找父类中的字段
        Map<String, Field> fields = fieldCache.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());

        return fields.computeIfAbsent(name, k -> {
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
     * @param instance 对象
     * @param method 执行方法
     * @param args 参数
     */
    public static <T> Object invokeMethod(T instance, Method method, T args) {
        method.setAccessible(true);
        try {
            return args != null ? method.invoke(instance, args) : method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException("调用方法失败: " + e.getMessage(), e);
        }
    }

    public static <T> Object invokeMethod(T instance, String methodName, T args) throws NoSuchMethodException {
        Method method = instance.getClass().getMethod(methodName);// public 的方法
        // Method method = getMethod(instance.getClass(), methodName, null);
        return invokeMethod(instance, method, args);
    }

    /**
     * 根据 method name 获取类方法 findMethodInHierarchy
     *
     * @param clazz 类
     * @param methodName 需要查找的方法名
     */
    public static Method getMethod(Class<?> clazz, String methodName) {

        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Method method = Arrays.stream(currentClass.getDeclaredMethods())
                    .filter(m -> m.getName().equals(methodName) && m.getParameterCount() == 1)
                    .findFirst()
                    .orElse(null);
            if (method != null) {
                return method;
            }
            currentClass = currentClass.getSuperclass(); // 继续在父类中查找
        }
        throw new RuntimeException("找不到该方法: " + methodName);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        Class<?> currentClass = clazz;

        while (currentClass != null) {
            Method method = Arrays.stream(currentClass.getDeclaredMethods())
                    .filter(m -> m.getName().equals(methodName))
                    // .filter(m -> paramTypes == null ? m.getParameterCount() == paramCount : Arrays.equals(m.getParameterTypes(), paramTypes))
                    .filter(m -> paramTypes == null || Arrays.equals(m.getParameterTypes(), paramTypes))
                    .findFirst()
                    .orElse(null);

            if (method != null) {
                return method;
            }
            currentClass = currentClass.getSuperclass();
        }

        throw new RuntimeException("找不到该方法: " + methodName);
    }


    // 检查值的类型是否与字段的类型兼容
    private static boolean isCompatibleType(Field field, Object value) {
        if (value == null) {
            return !field.getType().isPrimitive();
        }
        return field.getType().isAssignableFrom(value.getClass());
    }

    // 枚举字段操作类型
    private enum FieldOperation {
        GET, SET
    }


}
