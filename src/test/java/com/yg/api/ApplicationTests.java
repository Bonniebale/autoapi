package com.yg.api;

// import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;


// @Component
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class ApplicationTests extends AbstractTestNGSpringContextTests {


    @Value("${account.base_username}")
    protected String baseUser;

    @Value("${account.base_password}")
    protected String basePassword;

    @Value("${account.batch_user}")
    protected String batchUser;

    @Value("${account.non_refined_user}")
    protected String nonReUser;

    @Value("${account.third_party_warehouse}")
    protected String thirdUser;

    @Value("${account.third_party_warehouse_v2}")
    protected String thirdUserV2;

    @Test
    void contextLoads() {
        System.out.println(baseUser);
    }

}
