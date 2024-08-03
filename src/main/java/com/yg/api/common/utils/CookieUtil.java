package com.yg.api.common.utils;

import com.alibaba.fastjson2.JSONObject;
import com.yg.api.common.constant.ApiConstant;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.yg.api.common.enums.UrlEnum.API;

/**
 * @ClassName CookieUtil
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/12 13:55
 */

public class CookieUtil {

    public static final ThreadLocal<String> cookie = new ThreadLocal<>();
    public static String cookieString;

    private static String formatCookie(@NotNull Map<String, Object> cookies) {
        return cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue().toString())
                .reduce((cookie1, cookie2) -> cookie1 + "; " + cookie2)
                .orElse("");
    }

    public static void getCookie(String username, String password) {
        JSONObject body = new JSONObject();
        body.put("data", new JSONObject()
                .fluentPut("account", username)
                .fluentPut("password", password)
        );
        Map<String, Object> cookies = RequestUtil.sendPost(ApiConstant.LOGIN_API, body, API).getMap("cookie");
        cookieString = formatCookie(cookies);
    }

}
