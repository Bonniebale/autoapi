package com.yg.api.service.stock;

import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.common.utils.RequestUtil;
import com.yg.api.service.BaseApiService;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ProductionBatchApiService
 * @Description 生产批次ApiService
 * @Author flora
 * @Date 2024/7/21 16:33
 */
@Service
public class BatchApiService extends BaseApiService {

    //生成生产批次
    public Map<String, String> generateBatchInfo() {

        String todayStr = CommonUtil.getSpecifiedDateOrTimeStr("yyyy-MM-dd");
        String expirationDate = CommonUtil.getSpecifiedDateOrTimeStr(365, "yyyy-MM-dd");
        String batchId = CommonUtil.generateSpecifiedLengthStr(14);
        return Map.of("batchId", batchId, "productionDate", todayStr, "expirationDate", expirationDate);
    }


    //查询库存
    public JsonPath getBatchStock(List<String> sku, String batchId) {

        List<Map<String, String>> queryParam = CommonUtil.generateQueryCondition(
                Map.of("sku_id", sku, "batch_id", batchId)
        );
        var data = RequestDataHandler.generateQueryParams(queryParam);
        return RequestUtil.sendPostUrlenc(ApiConstant.BATCH_STOCK_ASPX, data);
    }
}
