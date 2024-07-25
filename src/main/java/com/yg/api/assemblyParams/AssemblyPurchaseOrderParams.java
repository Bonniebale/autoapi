package com.yg.api.assemblyParams;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.entity.PurchaseOrderDto;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName AssemblyPurchaseOrderParams
 * @Description 采购单param
 * @Author flora
 * @Date 2024/7/25 13:59
 */
public class AssemblyPurchaseOrderParams {

    // 查询采购单
    public static JSONObject generateGetOrderInfoParam(List<Integer> orderId, List<String> status, List<String> sku, List<Integer> supplierId) {

        List<Integer> safeOrderId = orderId != null ? orderId : Collections.emptyList();
        List<String> safeStatus = status != null ? status : Collections.emptyList();
        List<String> safeSku = sku != null ? sku : Collections.emptyList();
        List<Integer> safeSupplierId = supplierId != null ? supplierId : Collections.emptyList();

        JSONObject body = new JSONObject();
        body.put("page", new JSONObject() {{
            put("currentPage", 1);
            put("pageSize", 50);
        }});

        body.put("data", new JSONObject()
                        .fluentPut("SupplierIds", safeSupplierId)
                        .fluentPut("StatusList", safeStatus)
                        .fluentPut("Poids", safeOrderId)
                        .fluentPut("SkuIds", safeSku)
                // .fluentPut("PoDateStart", "2024-04-07")
                // .fluentPut("PoDateEnd", "2024-05-08")
        );
        return body;
    }

    // 创建采购单
    public static JSONObject generateCreateOrderParam(PurchaseOrderDto purchaseOrderDto) {
        JSONObject body = new JSONObject();
        ArrayList<JSONObject> paramList = new ArrayList<>();
        paramList.add(new JSONObject()
                .fluentPut("wmsCoId", purchaseOrderDto.getCompanyId())
                .fluentPut("supplierId", purchaseOrderDto.getSupplierId())
                .fluentPut("items", "自动化")
                .fluentPut("createFrom", "自动化")
                .fluentPut("createFrom", "自动化")
        );
        body.put("data", new JSONObject()
                        .fluentPut("createFrom", "自动化")
                // .fluentPut("createPurchases", safeStatus)
        );
        return body;
    }

    private static <T> Map<String, Object> createSkuQtyMap(Class<T> clazz) throws NoSuchMethodException {

        Method[] declaredMethods = clazz.getDeclaredMethods();

        // 获取指定的方法
        Method getSku = clazz.getDeclaredMethod("getSku", List.class);
        Method getQty = clazz.getDeclaredMethod("getQty", int.class);

        Map<String, Object> skuInfo = new HashMap<>();
        skuInfo.put("skuId", "");
        skuInfo.put("qty", "");

        Optional.ofNullable(clazz.getBatchId()).ifPresent(batchId -> {
            skuInfo.put("batchId", batchId);
            skuInfo.put("producedDate", clazz.getProductionDate());
            skuInfo.put("expirationDate", clazz.getExpirationDate());
        });
        return skuInfo;
    }

    private static <T> Map<String, Object> createSkuQtyMap(T t) throws NoSuchMethodException, NoSuchFieldException {


        Field sku = t.getClass().getDeclaredField("sku");
        Field qty = t.getClass().getDeclaredField("qty");

        Map<String, Object> skuInfo = new HashMap<>();
        skuInfo.put("skuId", sku);
        skuInfo.put("qty", qty);
        return skuInfo;
    }

}
