package com.yg.api.common.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Flora
 * @description:
 * @date: 2024/7/11 16:52
 */
@Data
public class ApiRequestBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ApiRequestBuilder.class);
    private static final Map<String, String> DEFAULT_HEADERS = new HashMap<>();

    private String path;
    private Map<String, String> headers = new HashMap<>(DEFAULT_HEADERS);
    private String requestBody;
    private String cookie;

    static {
        DEFAULT_HEADERS.put("Content-Type", "application/json");
        // Add other default headers here if needed
    }

    // 构造方法，接收 cookie 参数
    public ApiRequestBuilder(String cookie) {
        this.cookie = cookie; // 将传入的 cookie 参数赋值给实例变量
    }

    public Response sendPostRequest() {
        RequestSpecification request = RestAssured.given();

        if (headers != null) {
            headers.forEach(request::header);
        }

        logger.info("Sending POST request to endpoint: {}", path);
        logger.info("Request body: {}", requestBody);

        Response response = request.body(requestBody).post(path);

        logger.info("Response: {}", response.asString());

        return response;
    }
}
