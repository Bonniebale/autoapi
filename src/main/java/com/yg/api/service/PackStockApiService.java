package com.yg.api.service;

import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.enums.WhTypeEnum;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.common.utils.RequestUtil;
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
    public <T> JsonPath getStockInfo(List<String> skus, WhTypeEnum whTypeId, T packIds, String packType, List<String> bins, boolean isCombine,
                                     T batchId, Integer ownerWhId, Integer subWhId) {

        String path = CommonUtil.generateCrossPath(ApiConstant.PACK_ASPX, ownerWhId, subWhId);
        String skuField = isCombine ? "combine_sku_id" : "sku_id";

        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("[pit]." + skuField, skus);
        queryConditions.put("[p].wh_id", whTypeId.getId());
        queryConditions.put("[p].pack_id", packIds);
        queryConditions.put("[p].bin", bins);
        queryConditions.put("pack_type", packType);
        queryConditions.put("[pit].pb_id", batchId);

        var queryParam = CommonUtil.generateQueryCondition(queryConditions);
        var data = RequestDataHandler.generateQueryParams(queryParam);
        System.out.println(data);
        return RequestUtil.sendPostUrlenc(path, data);
    }

    public JsonPath getStockInfo(List<String> skus) {
        return getStockInfo(skus, null, null, null, null, false, null, null, null);
    }

    //暂存位库存
    public JsonPath getStockInfo(List<String> skus, String packId, List<String> batchId) {
        return getStockInfo(skus, null, packId, null, null, false, batchId, null, null);
    }

    //仓位库存
    public JsonPath getStockInfo(List<String> skus, List<String> bin, List<String> batchId, boolean isCombine) {
        return getStockInfo(skus, null, null, null, bin, isCombine, batchId, null, null);
    }
}
