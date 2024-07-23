package com.yg.api.common;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName StockDataVerifyUtil
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/22 13:43
 */
public class StockDataVerifyUtil {

    /**
     * 生成需要校验的库存数据集合
     *
     * @param originalStock 初始库存
     * @param currentStock  当前库存
     * @param qtyAndField   校验字段和数量
     */
    public static List<Map<String, Object>> generateVerifyStockData(Map<String, List<Map<String, Object>>> originalStock, Map<String, List<Map<String, Object>>> currentStock,
                                                                    Map<String, Map<String, Integer>> qtyAndField) {

        return originalStock.keySet().stream()
                .filter(stockType -> originalStock.get(stockType) != null && !originalStock.get(stockType).isEmpty())
                .map(stockType -> createVerificationEntry(
                        originalStock.get(stockType),
                        currentStock.getOrDefault(stockType, Collections.emptyList()),
                        qtyAndField.getOrDefault(stockType, Collections.emptyMap()),
                        getStockDesc(stockType)))
                .collect(Collectors.toList());
    }


    /**
     * 过滤stock = null 或者 空的库存数据
     */
    private static Map<String, List<Map<String, Object>>> filterEmptyStock(Map<String, List<Map<String, Object>>> stock) {
        return stock.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }


    /**
     * 根据 批次id + 生产日期 信息过滤
     */
    public static List<Map<String, Object>> filterStockByBatch(List<Map<String, Object>> stock, String bathId, String proDate, boolean isFilter) {

        if (!isFilter) {
            return stock;
        }

        return stock.stream()
                .filter(s -> Objects.equals(s.get("batch_id"), bathId) && Objects.equals(s.get("produced_date"), proDate))
                .collect(Collectors.toList());

    }

//    private static Map<String, List<Map<String, Object>>> getStockData(Map<String, List<Map<String, Object>>> stockDatas) {
//        Map<String, String> stockTypesKeys = Map.of(
//                "stock", "data",
//                "subStock", "datas",
//                "tempStock", "datas",
//                "binStock", "datas",
//                "comStock", "datas",
//                "batchStock", "datas"
//        );
//
//        return stockTypesKeys.entrySet().stream()
//                .filter(entry -> stockDatas.containsKey(entry.getKey()))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        entry -> stockDatas.getOrDefault(entry.getKey(), Collections.emptyList())
//                ));
//    }


    // 创建校验条目
    private static Map<String, Object> createVerificationEntry(List<Map<String, Object>> initialStock, List<Map<String, Object>> currentStock, Map<String, Integer> field, String desc) {

        if (initialStock == null || initialStock.isEmpty()) {
            return Collections.emptyMap();
        }

        return Map.of(
                "initialStock", initialStock,
                "currentStock", currentStock,
                "field", field,
                "desc", desc
        );
    }

    // 获取库存描述
    private static String getStockDesc(String stockType) {
        Map<String, String> key = Map.of(
                "tempStock", "暂存位库存",
                "binStock", "仓位库存",
                "stock", "主仓库存",
                "subStock", "分仓库存",
                "comStock", "组合装库存",
                "batchStock", "生产批次库存"
        );
        return key.getOrDefault(stockType, "");
    }

}
