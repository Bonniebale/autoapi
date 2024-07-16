package com.yg.api.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import io.restassured.path.json.JsonPath;

/**
 * @ClassName ResponseDataHandler
 * @Description 处理响应数据
 * @Author flora
 * @Date 2024/7/14 13:46
 */
public class ResponseDataHandler {
    public static String splitWith01(String data) {
        // 截取response结果"0|"后内容
        if (data != null && data.contains("0|")) {
            return data.split("0\\|", 2)[1];
        }
        return data;
    }

    /**
     * 把响应结果转成json
     * @param data 响应结果
     */
    public static JSONObject convertData(String data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Response data is empty.");
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
     * 把响应结果转成JsonPath
     * @param data 响应结果json
     */
    public static JsonPath convertToJsonPath(JSONObject data) {
        return new JsonPath(JSON.toJSONString(data));
    }

    /**
     * 处理响应数据
     * @param data 响应data
     */
    public static JsonPath handleResponseData(String data) {
        JSONObject jsonObject = convertData(splitWith01(data));
        return convertToJsonPath(jsonObject);
    }



}


