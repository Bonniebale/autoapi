package com.yg.api.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @ClassName SkuInfoUtil
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/26 15:10
 */
public class SkuInfoUtil {


    public static <T> Map<String, Object> createSkuQtyMap(T instance) {
        Map<String, Object> skuInfo = new HashMap<>();

        // Populate common fields
        Stream.of("sku", "qty", "serialNumber").forEach(field ->
                populateField(skuInfo, instance, field)
        );

        // Handle optional batch-related fields
        Optional.ofNullable(ReflectUtil.getFieldValue(instance, "batchId")).ifPresent(batchId -> {
            skuInfo.put("batchId", batchId);
            populateField(skuInfo, instance, "productionDate");
            populateField(skuInfo, instance, "expirationDate");
        });

        return skuInfo;
    }


    /**
     * 填充字段
     *
     * @param skuMap skuMap
     * @param instance 类实例
     * @param fieldName 字段名称
     */
    private static <T> void populateField(Map<String, Object> skuMap, T instance, String fieldName) {
        Object value = ReflectUtil.getFieldValue(instance, fieldName);
        if (DataUtil.isValidValue(value)) {
            skuMap.put(fieldName, value);
        }
    }

}
