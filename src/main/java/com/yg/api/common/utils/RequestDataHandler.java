package com.yg.api.common.utils;

/**
 * @ClassName RequestDataUtil
 * @Description TODO
 * @Author flora
 * @Date 2024/7/13 17:48
 */

import com.alibaba.fastjson.JSON;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestDataHandler {
    public static Map<String, Object> generateReqParamACall(String method, List<Object> params, Integer dataType, Map<String, Object> otherArgs) {
        return injectRequestParams(method, params, "ACall1", dataType, otherArgs);
    }

    public static Map<String, Object> generateReqParamJTable(String method, List<Object> params, Integer dataType, Map<String, Object> otherArgs) {
        return injectRequestParams(method, params, "JTable1", dataType, otherArgs);
    }

    public static String generateQueryParams(List<Map<String, String>> params) {
        ArrayList<Object> queryList = new ArrayList<>();
        queryList.add("1");
        queryList.add(JSON.toJSONString(params));
        queryList.add("{}");
        return constructCallBackParamData("LoadDataToJSON", queryList, 2);
    }

    public static Map<String, Object> injectRequestParams(String method, List<Object> params, String callBackId,
                                                          Integer dataType, Map<String, Object> otherArgs) {
        return constructData(method, callBackId, params, dataType, otherArgs);
    }

    // 将注入对象转换为字符串的方法
    public static String convertToString(Object injectObj) throws JsonProcessingException {
        if (injectObj instanceof Integer || injectObj instanceof String) {
            return injectObj.toString();
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(injectObj);
    }

    // 构造请求数据
    private static Map<String, Object> constructData(String method, String callBackId,
                                                     List<Object> params, int dataType, Map<String, Object> otherArgs) {
        Map<String, Object> data = new HashMap<>(otherArgs != null ? otherArgs : new HashMap<>());
        data.put("__VIEWSTATE", "");
        data.put("__CALLBACKID", callBackId);
        data.put("__CALLBACKPARAM", constructCallBackParamData(method, params, dataType));
        return data;
    }


    //构造 __CALLBACKPARAM 参数内容
    private static String constructCallBackParamData(String method, List<Object> params, int dataType) {
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

    // 将参数转换为字符串列表的方法
    private static List<String> convertParamsToString(List<Object> params) throws JsonProcessingException {
        return params.stream()
                .map(param -> {
                    try {
                        return convertToString(param);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

}

