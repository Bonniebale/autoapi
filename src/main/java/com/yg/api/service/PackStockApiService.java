package com.yg.api.service;

import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataUtil;
import com.yg.api.common.utils.RequestUtil;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PackStockApiService
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/13 17:33
 */
@Service
public class PackStockApiService extends BaseApiService {


    // 查询箱及仓位库存信息
    public JsonPath getStockInfo(List<String> skus, Integer whTypeId, List<String> packIds, String packType, List<String> bins, boolean isCombine,
                                          List<String> batchId, Integer ownerWhId, Integer subWhId) throws JsonProcessingException {

        String path = CommonUtil.generateCrossPath(ApiConstant.PACK_ASPX, ownerWhId, subWhId);
        String skuField = isCombine ? "combine_sku_id" : "sku_id";

        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("[pit]." + skuField, skus);
        queryConditions.put("[p].wh_id", whTypeId);
        queryConditions.put("[p].pack_id", packIds);
        queryConditions.put("[p].bin", bins);
        queryConditions.put("pack_type", packType);
        queryConditions.put("[pit].pb_id", batchId);

        var queryParam = CommonUtil.generateQueryCondition(queryConditions);
        var data = RequestDataUtil.generateQueryParams(queryParam);
        System.out.println(data);
        return RequestUtil.sendPostUrlenc(path, data);
    }

    public JsonPath getStockInfo(List<String> skus, Integer whTypeId) throws JsonProcessingException {
        return getStockInfo(skus, whTypeId, null, null, null, false, null, null, null);
    }
}
