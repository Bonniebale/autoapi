package com.yg.api.common.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ReflectUtil
 * @Description 反射Util
 * @Author Flora
 * @Date 2024/7/26 15:14
 */
public class ReflectUtil {
    private static final Map<Class<?>, Map<String, Field>> fieldCache = new ConcurrentHashMap<>();//缓存字段反射操作



    /**
     * 根据fieldName获取该实例的字段值
     */
    private static Object getFieldValueFromClassHierarchy(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {

        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true); // 设置字段可访问
                return field.get(instance); // 返回字段的值
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("No such field: " + fieldName);
    }

    /**
     * 根据fieldName获取该实例的字段值
     */
    public static Object getFieldValue(Object instance, Class<?> clazz, String fieldName) throws IllegalAccessException {
        Field field = getFieldFromClassHierarchy(clazz, fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * 在当前类以及父类中查找字段
     */
    private static Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) {
        //使用缓存机制和递归查找父类中的字段
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




}
