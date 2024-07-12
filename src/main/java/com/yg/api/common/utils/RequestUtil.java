package com.yg.api.common.utils;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

/**
 * @author: Flora
 * @description:
 * @date: 2024/7/11 11:29
 */
public class RequestUtil {
    /**
     * 发送通用的POST请求
     *
     * @param url         请求地址
     * @param cookie      cookie
     * @param requestBody 请求体
     * @return 响应
     */
    public static Response postRequest(String url, JSONObject requestBody) {
        return RestAssured.given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(requestBody.toJSONString())
                .post(url);
    }

    public static JsonPath sendPost(String url, JSONObject requestBody) {
        Response response = postRequest(url, requestBody);
        return ApiResponseHandler.getResponseBodyAsJsonPath(response);
    }

    private static RequestSpecification getHeaderWithAuthType(String authType, String apiKey) {
        RequestSpecification requestSpecification = RestAssured.given()
                .header("accept", "application/json");
        return requestSpecification;

    }

//    public static JSONObject sendPost(String url, JSONObject requestBody) {
//        Response response = postRequest(url, requestBody);
//        return ApiResponseHandler.getResponseBodyAsJsonObject(response);
//    }

    @Test
    public void test() {

    }
}
