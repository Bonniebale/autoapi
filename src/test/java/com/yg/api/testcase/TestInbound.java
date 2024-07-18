package com.yg.api.testcase;

import com.yg.api.entity.InboundDto;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TestInbound
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/18 10:19
 */
public class TestInbound extends BaseTest {

    @DataProvider(name = "inboundParam")
    public Object[][] inboundParam() {
        return new Object[][]{
                {InboundDto.builder().skus(List.of("strawberry", "milk")).inboundType("").deliveryNo("").build()},
                {InboundDto.builder()}
        };
    }

    @Test(dataProvider = "inboundParam")
    public void testPostRequest(InboundDto inboundDto) {

        List<String> skus = inboundDto.getSkus();


    }
}
