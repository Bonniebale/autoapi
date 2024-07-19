package com.yg.api.testcase;

import com.yg.api.common.enums.InTypeEnum;
import com.yg.api.entity.BaseDto;
import com.yg.api.entity.InboundDto;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.collections.Lists;

/**
 * @ClassName TestInbound
 * @Description 采购进仓+ 入库点数
 * @Author flora
 * @Date 2024/7/11 22:52
 */
public class TestFactory extends BaseTest {
    private InboundDto value;

    public TestFactory(InboundDto value) {
        this.value = value;
    }


    @Test
    public void testApiUrl() {
        System.out.println("value:" + value.toString());
        //用例失败重试，需要实现IRetryAnalyzer接口
    }

    @DataProvider(name = "baseDataProvider")
    public static Object[][] provideData() {
        return new Object[][]{
                {"sku1,sku2", 1, InTypeEnum.PO},
                {"sku1,sku2", 2, InTypeEnum.BIO},
        };
    }

    @Factory(dataProvider = "baseDataProvider")
    public static Object[] createInstances(String sku, int qty, InTypeEnum orderType) {
        return new Object[]{new TestFactory(
                InboundDto.builder()
                        .sku(Lists.newArrayList(sku.split(",")))
                        .qty(qty)
                        .inboundType(orderType)
                        .build()
        )};
    }


}

//    private InboundDto inboundDto;
//
//    public TestFactory(InboundDto inboundDto) {
//        this.inboundDto = inboundDto;
//    }


