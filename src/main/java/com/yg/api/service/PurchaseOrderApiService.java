package com.yg.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.assemblyParams.AssemblyPurchaseOrderParams;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.RequestUtil;
import com.yg.api.common.utils.ResponseDataUtils;
import com.yg.api.entity.PurchaseOrderDto;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yg.api.common.enums.UrlEnum.API;

/**
 * @ClassName PurchaseOrderApiService
 * @Description 采购单ApiService
 * @Author Flora
 * @Date 2024/7/23 14:54
 */
@Service
public class PurchaseOrderApiService extends BaseApiService {

    /**
     * 查询采购单信息
     *
     * @param orderId 采购单id
     * @param status 采购单状态 WaitConfirm待审核，Confirmed 已审核 Finished完成，Cancelled作废
     * @param sku skuId
     * @param supplierId 供应商id
     */
    public JsonPath getOrderInfo(List<Integer> orderId, List<String> status, List<String> sku, List<Integer> supplierId) {
        JSONObject data = AssemblyPurchaseOrderParams.generateGetOrderInfoParam(orderId, status, sku, supplierId);
        var response = RequestUtil.sendPost(ApiConstant.GET_PURCHASE_DETAIL, data, API);
        return ResponseDataUtils.checkQueryResponseData(response);
    }

    public JsonPath getOrderInfo(List<Integer> orderId) {
        return getOrderInfo(orderId, null, null, null);
    }

    /**
     * 创建采购单
     *
     * @param purchaseOrderDto 采购单model
     */
    public JsonPath createOrder(PurchaseOrderDto purchaseOrderDto) {
        JSONObject data = AssemblyPurchaseOrderParams.generateCreateOrderParam(purchaseOrderDto);
        return RequestUtil.sendPost(ApiConstant.CREATE_PURCHASE, data, API);
    }

    /**
     * 创建并返回采购单id
     */
    public Integer getCreateOrderId(PurchaseOrderDto purchaseOrderDto) {
        return createOrder(purchaseOrderDto).getInt("data.successPoIds[0]");
    }


}
