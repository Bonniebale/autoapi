package com.yg.api.service;

import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SerialNumberApiService
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/23 15:00
 */
@Service
public class SerialNumberApiService extends BaseApiService {

    public JsonPath getTrackingInfo(List<String> skuSn,List<String> packSn) {
        return null;
    }


}