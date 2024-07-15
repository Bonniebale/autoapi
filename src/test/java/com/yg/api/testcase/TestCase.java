package com.yg.api.testcase;

import com.alibaba.fastjson2.JSON;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.service.PackStockApiService;
import com.yg.api.service.UserApiService;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

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

    @Test
    public void getUser() throws JsonProcessingException {
        ArrayList<String> skuList = new ArrayList<>();
        skuList.add("strawberry");
        skuList.add("milk");

        packStockApiService.getStockInfo(skuList);

        // System.out.println(res);
    }

    @Test
    public void genQuer() throws JsonProcessingException {
        String url = "https://www.erp321.com/app/wms/Pack/PackItems.aspx";
        String cookie = "u_cid=133654825614281038; u_co_id=13591200; u_co_name=wms%e8%87%aa%e5%8a%a8%e5%8c%96%e6%b5%8b%e8%af%95; u_id=18137833; u_name=admin; u_lid=wms%40yg.com; u_r=11%2c12%2c13%2c14%2c23%2c27%2c34%2c39%2c40%2c41; u_shop=-1; u_ssi=; p_50=A236E64243D2B82343D29247526BAB8A638566345614284909%7c13591200; u_drp=-1; v_d_144=; j_d_3=; u_env=www; u_sso_token=CS@b0efc97815f742809f5cb060e6a5f6d3; u_json=%7b%22t%22%3a%222024-7-15+10%3a02%3a41%22%2c%22co_type%22%3a%22%e6%8a%80%e6%9c%af%e6%b5%8b%e8%af%95%22%2c%22proxy%22%3anull%2c%22ug_id%22%3a%22%22%2c%22dbc%22%3a%221650%22%2c%22tt%22%3a%2238%22%2c%22apps%22%3a%22999.170.152%22%2c%22pwd_valid%22%3a%220%22%2c%22ssi%22%3anull%2c%22sign%22%3a%224065161.61EFAF0915FD45D9BFD935737A0D3D85%2c0019488d8c5cc4274069a151b3c06625%22%7d; u_exp=1721848447";
        //{"Args": ["1", "[{"k":"[pit].sku_id","v":"nfsq","c":"like"},{"k":"[p].wh_id","v":"0","c":">"}]", "{}"], "Method": "LoadDataToJSON"} fail
        //{"Args": ["1", '[{"k":"[pit].sku_id","v":"nfsq","c":"like"},{"k":"[p].wh_id","v":"0","c":">"}]', "{}"], "Method": "LoadDataToJSON"} success

        ArrayList<String> skuList = new ArrayList<>();
        skuList.add("strawberry");
        skuList.add("milk");

        Map<String, Object> queryConditions = new HashMap<>();
        queryConditions.put("[pit].sku_id", skuList);

        var queryParam = CommonUtil.generateQueryCondition(queryConditions);//[{k=[pit].sku_id, c=@=, v=strawberry,milk}]
        System.out.println("组装好的查询参数：" + queryParam);

        String jsonString = JSON.toJSONString(queryParam);//[{"c":"@=","v":"strawberry,milk","k":"[pit].sku_id"}]
        System.out.println("第1次转json:" + jsonString);


        ArrayList<Object> queryList = new ArrayList<>();
        queryList.add("1");
        queryList.add(jsonString);
        queryList.add("{}");


        Map<String, Object> callBackParam = new HashMap<>(); //{Args=["1","[{\"k\":\"[pit].sku_id\",\"c\":\"@=\",\"v\":\"strawberry,milk\"}]","{}"], Method=LoadDataToJSON}
        callBackParam.put("Method", "LoadDataToJSON");
        callBackParam.put("Args", queryList);
        System.out.println("call:" + callBackParam);

        String jsonString2 = JSON.toJSONString(callBackParam);//{"Args":"[\"1\",\"[{\\\"k\\\":\\\"[pit].sku_id\\\",\\\"c\\\":\\\"@=\\\",\\\"v\\\":\\\"strawberry,milk\\\"}]\",\"{}\"]","Method":"LoadDataToJSON"}
        System.out.println("第2次转json:" + jsonString2);


//        String argsJson = "[{\"k\": \"[pit].sku_id\", \"v\": \"strawberry,milk\", \"c\": \"@=\"}, {\"k\": \"[p].pack_id\", \"v\": \"913591200000001000\", \"c\": \"=\"}]";
//        Map.of(
//                "__CALLBACKID", "JTable1",
//                "__CALLBACKPARAM", Map.of(
//                        "Args", List.of("1", argsJson, "{}"),
//                        "Method", "LoadDataToJSON"
//                ),
//                "__VIEWSTATE", ""
//        );
//        String sucArgs = "{\"Args\": [\"1\", '[{\"k\":\"[pit].sku_id\",\"v\":\"nfsq\",\"c\":\"like\"},{\"k\":\"[p].wh_id\",\"v\":\"0\",\"c\":\">\"}]', \"{}\"], \"Method\": \"LoadDataToJSON\"}";
//
        Response post = given()
                .header("Cookie", cookie)
                .formParam("__VIEWSTATE", "")
                .formParam("__CALLBACKID", "JTable1")
                .formParam("__CALLBACKPARAM", jsonString2)
                .post(url);

        String jsonPath = post.getBody().asString();
        System.out.println(jsonPath);

    }

    @Test
    public void test3() {


    }
}
