package com.yg.api.common.utils;

/**
 * @ClassName SkuInfoUtil
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/26 15:10
 */

import com.yg.api.entity.InboundDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class SkuInfoUtil {


    public static <T> Map<String, Object> createSkuQtyMap(T t) throws IllegalAccessException {
        Map<String, Object> skuInfo = new HashMap<>();
        Class<?> clazz = t.getClass();

        // Populate common fields
        populateField(skuInfo, t, clazz, "skuId", "sku");
        populateField(skuInfo, t, clazz, "qty", "qty");

        // Handle optional batch-related fields
        Optional<Object> batchIdOpt = Optional.ofNullable(ReflectUtil.getFieldValue(t, clazz, "batchId"));
        if (batchIdOpt.isPresent()) {
            populateField(skuInfo, t, clazz, "batchId", "batchId");
            populateField(skuInfo, t, clazz, "producedDate", "productionDate");
            populateField(skuInfo, t, clazz, "expirationDate", "expirationDate");
        }

        // Populate serial number fields
        Stream.of("serialNumber", "seriesNumber", "packSn").forEach(field -> {
            try {
                populateField(skuInfo, t, clazz, field, field);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return skuInfo;
    }


    private static <T> void populateField(Map<String, Object> skuInfo, T instance, Class<?> clazz, String mapKey, String fieldName) throws IllegalAccessException {
        Object value = ReflectUtil.getFieldValue(instance, clazz, fieldName);
        if (value != null) {
            skuInfo.put(mapKey, value);
        }
    }


    public static void main(String[] args) {
        try {
            InboundDto inboundDto = new InboundDto();
            inboundDto.setQty(10);
            inboundDto.setBatchId("BATCH001");
            inboundDto.setProductionDate("2023-01-01");
            inboundDto.setExpirationDate("2024-01-01");
            inboundDto.setSerialNumber("SN123456");
            inboundDto.setPackSn("PACKSN001");

            Map<String, Object> skuInfo = createSkuQtyMap(inboundDto);
            System.out.println(skuInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
