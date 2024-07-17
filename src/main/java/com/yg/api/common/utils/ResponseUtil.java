package com.yg.api.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * @ClassName ResponseDataHandler
 * @Description 处理响应数据
 * @Author flora
 * @Date 2024/7/14 13:46
 */
public class ResponseUtil {

    // 截取response结果"0|"后内容
    public static String splitWith01(String data) {

        if (data != null && data.contains("0|")) {
            return data.split("0\\|", 2)[1];
        }
        return data;
    }

    /**
     * 把响应结果转成json
     *
     * @param data 响应结果
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

    /**
     * 把JSONObject 的响应数据转成JsonPath类型
     *
     * @param data 响应结果json
     */
    public static JsonPath convertToJsonPath(JSONObject data) {
        return new JsonPath(JSON.toJSONString(data));
    }

    /**
     * 处理响应数据
     *
     * @param data 响应data
     */
    public static JsonPath handleResponseData(Response data, boolean isUrlenc) {
        if (200 != data.getStatusCode()) {
            throw new RestClientException("请求失败: " + data.getStatusCode());
        }

        String responseData = data.getBody().asString();
        if (isUrlenc) {
            responseData = splitWith01(responseData);
        }
        JSONObject jsonObject = convertData(responseData);
        return convertToJsonPath(jsonObject);
    }

    //校验响应结果
    private static void validateResponse(JSONObject data, boolean isUrlenc) {
        Map<String, String> resultKey = Map.of(
                "resultCode", isUrlenc ? "IsSuccess" : "code",
                "errorMsg", isUrlenc ? "ExceptionMessage" : "msg",
                "requestId", isUrlenc ? "RequestId" : "requestId",
                "data", isUrlenc ? "ReturnValue" : "data"
        );
        var resultCode = data.get(resultKey.get("resultCode"));
        if (resultCode instanceof Boolean && !(Boolean) resultCode) {
            // false 抛出异常


        } else if (resultCode instanceof Integer) {
            int code = (Integer) resultCode;
            if (code > 0) {
                //抛出异常
            }
            if (code < 0) {
                //抛出异常

            }

        }


    }


}


