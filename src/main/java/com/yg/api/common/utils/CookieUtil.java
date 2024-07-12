package com.yg.api.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.yg.api.service.UserService;
import io.restassured.path.json.JsonPath;
import jakarta.annotation.Resource;

/**
 * @ClassName CookieUtil
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/12 13:55
 */
public class CookieUtil {
    @Resource
    UserService userService;

    public static String formatCookie(JSONObject cookies) {
        return cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((cookie1, cookie2) -> cookie1 + "; " + cookie2)
                .orElse("");
    }

    public String getCookie(String username, String password) {
        JsonPath login = userService.login(username, password);
        return formatCookie(login.getJsonObject("cookie"));
    }
}
