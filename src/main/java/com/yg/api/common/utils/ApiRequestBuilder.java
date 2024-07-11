package com.yg.api.common.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Flora
 * @description:
 * @date: 2024/7/11 16:52
 */
@Data
@Accessors(chain = true)
public class ApiRequestBuilder {

    private String path;
    private String requestBody;
    private String contentType = "application/json";
    private Map<String, String> headers = new HashMap<>();
    private String cookie;

    // 构造方法，接收 cookie 参数
    // public ApiRequestBuilder(String cookie) {
    //     this.cookie = cookie; // 将传入的 cookie 参数赋值给实例变量
    // }

    public Response sendPostRequest() {
        RequestSpecification request = RestAssured.given();
        request.contentType(contentType);
        headers.forEach(request::header);
        if (requestBody != null) {
            request.body(requestBody);
        }
        return request.post(path);
    }
}
