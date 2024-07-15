package com.yg.api.common.utils;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.common.BaseInfo;
import com.yg.api.common.enums.UrlEnum;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;


/**
 * @ClassName RequestUtil
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/11 11:29
 */
public class RequestUtil {

    /**
     * 初始化header
     *
     * @param cookie cookie 信息
     */
    private static RequestSpecification requestSpec(ContentType contentType, String cookie) {
        RequestSpecBuilder specBuilder = new RequestSpecBuilder()
                .addHeader("Content-Type", contentType.toString());

        if (cookie != null) {
            specBuilder.addHeader("Cookie", cookie);
        }

        return specBuilder.build();
    }


    /**
     * 发送 POST 请求的通用逻辑
     *
     * @param contentType 请求的 Content-Type
     * @param url 请求地址
     * @param requestBody 请求体
     * @return Response
     */
    public static Response doPost(ContentType contentType, String url, JSONObject requestBody) {
        return given()
                .spec(requestSpec(contentType, CookieUtil.cookieString))
                .body(requestBody.toJSONString())// 可以是map或者POJO
                .post(url);
    }

    public static <T> Response doPost(ContentType contentType, String url, T data) {
        return given()
                .spec(requestSpec(contentType, CookieUtil.cookieString))
                .body(data)
                .post(url);
    }

    /**
     * 发送 POST 请求的通用逻辑
     */
    public static Response doPost(String url, JSONObject requestBody) {
        return doPost(ContentType.JSON, url, requestBody);
    }


    /**
     * 发送 JSON 格式的 POST 请求
     */
    public static JsonPath sendPostJson(String path, JSONObject requestBody, UrlEnum urlType) {
        String url = BaseInfo.getUrlByUrlType(urlType);
        return doPost(url + path, requestBody).jsonPath();
    }

    /**
     * 发送 x-www-form-urlencoded 格式的 POST 请求
     */
    public static Response doPostUrlenc(String url, String jsonString, String callBackId) {

        return given()
                .spec(requestSpec(ContentType.URLENC, CookieUtil.cookieString))
                .formParam("__VIEWSTATE", "")
                .formParam("__CALLBACKID", callBackId)
                .formParam("__CALLBACKPARAM", jsonString)
                .post(url);
    }

    public static JsonPath sendPostUrlenc(String path, String jsonStringParam) {
        String responseData = doPostUrlenc(BaseInfo.ERP_URL + path, jsonStringParam, "JTable1")
                .getBody().asString();
        JSONObject responseJson = ResponseDataHandler.convertData(responseData);
        return ResponseDataHandler.convertToJsonPath(responseJson);
    }

    public static String buildUrlEncodedRequestBody(Map<String, Object> params) {
        return params.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue().toString()))
                .collect(Collectors.joining("&"));
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }


}
