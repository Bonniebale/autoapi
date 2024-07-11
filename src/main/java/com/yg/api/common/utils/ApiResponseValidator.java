package com.yg.api.common.utils;

import io.restassured.response.Response;

import static org.testng.Assert.assertEquals;

/**
 * @ClassName ApiResponseValidator
 * @Description TODO
 * @Author flora
 * @Date 2024/7/11 23:03
 */
public class ApiResponseValidator {
    public static void validateResponse(Response response, int expectedStatusCode, String responseKey, String expectedValue) {
        assertEquals(response.statusCode(), expectedStatusCode);
        assertEquals(response.jsonPath().getString(responseKey), expectedValue);
    }
}
