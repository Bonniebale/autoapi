package com.yg.api.testcase;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @ClassName ApiTest
 * @Description TODO
 * @Author flora
 * @Date 2024/7/11 22:52
 */
public class ApiTest extends BaseTest {


    @Test
    public void testApiUrl() {
    }

    @DataProvider(name = "postDataProvider")
    public Object[][] postDataProvider() {
        return new Object[][]{
                {"/api/endpoint", "{ \"key\": \"value\" }", 200, "key", "value"},
                {"/api/another-endpoint", "{ \"anotherKey\": \"anotherValue\" }", 201, "anotherKey", "anotherValue"}
        };
    }

    // @Test(dataProvider = "postDataProvider")
    // public void testPostRequest(String endpoint, String requestBody, int expectedStatusCode, String responseKey, String expectedValue) {
    //     requestBuilder.setPath(endpoint)
    //             .setRequestBody(requestBody);
    //
    //     Response response = requestBuilder.sendPostRequest();
    //
    //     ApiResponseValidator.validateResponse(response, expectedStatusCode, responseKey, expectedValue);
    // }
}

