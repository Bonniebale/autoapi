package com.yg.api.testcase;

import com.yg.api.common.DtoBuilder;
import com.yg.api.entity.InboundDto;
import com.yg.api.service.InventoryApiService;
import com.yg.api.service.PurInboundApiService;
import com.yg.api.service.UserApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

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
                {"sku1,sku2", "2", "1", "mo", "123", false, true, true},
                // {"strawberry,milk", "3", "po"}
        };
    }

    @Test(dataProvider = "inboundParam")
    public void testPostRequest(String sku, String qty, String whTypeId, String inboundType, String supplierId, Boolean needSn, Boolean needPackSn, Boolean useExternalPackSn) {

        // 将参数存入Map
        Map<String, Object> paramMap = Map.of(
                "sku", sku,
                "qty", qty,
                "whTypeId", whTypeId,
                "inboundType", inboundType,
                "supplierId", supplierId,
                "needSn", needSn,
                "needPackSn", needPackSn,
                "useExternalPackSn", useExternalPackSn
                // 添加其他参数到Map...
        );

        // 使用Map构建InboundDto对象
        // InboundDto model = DtoObjectBuilder.buildInboundDtoFromMap(paramMap);
        InboundDto model = DtoBuilder.buildDtoFromMap(InboundDto.class, paramMap);


        // 添加更多的测试逻辑或断言
        System.out.println("model: " + model);


    }

    @Test
    public void testPurRequest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        // 获取类的 Class 对象 com.yg.api.entity.InboundDto$InboundDtoBuilderImpl
        Class<?> clazz = InboundDto.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();

        // 获取指定的方法（名字为 myMethod，参数类型为 String）
        Method method = clazz.getDeclaredMethod("setInboundType", String.class);

        // 创建类的实例
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // 设置方法为可访问（因为 myMethod 是私有的）
        method.setAccessible(true);

        // 调用方法，传入实例和参数
        method.invoke(instance, "Hello, World!");

        System.out.println();
        // InboundDto inboundDto = new InboundDto();
        // inboundDto.setSku();


    }


}
