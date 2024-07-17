package com.yg.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.assemblyParams.AssemblyInventoryParams;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.common.utils.RequestUtil;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yg.api.common.enums.UrlEnum.API;

/**
 * @ClassName InventoryService
 * @Description TODO
 * @Author flora
 * @Date 2024/7/17 22:23
 */
@Service
public class InventoryApiService extends BaseApiService {

    // 查询商品库存
    public JsonPath getSkuStock(List<String> skus, Integer ownerWhId, Integer subWhId) {
        String path = CommonUtil.generateCrossPath(ApiConstant.STOCK, ownerWhId, subWhId);
        JSONObject data = AssemblyInventoryParams.generateGetStockParams(skus);
        return RequestUtil.sendPost(path, data, API);
    }

    // 查询商品库存(分仓)
    public JsonPath getSubWarehouseSkuStock(List<String> skus, List<Integer> storages, Integer ownerWhId, Integer subWhId) {
        String path = CommonUtil.generateCrossPath(ApiConstant.SUB_STOCK_ASPX, ownerWhId, subWhId);
        var queryParam = CommonUtil.generateQueryCondition(Map.of(
                        "sku_id", skus,
                        "wms_co_id", storages
                )
        );
        String data = RequestDataHandler.generateQueryParams(queryParam);
        return RequestUtil.sendPostUrlenc(path, data);
    }


}
