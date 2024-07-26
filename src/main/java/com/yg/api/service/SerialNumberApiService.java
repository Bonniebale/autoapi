package com.yg.api.service;

import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.common.utils.RequestUtil;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SerialNumberApiService
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/23 15:00
 */
@Service
public class SerialNumberApiService extends BaseApiService {

    /**
     * 获取跟踪信息
     * @param skuSn sku唯一码
     * @param packSn 箱唯一码
     */
    public JsonPath getTrackingInfo(List<String> skuSn, List<String> packSn) {
        var queryParam = CommonUtil.generateQueryCondition(Map.of(
                        "skus", skuSn,
                        "sku_sn", skuSn,
                        "pack_sn", packSn
                )
        );
        var data = RequestDataHandler.generateQueryParams(queryParam);
        return RequestUtil.sendPostUrlenc(ApiConstant.GET_SN_TRACKING_ASPX, data);
    }


}