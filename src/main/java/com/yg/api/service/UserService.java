package com.yg.api.service;

import com.alibaba.fastjson.JSONObject;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.RequestUtil;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;


/**
 * @ClassName UserService
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/11 16:56
 */
@Service
public class UserService {

    public JsonPath login(String username, String password) {
        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        body.put("data", data);
        data.put("account", username);
        data.put("password", password);
        return RequestUtil.sendPost(ApiConstant.LOGIN_API, body);
    }


}
