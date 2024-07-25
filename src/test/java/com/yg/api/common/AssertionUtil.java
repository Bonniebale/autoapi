package com.yg.api.common;

import org.testng.Assert;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * @ClassName AssertionUtil
 * @Description 校验
 * @Author flora
 * @Date 2024/7/22 21:35
 */
public class AssertionUtil {
    private static final Logger logger = Logger.getLogger(AssertionUtil.class.getName());

    // 单条数据断言
    public static void assertEquals(Object expected, Object actual, String message, String field) {
        String defaultMessage = String.format("%s校验失败, 期望: %s, 实际: %s", field != null ? field : "", expected, actual);
        Assert.assertEquals(actual, expected, message != null ? message : defaultMessage);
    }

    /**
     * 库存断言
     *
     * @param initialStock 初始库存
     * @param currentStock 当前库存
     * @param fields 校验字段和数量
     * @param desc 库存类型
     */
    public static void verifyStockInfo(List<Map<String, Object>> initialStock, List<Map<String, Object>> currentStock, Map<String, Integer> fields, String desc) {
        logger.info("校验: " + desc + ", 字段: " + fields);
        logger.info("初始库存: " + initialStock);
        logger.info("当前库存: " + currentStock);

        IntStream.range(0, Math.min(initialStock.size(), currentStock.size()))
                .forEach(i -> fields.forEach((field, qty) -> {
                    Number initialQtyNumber = (Number)initialStock.get(i).get(field);
                    Number actualNumber = (Number)currentStock.get(i).get(field);
                    int initialQty = (initialQtyNumber != null) ? initialQtyNumber.intValue() : 0;
                    int actual = (actualNumber != null) ? actualNumber.intValue() : 0;
                    assertEquals(initialQty + qty, actual, null, field);
                }));
    }

    /**
     * 批量校验库存
     *
     * @param stockDataList 初始库存和当前库存数据列表
     * [{"initialStock": "初始库存", "currentStock": "当前库存", "field": {"需要校验的字段名": 库存变化值},"desc":"描述"}]
     */
    public static void batchVerifyStockInfo(List<Map<String, Object>> stockDataList) {

        if (stockDataList == null || stockDataList.isEmpty()) {
            throw new IllegalArgumentException("库存数据列表不能为空");
        }

        for (Map<String, Object> stock : stockDataList) {
            List<Map<String, Object>> initialStock = castList(stock.get("initialStock"));
            List<Map<String, Object>> currentStock = castList(stock.get("currentStock"));
            Map<String, Integer> field = castMap(stock.get("field"));
            String desc = (String)stock.get("desc");
            verifyStockInfo(initialStock, currentStock, field, desc);
        }

    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> castList(Object obj) {
        return (List<T>)obj;
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Map<K, V> castMap(Object obj) {
        return (Map<K, V>)obj;
    }


}
