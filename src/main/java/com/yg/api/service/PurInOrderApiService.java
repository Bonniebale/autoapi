package com.yg.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.assemblyParams.AssemblyPurchaseInOrderParams;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.RequestUtil;
import com.yg.api.common.utils.ResponseDataUtils;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yg.api.common.enums.UrlEnum.API;

/**
 * @ClassName PurInOrderApiService
 * @Description 采购入库单ApiService
 * @Author Flora
 * @Date 2024/7/23 14:57
 */
@Service
public class PurInOrderApiService extends BaseApiService {

    /**
     * 查询采购入库单信息
     *
     * @param ioIds 入库单id
     * @param status 入库单状态 默认查询已入库状态 WaitConfirm 待入库 Confirmed 已入库 Cancelled 取消 Delete 作废 Archive 归档 OuterConfirming 外部确认中
     * @param poIds 采购单id
     * @param soIds 线上单号
     */
    public JsonPath getOrderInfo(List<Integer> ioIds, List<String> status, List<Integer> poIds, List<Integer> soIds) {
        JSONObject data = AssemblyPurchaseInOrderParams.generateGetOrderInfoParam(ioIds, status, poIds, soIds);
        var response = RequestUtil.sendPost(ApiConstant.GET_PURCHASE_IN_DETAIL, data, API);
        return ResponseDataUtils.checkQueryResponseData(response);
    }

    public JsonPath getOrderInfo(List<Integer> ioIds) {
        return getOrderInfo(ioIds, null, null, null);
    }
}
