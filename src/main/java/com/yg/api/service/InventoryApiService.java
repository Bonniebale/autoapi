package com.yg.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.assemblyParams.AssemblyInventoryParams;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.enums.WhTypeEnum;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.common.utils.RequestUtil;
import io.restassured.path.json.JsonPath;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
//    @Autowired
//    PackStockApiService packStockApiService;//Field injection is not recommended

    private PackStockApiService packStockApiService;

    @Autowired
    public void setPackStockApiService(PackStockApiService packStockApiService) {
        this.packStockApiService = packStockApiService;
    }

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

    //查询组合装库存
    public JsonPath getCombSkuStock(List<String> skus, Integer ownerWhId, Integer subWhId) {
        String path = CommonUtil.generateCrossPath(ApiConstant.SUB_STOCK_ASPX, ownerWhId, subWhId);
        var queryParam = CommonUtil.generateQueryCondition(Map.of(
                        "sku_id", skus
                )
        );
        String data = RequestDataHandler.generateQueryParams(queryParam);
        return RequestUtil.sendPostUrlenc(path, data);
    }


    /**
     * 获取sku库存，包括主仓库存、箱及仓位库存、分仓库存、组合装库存、生产批次库存
     *
     * @param sku          skuId，如果comSku传值，则为组合装skuId
     * @param whTypeId     仓库类型id 1: 主仓, 2: 销退仓 3: 进货仓, 4: 次品仓, 6: 自定义1仓
     * @param subStorageId 需要查询商品库存(分仓)的公司id
     * @param companyId    需要查询暂存位库存的公司id
     * @param bin          仓位
     * @param comSku       组合装中的子sku
     * @param batchId      生产批次id
     * @param isRefined    是否精细化用户 true 是 false 否
     * @param ownerWhId    货主id
     * @param subWhId      有权限的仓库id
     */
    public HashMap<String, Object> getMultipleStocks(List<String> sku, WhTypeEnum whTypeId, List<Integer> subStorageId, Integer companyId, List<String> bin,
                                                     List<String> comSku, List<String> batchId, boolean isRefined, Integer ownerWhId, Integer subWhId) {
        HashMap<String, Object> stockMap = new HashMap<>();
        //商品库存
        var stock = getSkuStock(sku, ownerWhId, subWhId);

        //商品库存(分仓)库存
        var subStock = getSubWarehouseSkuStock(sku, subStorageId, ownerWhId, subWhId);
        stockMap.put("stock", stock);
        stockMap.put("subStock", subStock);

        //暂存位库存
        if (null != companyId) {
            String tempId = CommonUtil.generateTempId(companyId, whTypeId.getId());
            var tempStock = packStockApiService.getStockInfo(sku, tempId, batchId);
            stockMap.put("tempStock", tempStock);
        }
        //仓位库存
        if (CollectionUtils.isNotEmpty(bin)) {
            List<String> querySku = CollectionUtils.isNotEmpty(comSku) ? comSku : sku;
            boolean isCombine = CollectionUtils.isNotEmpty(comSku);
            var binStock = packStockApiService.getStockInfo(querySku, bin, batchId, isCombine);
            stockMap.put("binStock", binStock);
        }

        //组合装库存
        if (CollectionUtils.isNotEmpty(comSku)) {
            var comStock = getCombSkuStock(comSku, ownerWhId, subWhId);
            stockMap.put("comStock", comStock);

        }
        //生产批次库存
        if (!isRefined && CollectionUtils.isNotEmpty(batchId)) {
            stockMap.put("batchStock", "batchStock");

        }

        return stockMap;

    }


}
