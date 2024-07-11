package com.yg.api.common.utils;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
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
     * @param url 请求地址
     * @param cookie cookie
     * @param requestBody 请求体
     * @return 响应
     */
    public static Response PostRequest(String url, JSONObject requestBody) {
        System.out.println("请求："+requestBody.toJSONString());
        RequestSpecification request = RestAssured.given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(requestBody.toJSONString());

        return request.post(url);
    }

    @Test
    public void test() {
        String url = "https://api.erp321.com/erp/webapi/UserApi/WebLogin/Passport";
        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        body.put("data",data);
        data.put("account","wms@yg.com");
        data.put("password","yaoguang20@@");
        Response response = RequestUtil.PostRequest(url, body);
        System.out.println(response.getBody().asString());
        // response.getBody().asString();
    }
}
