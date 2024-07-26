package com.yg.api.common.utils;

import com.alibaba.fastjson2.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RequestDataUtil
 * @Description 处理请求数据
 * @Author Flora
 * @Date 2024/7/13 17:48
 */
public class RequestDataHandler {

    /**
     * ACall1, 参数: [Args] && [CallControl] && 其他参数
     *
     * @param method    method
     * @param data      params
     * @param dataType  数据类型 1: 有[Args] && [CallControl] 2: 无[CallControl] 3: 无[Args]
     * @param otherArgs 其他参数
     */
    public static Map<String, Object> generateReqParamACall(String method, Map<String, Object> data, Integer dataType, Map<String, Object> otherArgs) {
        return constructRequestData(method, List.of(JSON.toJSONString(data)), "ACall1", dataType, otherArgs);
    }

    /**
     * ACall1, 参数: [Args] && [CallControl]
     */
    public static Map<String, Object> generateReqParamACall(String method, Map<String, Object> data) {
        return generateReqParamACall(method, data, 1, null);
    }


    /**
     * ACall1, 参数: [CallControl]
     */
    public static Map<String, Object> generateReqParamACall(String method) {
        return generateReqParamACall(method, null, 3, null);
    }

    /**
     * JTable1, 参数: [Args] && [CallControl] && 其他参数
     *
     * @param method    method
     * @param data      params
     * @param dataType  数据类型 1: 有[Args] && [CallControl] 2: 无[CallControl] 3: 无[Args]
     * @param otherArgs 其他参数, 指与[CallBackParam] 同级的参数
     */
    public static Map<String, Object> generateReqParamJTable(String method, List<String> data, Integer dataType, Map<String, Object> otherArgs) {
        return constructRequestData(method, data, "JTable1", dataType, otherArgs);
    }

    /**
     * JTable1, 参数: [Args] && [CallControl]
     */
    public static Map<String, Object> generateReqParamJTable(String method, List<String> data, Integer dataType) {
        return generateReqParamJTable(method, data, dataType, null);
    }


    /**
     * 构造查询参数
     */
    public static Map<String, Object> generateQueryParams(List<Map<String, String>> params) {

        return generateReqParamJTable("LoadDataToJSON", List.of("1", JSON.toJSONString(params), "{}"), 2);
    }

    /**
     * 构造请求数据
     *
     * @param method     方法名称
     * @param params     请求参数
     * @param callBackId 回调ID, ACall1 / JTable1
     * @param dataType   request数据的结构类型 1: 有[Args] && [CallControl] 2: 无[CallControl] 3: 无[Args]
     * @param otherArgs  其他参数, 指与[CallBackParam] 同级的参数
     */
    private static Map<String, Object> constructRequestData(String method, List<String> params, String callBackId, Integer dataType, Map<String, Object> otherArgs) {

        Map<String, Object> data = new HashMap<>(otherArgs != null ? otherArgs : new HashMap<>());
        data.put("__VIEWSTATE", "");
        data.put("__CALLBACKID", callBackId);
        data.put("__CALLBACKPARAM", constructCallBackParamData(method, params, dataType));
        return data;
    }

    // 构造 __CALLBACKPARAM 参数内容
    private static String constructCallBackParamData(String method, List<String> params, int dataType) {
        Map<String, Object> callBackParam = new HashMap<>();
        callBackParam.put("Method", method);

        if (dataType != 3) {
            callBackParam.put("Args", params);
        }
        if (dataType != 2) {
            callBackParam.put("CallControl", "{page}");
        }
        return JSON.toJSONString(callBackParam);
    }


}

