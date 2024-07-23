package com.yg.api.service;

import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName PurchaseOrderApiService
 * @Description 采购单ApiService
 * @Author Flora
 * @Date 2024/7/23 14:54
 */
@Service
public class PurchaseOrderApiService extends BaseApiService {

    public JsonPath getOrderDetail(List<Integer> purchaseId) {
        return null;
    }
}
