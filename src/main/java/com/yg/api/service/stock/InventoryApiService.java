package com.yg.api.service.stock;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.assemblyParams.AssemblyInventoryParams;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.common.utils.RequestUtil;
import com.yg.api.service.BaseApiService;
import io.restassured.path.json.JsonPath;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yg.api.common.enums.UrlEnum.API_WEB;

/**
 * @ClassName InventoryService
 * @Description TODO
 * @Author flora
 * @Date 2024/7/17 22:23
 */
@Service
public class InventoryApiService extends BaseApiService {

    private PackStockApiService packStockApiService;
    private BatchApiService batchApiService;

    @Autowired
    public void setPackStockApiService(PackStockApiService packStockApiService) {
        this.packStockApiService = packStockApiService;
    }

    @Autowired
    public void setBatchApiService(BatchApiService batchApiService) {
        this.batchApiService = batchApiService;
    }

    // 查询商品库存
    public JsonPath getSkuStock(List<String> skus, Integer ownerWhId, Integer subWhId) {
        String path = CommonUtil.generateCrossPath(ApiConstant.STOCK, ownerWhId, subWhId);
        JSONObject data = AssemblyInventoryParams.generateGetStockParams(skus);
        return RequestUtil.sendPost(path, data, API_WEB);
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
        return RequestUtil.sendUrlencJ(path, data);
    }

    // 查询组合装库存
    public JsonPath getCombSkuStock(List<String> skus, Integer ownerWhId, Integer subWhId) {
        String path = CommonUtil.generateCrossPath(ApiConstant.SUB_STOCK_ASPX, ownerWhId, subWhId);
        var queryParam = CommonUtil.generateQueryCondition(Map.of(
                        "sku_id", skus
                )
        );
        String data = RequestDataHandler.generateQueryParams(queryParam);
        return RequestUtil.sendUrlencJ(path, data);
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
    public Map<String, List<Map<String, Object>>> getMultipleStocks(List<String> sku, int whTypeId, List<Integer> subStorageId, Integer companyId, List<String> bin,
                                                                    List<String> comSku, String batchId, boolean isRefined, Integer ownerWhId, Integer subWhId) {

        Map<String, List<Map<String, Object>>> stockMap = new HashMap<>();
        // 商品库存
        List<Map<String, Object>> stock = getSkuStock(sku, ownerWhId, subWhId).getList("data");

        // 商品库存(分仓)库存
        List<Map<String, Object>> subStock = getSubWarehouseSkuStock(sku, subStorageId, ownerWhId, subWhId).getList("ReturnValue.datas");
        stockMap.put("stock", stock);
        stockMap.put("subStock", subStock);

        // 暂存位库存
        if (null != companyId) {
            String tempId = CommonUtil.generateTempId(companyId, whTypeId);
            List<Map<String, Object>> tempStock = packStockApiService.getStockInfo(sku, tempId, batchId).getList("ReturnValue.datas");
            stockMap.put("tempStock", tempStock);
        }

        // 仓位库存
        if (CollectionUtils.isNotEmpty(bin)) {
            List<String> querySku = CollectionUtils.isNotEmpty(comSku) ? comSku : sku;
            boolean isCombine = CollectionUtils.isNotEmpty(comSku);
            List<Map<String, Object>> binStock = packStockApiService.getStockInfo(querySku, bin, batchId, isCombine).getList("ReturnValue.datas");
            stockMap.put("binStock", binStock);
        }

        // 组合装库存
        if (CollectionUtils.isNotEmpty(comSku)) {
            List<Map<String, Object>> comStock = getCombSkuStock(comSku, ownerWhId, subWhId).getList("ReturnValue.datas");
            stockMap.put("comStock", comStock);
        }

        // 生产批次库存
        if (!isRefined && StringUtils.isNotBlank(batchId)) {
            List<Map<String, Object>> batchStock = batchApiService.getBatchStock(sku, batchId).getList("ReturnValue.datas");
            stockMap.put("batchStock", batchStock);
        }

        return stockMap;
    }


}
