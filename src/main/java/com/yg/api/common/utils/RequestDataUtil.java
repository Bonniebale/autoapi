package com.yg.api.common.utils;

/**
 * @ClassName RequestDataUtil
 * @Description TODO
 * @Author flora
 * @Date 2024/7/13 17:48
 */

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestDataUtil {
    public static Map<String, Object> generateReqParamACall(String method, List<Object> params, Integer dataType, Map<String, Object> otherArgs) throws JsonProcessingException {
        return injectRequestParams(method, params, "ACall1", dataType, otherArgs);
    }

    public static Map<String, Object> generateReqParamJTable(String method, List<Object> params, Integer dataType, Map<String, Object> otherArgs) throws JsonProcessingException {
        return injectRequestParams(method, params, "JTable1", dataType, otherArgs);
    }

    public static Map<String, Object> generateQueryParams(List<Map<String, String>> params) throws JsonProcessingException {
        ArrayList<Object> queryList = new ArrayList<>();
        queryList.add("1");
        queryList.addAll(params);
        queryList.add("{}");
        return generateReqParamJTable("LoadDataToJSON", queryList, 2, null);
    }

    public static Map<String, Object> injectRequestParams(String method, List<Object> params, String callBackId,
                                                          Integer dataType, Map<String, Object> otherArgs) throws JsonProcessingException {
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

    // 构造请求数据字典的通用方法
    private static Map<String, Object> constructData(String method, String callBackId,
                                                     List<Object> params, int dataType, Map<String, Object> otherArgs) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<>(otherArgs != null ? otherArgs : new HashMap<>());

        data.put("__VIEWSTATE", "");
        data.put("__CALLBACKID", callBackId);

        Map<String, Object> callBackParam = new HashMap<>();
        callBackParam.put("Method", method);

        if (dataType != 3) {
            callBackParam.put("Args", convertParamsToString(params));
        }
        if (dataType != 2) {
            callBackParam.put("CallControl", "{page}");
        }

        data.put("__CALLBACKPARAM", callBackParam);
        return data;
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

