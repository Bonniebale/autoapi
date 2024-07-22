package com.yg.api.service.stock;

import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.common.utils.RequestUtil;
import com.yg.api.service.BaseApiService;
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


    /**
     * 查询箱及仓位库存信息
     *
     * @param skus      sku
     * @param whTypeId  仓库类型id 1: 主仓, 2: 销退仓 3: 进货仓, 4: 次品仓, 6: 自定义1仓
     * @param packIds   可使用暂存位id, 箱id
     * @param packType  库存类型 pack: 箱, bin: 仓位, defaultPack: 暂存位
     * @param bins      仓位
     * @param isCombine 是否组合装商品
     * @param batchId   生产批次id
     * @param ownerWhId 货主仓库id
     * @param subWhId   分仓id
     */
    public <T> JsonPath getStockInfo(List<String> skus, Integer whTypeId, T packIds, String packType, List<String> bins, boolean isCombine,
                                     String batchId, Integer ownerWhId, Integer subWhId) {

        String path = CommonUtil.generateCrossPath(ApiConstant.PACK_ASPX, ownerWhId, subWhId);
        String skuField = isCombine ? "combine_sku_id" : "sku_id";

        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("[pit]." + skuField, skus);
        queryConditions.put("[p].wh_id", whTypeId);
        queryConditions.put("[p].pack_id", packIds);
        queryConditions.put("[p].bin", bins);
        queryConditions.put("pack_type", packType);
        queryConditions.put("[pit].pb_id", batchId);

        List<Map<String, String>> queryParam = CommonUtil.generateQueryCondition(queryConditions);
        var data = RequestDataHandler.generateQueryParams(queryParam);
        return RequestUtil.sendUrlencJ(path, data);
    }

    public JsonPath getStockInfo(List<String> skus) {
        return getStockInfo(skus, null, null, null, null, false, null, null, null);
    }

    //暂存位库存
    public JsonPath getStockInfo(List<String> skus, String packId, String batchId) {
        return getStockInfo(skus, null, packId, null, null, false, batchId, null, null);
    }

    //仓位库存
    public JsonPath getStockInfo(List<String> skus, List<String> bin, String batchId, boolean isCombine) {
        return getStockInfo(skus, null, null, null, bin, isCombine, batchId, null, null);
    }
}
