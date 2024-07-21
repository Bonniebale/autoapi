package com.yg.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.RequestUtil;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import static com.yg.api.common.enums.UrlEnum.API;


/**
 * @ClassName UserApiService
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/11 16:56
 */
@Service
public class UserApiService {

    /**
     * 登录
     */
    public JsonPath login(String username, String password) {
        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        body.put("data", data);
        data.put("account", username);
        data.put("password", password);
        return RequestUtil.sendPost(ApiConstant.LOGIN_API, body, API);
    }

    /**
     * 获取user信息
     */
    public JsonPath getUser() {
        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        body.put("data", data);
        return RequestUtil.sendPost(ApiConstant.GET_USER_INFO, body, API);
    }

    /**
     * 获取登录用户的company Id
     */
    public int getCompanyId() {
        return getUser().getInt("data.coId");
    }


}
