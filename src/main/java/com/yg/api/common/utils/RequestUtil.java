package com.yg.api.common.utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Map;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;

/**
 * @author: Flora
 * @description:
 * @date: 2024/7/11 11:29
 */
public class RequestUtil {


    /**
     * 发送通用的POST请求
     *
     * @param endpoint    API端点
     * @param headers     请求头
     * @param requestBody 请求体
     * @return 响应
     */
    public Response sendPostRequest(String endpoint, Map<String, String> headers, String requestBody) {
        // 创建请求规范
        RequestSpecification request = given();

        // 设置请求头
        if (headers != null) {
            headers.forEach(request::header);
        }

        // 发送POST请求并返回响应
        return request.body(requestBody).post(endpoint);
    }
}
