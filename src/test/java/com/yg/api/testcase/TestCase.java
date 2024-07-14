package com.yg.api.testcase;

import com.yg.api.common.utils.YamlReaderUtil;
import com.yg.api.service.PackStockApiService;
import com.yg.api.service.UserApiService;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * @author flora
 * @Title: Test
 * @date 2024/7/11 11:19
 */

public class TestCase extends BaseTest {
    @Autowired
    UserApiService userService;
    @Autowired
    PackStockApiService packStockApiService;

    // @BeforeSuite
    // public void test() {
    //     CookieUtil.getCookie(baseUser, basePassword);
    // }

    @Test
    public void getUser() throws JsonProcessingException {
        ArrayList<String> skuList = new ArrayList<>();
        skuList.add("strawberry");
        skuList.add("milk");


        var res = packStockApiService.getStockInfo(skuList, 1);
        System.out.println(res);
    }

    @Test
    public void test2() throws FileNotFoundException, YamlReaderUtil.KeyNotFoundException {
        String path = "src/main/resources/application.yml";
        String value = YamlReaderUtil.getYamlValue(path, "base_url", "api");
        System.out.println(value);
    }

    @Test
    public void test3() {


    }
}
