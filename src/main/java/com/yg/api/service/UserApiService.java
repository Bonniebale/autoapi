package com.yg.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.common.BaseInfo;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.RequestUtil;
import io.restassured.response.Response;
import org.springframework.stereotype.Service;


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
    public Response login(String username, String password) {
        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        body.put("data", data);
        data.put("account", username);
        data.put("password", password);
        return RequestUtil.doPost(BaseInfo.API_URL + ApiConstant.LOGIN_API, body);
    }

    /**
     * 获取user信息
     */
    public String getUser() {
        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        body.put("data", data);
        return RequestUtil.doPost(BaseInfo.API_URL + ApiConstant.GET_USER_INFO, body).getBody().asString();
    }


}
