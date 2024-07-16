package com.yg.api.common.utils;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.common.BaseInfo;
import com.yg.api.common.constant.ApiConstant;
import io.restassured.http.ContentType;

import java.util.Map;

/**
 * @ClassName CookieUtil
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/12 13:55
 */

public class CookieUtil {
    // public static final ThreadLocal<Map<String, String>> cookie = new ThreadLocal<>();
    // public static Map<String, String> cookie = null;
    public static final ThreadLocal<String> cookie = new ThreadLocal<>();
    public static String cookieString = null;


    public static String formatCookie(Map<String, String> cookies) {
        return cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((cookie1, cookie2) -> cookie1 + "; " + cookie2)
                .orElse("");
    }

    // public static String getCookie111(String username, String password) {
    //     UserApiService userApiService = new UserApiService();
    //     var cookies = userApiService.login(username, password).getCookies();
    //
    //     String cookieStr = CookieUtil.formatCookie(cookies);
    //     cookie.set(cookieStr);
    //     return cookieStr;
    // }

    public static void getCookie(String username, String password) {
        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        body.put("data", data);
        data.put("account", username);
        data.put("password", password);

        var cookies = RequestUtil.doPost(ContentType.JSON, BaseInfo.API_URL + ApiConstant.LOGIN_API, body.toJSONString())
                .getCookies();
        CookieUtil.cookieString = CookieUtil.formatCookie(cookies);
    }


}
