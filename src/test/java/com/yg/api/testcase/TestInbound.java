package com.yg.api.testcase;

import com.yg.api.common.enums.InTypeEnum;
import com.yg.api.entity.InboundDto;
import com.yg.api.service.InventoryApiService;
import com.yg.api.service.PurInboundApiService;
import com.yg.api.service.UserApiService;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @ClassName TestInbound
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/18 10:19
 */
public class TestInbound extends BaseTest {
    @Autowired
    InventoryApiService inventoryApiService;
    @Autowired
    PurInboundApiService purInboundApiService;
    @Autowired
    UserApiService userApiService;

    @DataProvider(name = "inboundParam")
    public static Object[][] inboundParam() {
        return new Object[][]{
                {"sku1,sku2", "1", "mo"},
                {"strawberry,milk", "", "po"}
        };
    }

    @Test(dataProvider = "inboundParam")
    public void testPostRequest(String sku, String qty, String inboundType) {

        InboundDto model = InboundDto.builder().build();

        if(StringUtils.isNotBlank(sku)){
            model.setSku(Lists.newArrayList(sku.split(",")));
        }
        if(StringUtils.isNotBlank(qty)){
            model.setQty(Integer.parseInt(qty));
        }
        if(StringUtils.isNotBlank(inboundType)){
            model.setInboundType(InTypeEnum.valueOf(inboundType));
        }
        if(StringUtils.isNotBlank(qty)){
            model.setQty(Integer.parseInt(qty));
        }

        if(StringUtils.isNotBlank(qty)){
            model.setQty(Integer.parseInt(qty));
        }
        if(StringUtils.isNotBlank(qty)){
            model.setQty(Integer.parseInt(qty));
        }
        if(StringUtils.isNotBlank(qty)){
            model.setQty(Integer.parseInt(qty));
        }



    }
}
