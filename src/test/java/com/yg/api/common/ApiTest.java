package com.yg.api.common;

import com.yg.api.common.utils.ApiRequestBuilder;
import com.yg.api.common.utils.ApiResponseValidator;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @ClassName ApiTest
 * @Description TODO
 * @Author flora
 * @Date 2024/7/11 22:52
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApiTest extends AbstractTestNGSpringContextTests {

    @Value("${rest.assured.base.uri}")
    private String baseUri;

    @Value("${rest.assured.port}")
    private int port;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseUri;
        RestAssured.port = port;
    }

    @DataProvider(name = "postDataProvider")
    public Object[][] postDataProvider() {
        return new Object[][]{
                {"/api/endpoint", "{ \"key\": \"value\" }", 200, "key", "value"},
                {"/api/another-endpoint", "{ \"anotherKey\": \"anotherValue\" }", 201, "anotherKey", "anotherValue"}
        };
    }


    @Test()
    public void testPostRequest(String endpoint, String requestBody, int expectedStatusCode, String responseKey, String expectedValue) {
        ApiRequestBuilder requestBuilder = new ApiRequestBuilder()
                .setPath(endpoint)
                .setRequestBody(requestBody);

        Response response = requestBuilder.sendPostRequest();

        ApiResponseValidator.validateResponse(response, expectedStatusCode, responseKey, expectedValue);
    }
}
