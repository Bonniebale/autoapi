package com.yg.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class ApplicationTests {

    @Value("${base_url.erp}")
    public String erpUrl;

    @Value("${base_url.api}")
    public String apiUrl;

    @Value("${base_url.api_web}")
    public String apiWebUrl;

    @Value("${base_url.open_old}")
    public String openUrl;

    @Value("${account.base_username}")
    protected String baseUser;

    @Value("${account.base_password}")
    protected String basePassword;

    @Test
    void contextLoads() {
    }

}
