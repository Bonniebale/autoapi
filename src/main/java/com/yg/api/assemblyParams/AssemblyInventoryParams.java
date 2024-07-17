package com.yg.api.assemblyParams;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AssemblyInventoryParams
 * @Description 库存params
 * @Author flora
 * @Date 2024/7/17 22:38
 */
public class AssemblyInventoryParams {
    // 查询库存
    public static JSONObject generateGetStockParams(List<String> skus) {

        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        body.put("page", Map.of(
                "currentPage", 1,
                "pageSize", 50,
                "hasPageInfo", true,
                "pageAction", 1));
        body.put("data", data);
        data.put("sku_type", "1");
        data.put("sku_id", String.join(",", skus));
        data.put("stock_type", 6);
        data.put("queryFlds", List.of("sku_id", "name", "qty", "return_qty", "in_qty", "defective_qty", "customize_qty_1", "purchase_qty", "allocate_qty"));

        return body;
    }


}
