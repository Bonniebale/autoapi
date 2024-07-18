package com.yg.api.testcase;

import com.yg.api.common.enums.InTypeEnum;
import com.yg.api.entity.InboundDto;
import org.assertj.core.util.Lists;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
                {InboundDto.builder().skus(List.of("strawberry", "milk")).inboundType(InTypeEnum.BIO).deliveryNo("").build()},
                {"strawberry,milk", 1, InTypeEnum.PO}
        };
    }

    @Test(dataProvider = "inboundParam")
    public void testPostRequest(String sku, int qty, InTypeEnum inboundType) {

        InboundDto.builder()
                .skus(Lists.newArrayList(sku.split(",")))
                .qty(qty)
                .inboundType(inboundType)
                .build();


    }
}
