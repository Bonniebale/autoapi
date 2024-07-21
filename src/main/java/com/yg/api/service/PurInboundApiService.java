package com.yg.api.service;

import com.yg.api.assemblyParams.AssemblyInboundParams;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.common.utils.RequestDataHandler;
import com.yg.api.common.utils.RequestUtil;
import com.yg.api.entity.InboundDto;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName PurInboundApiService
 * @Description 采购进仓+ ApiService
 * @Author Flora
 * @Date 2024/7/19 14:48
 */
@Service
public class PurInboundApiService extends BaseApiService {

    // 入库点数、入库装箱
    public Object createInboundOrder(InboundDto inboundDto, Integer ownerWhId, Integer subWhId) {
        String path = CommonUtil.generateCrossPath(ApiConstant.IN_COUNT_ASPX, ownerWhId, subWhId);
        Map<String, Object> params = AssemblyInboundParams.generateInboundParams(inboundDto);
        return RequestUtil.sendPostUrlenc(path, params);
    }

    // 创建箱唯一码
    public JsonPath create_pack_sn() {
        Map<String, Object> params = RequestDataHandler.generateReqParamACall("GetAutoPackSn");
        return RequestUtil.sendPostUrlenc(ApiConstant.IN_COUNT_ASPX, params);
    }

}
