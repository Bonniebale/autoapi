package com.yg.api.assemblyParams;

import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.entity.InboundDto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName AssemblyInboundParams
 * @Description 采购入库+ 接口参数
 * @Author flora
 * @Date 2024/7/21 23:08
 */
public class AssemblyInboundParams {
    // 入库点数、入库装箱param
    // public static Map<String, Object> generateInboundParams(InboundDto inboundDto) {
    //
    //     // 组装 SKU 信息
    //     List<Map<String, Object>> skuInfos = inboundDto.getSku().stream()
    //             .map(sku -> createSkuInfoMap(inboundDto, sku))
    //             .toList();
    //
    //     // 参数字段
    //     Map<String, Object> argsFields = new HashMap<>();
    //     argsFields.put("packQty", inboundDto.getPackQty());
    //     argsFields.put("inByPack", inboundDto.isInByPack());
    //     argsFields.put("supplierId", inboundDto.getSupplierId());
    //     argsFields.put("packId", inboundDto.getPackId());
    //     argsFields.put("deliveryNo", inboundDto.getDeliveryNo());
    //     argsFields.put("inType", inboundDto.getInboundType());
    //     argsFields.put("whId", inboundDto.getWhTypeId());
    //     argsFields.put("poId", inboundDto.getOrderId());
    //     argsFields.put("lid", inboundDto.getLogisticsId());
    //     argsFields.put("bioId", inboundDto.getReservationId());
    //
    //     // 组装 items 字段
    //     List<Map<String, Object>> items = skuInfos.stream().map(skuInfo -> {
    //         Map<String, Object> item = new HashMap<>();
    //         item.put("skuId", skuInfo.get("skuId"));
    //         item.put("qty", skuInfo.get("qty"));
    //         item.put("price", skuInfo.getOrDefault("price", 0));
    //         item.put("sn", skuInfo.getOrDefault("sn", ""));
    //         item.put("seriesNumber", skuInfo.getOrDefault("seriesNumber", ""));
    //         item.put("packSn", skuInfo.getOrDefault("packSn", ""));
    //         if (skuInfo.containsKey("batchId")) {
    //             item.put("batchId", skuInfo.get("batchId"));
    //             item.put("producedDate", skuInfo.get("producedDate"));
    //             item.put("expirationDate", skuInfo.get("expirationDate"));
    //         }
    //         return item;
    //     }).collect(Collectors.toList());
    //
    //     argsFields.put("items", items);
    //
    //     // 如果需要箱唯一码，则添加 packSnItems 字段
    //     if (inboundDto.isNeedPackSn()) {
    //         argsFields.put("packSnItems", items);
    //     }
    //     return RequestDataHandler.generateReqParamACall("InCount", Collections.singletonList(argsFields));
    // }

    public static Map<String, Object> generateInboundParams(InboundDto inbound) {
        List<Map<String, Object>> skuInfos = inbound.getSku().stream()
                .map(sku -> createSkuInfoMap(inbound, sku))
                .collect(Collectors.toList());

        Map<String, Object> argsFields = new HashMap<>();
        argsFields.put("packQty", inbound.getPackQty());
        argsFields.put("inByPack", inbound.isInByPack());
        argsFields.put("supplierId", inbound.getSupplierId());
        argsFields.put("packId", inbound.getPackId());
        argsFields.put("deliveryNo", inbound.getDeliveryNo());
        argsFields.put("inType", inbound.getInboundType());
        argsFields.put("whId", inbound.getWhTypeId());
        argsFields.put("poId", inbound.getOrderId());
        argsFields.put("lid", inbound.getLogisticsId());
        argsFields.put("bioId", inbound.getReservationId());
        argsFields.put("items", skuInfos);

        if (inbound.isNeedPackSn()) {
            argsFields.put("packSnItems", skuInfos);
        }

        return RequestDataHandler.generateReqParamACall("InCount", argsFields);
    }

    private static Map<String, Object> createSkuInfoMap(InboundDto inbound, String sku) {
        Map<String, Object> skuInfo = new HashMap<>();
        skuInfo.put("skuId", sku);
        skuInfo.put("qty", inbound.getQty());

        Optional.ofNullable(inbound.getBatchId()).ifPresent(batchId -> {
            skuInfo.put("batchId", batchId);
            skuInfo.put("producedDate", inbound.getProductionDate());
            skuInfo.put("expirationDate", inbound.getExpirationDate());
        });

        skuInfo.put("sn", inbound.getSerialNumber());
        skuInfo.put("seriesNumber", inbound.getSerialNumber());
        skuInfo.put("packSn", inbound.getPackSn());

        return skuInfo;
    }

}
