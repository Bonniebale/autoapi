package com.yg.api.assemblyParams;

import com.alibaba.fastjson2.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName AssemblyPurchaseInOrderParams
 * @Description 采购入库单param
 * @Author flora
 * @Date 2024/7/25 15:10
 */
public class AssemblyPurchaseInOrderParams {
    // 查询采购入库单
    public static JSONObject generateGetOrderInfoParam(List<Integer> ioIds, List<String> status, List<Integer> poIds, List<Integer> soIds) {

        List<Integer> safeOrderId = ioIds != null ? ioIds : Collections.emptyList();
        List<String> safeStatus = status != null ? status : Collections.emptyList();
        List<Integer> safePoId = poIds != null ? poIds : Collections.emptyList();
        List<Integer> safeSoId = soIds != null ? soIds : Collections.emptyList();

        JSONObject body = new JSONObject();
        body.put("data", new JSONObject()
                .fluentPut("ioIds", safeOrderId)
                .fluentPut("poIds", safePoId)
                .fluentPut("statuss", safeStatus)
                .fluentPut("soIds", safeSoId)
                .fluentPut("modifiedBegin", "")
                .fluentPut("modifiedEnd", "")
        );
        return body;
    }
}
