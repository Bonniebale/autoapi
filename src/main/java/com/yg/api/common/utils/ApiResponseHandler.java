package com.yg.api.common.utils;

import com.alibaba.fastjson.JSONObject;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: Flora
 * @description:
 * @date: 2024/7/12 10:14
 */
@Component
public class ApiResponseHandler {

    public static JSONObject getResponseBodyAsJsonObject(Response response) {
        return JSONObject.parseObject(response.getBody().asString());
    }

    //JsonPath 主要方法
    //getString(): 返回指定路径的字符串值。
    //getInt(): 返回指定路径的整数值。
    //getList(): 返回指定路径的列表。
    //getMap(): 返回指定路径的映射。
    //getObject(): 将指定路径的值转换为指定类型的 Java 对象。
    public static JsonPath getResponseBodyAsJsonPath(Response response) {
        return response.jsonPath();
    }


    /**
     * 获取响应体的字符串表示形式。
     *
     * @param response RestAssured 的 Response 对象。
     * @return 响应体的字符串表示。
     */
    public static String getResponseBodyAsString(Response response) {
        return response.getBody().asString();
    }

    /**
     * 将响应体转换为指定的 Java 对象类型。
     *
     * @param response      RestAssured 的 Response 对象。
     * @param responseClass 要转换为的 Java 类。
     * @param <T>           泛型类型参数。
     * @return 转换后的 Java 对象。
     */
    public static <T> T getResponseBodyAsObject(Response response, Class<T> responseClass) {
        return response.as(responseClass);
    }
}
