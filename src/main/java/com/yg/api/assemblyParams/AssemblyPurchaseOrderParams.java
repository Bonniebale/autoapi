package com.yg.api.assemblyParams;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.entity.PurchaseOrderDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

}
