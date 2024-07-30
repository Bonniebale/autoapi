package com.yg.api.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.function.Predicate;

/**
 * @ClassName ResponseUtil
 * @Description 处理响应
 * @Author flora
 * @Date 2024/7/14 13:46
 */
public class ResponseUtil {


    /**
     * 处理响应
     *
     * @param response response
     * @param isUrlenc content type 是否为urlencoded
     */
    public static JsonPath handleResponse(Response response, boolean isUrlenc) {
        if (200 != response.getStatusCode()) {
            throw new RestClientException("请求失败: " + response.getStatusCode());
        }

        String responseData = response.getBody().asString();
        JSONObject responseJson = parseResponseData(responseData, isUrlenc);
        validateResponse(responseJson, isUrlenc);

        return new JsonPath(JSON.toJSONString(responseJson));
    }

    /**
     * 校验响应结果
     */
    private static void validateResponse(JSONObject response, boolean isUrlenc) {

        Map<String, String> resultKey = Map.of(
                "resultCode", isUrlenc ? "IsSuccess" : "code",
                "errorMsg", isUrlenc ? "ExceptionMessage" : "msg",
                "requestId", isUrlenc ? "RequestId" : "requestId",
                "data", isUrlenc ? "ReturnValue" : "data"
        );

        var resultCode = response.get(resultKey.get("resultCode"));

        if (resultCode instanceof Boolean) {
            handleResult(resultCode, response, resultKey, Boolean.FALSE::equals);
        } else if (resultCode instanceof Integer) {
            handleResult(resultCode, response, resultKey, code -> (Integer) code != 0);
        }

    }


    /**
     * 解析响应数据
     */
    private static JSONObject parseResponseData(String responseData, boolean isUrlenc) {
        if (isUrlenc) {
            return convertData(splitWith01(responseData));
        } else {
            return JSON.parseObject(responseData);
        }
    }

    /**
     * 处理失败的响应结果
     */
    private static <T> void handleResult(T resultCode, JSONObject response, Map<String, String> resultKey, Predicate<T> shouldThrow) {
        if (shouldThrow.test(resultCode)) {
            String errorMsg = (String) response.get(resultKey.get("errorMsg"));
            String requestId = (String) response.get(resultKey.get("requestId"));
            String message = String.format("请求失败: %s, 错误代码: %s, 请求ID: %s", errorMsg, resultCode, requestId);
            throw new RestClientException(message);
        }
    }

    /**
     * 把数据从String转成JSONObject
     */
    public static JSONObject convertData(String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("数据不能为空");
        }
        JSONObject outerJson = JSON.parseObject(data);
        String returnValue = outerJson.getString("ReturnValue");
        // 解析 ReturnValue 字段的 JSON 字符串
        try {
            JSONObject returnValueJson = JSON.parseObject(returnValue);
            outerJson.put("ReturnValue", returnValueJson);
        } catch (JSONException e) {
            // 不是有效的 JSON，不做处理
        }
        return outerJson;
    }


    // 截取response结果"0|"后内容
    public static String splitWith01(String data) {

        if (data != null && data.contains("0|")) {
            return data.split("0\\|", 2)[1];
        }
        return data;
    }


}


