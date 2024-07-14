package com.yg.api.common.utils;

import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName CommonUtil
 * @Description TODO
 * @Author flora
 * @Date 2024/7/13 17:35
 */
public class CommonUtil {
    // 返回指定path
    public static String generateCrossPath(String path, Integer ownerWhId, Integer subWhId) {
        if (Objects.isNull(ownerWhId) || Objects.isNull(subWhId)) {
            return path;
        }

        return generateUrlWithArgs(path, Map.of(
                "owner_co_id", ownerWhId,
                "authorize_co_id", subWhId
        ));
    }

    // 返回带参数的url
    public static String generateUrlWithArgs(String path, Map<String, Object> args) {
        String queryParams = args.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().toString().isEmpty())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        if (!queryParams.isEmpty()) {
            path += (path.contains("?") ? "&" : "?") + queryParams;
        }

        return path;
    }

    // 生成查询条件
    public static List<Map<String, String>> generateQueryCondition(Map<String, Object> params) {
        return params.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().toString().isEmpty())
                .map(entry -> Map.of(
                        "k", entry.getKey(),
                        "v", convertToString(entry.getValue()),
                        "c", entry.getValue() instanceof List ? "@=" : "="
                ))
                .collect(Collectors.toList());
    }

    // 把list 转成"a,b,c"的形式
    private static String convertToString(Object value) {
        if (value instanceof List) {
            return ((List<?>)value).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        } else {
            return value.toString();
        }
    }

    @Test
    public void genQuer() {
        ArrayList<String> skuList = new ArrayList<>();
        skuList.add("strawberry");
        skuList.add("milk");

        Map<String, Object> params = new HashMap<>();
        params.put("sku_id", "1");
        params.put("wms_co_id", "");
        List<Map<String, String>> list = generateQueryCondition(params);
        System.out.println(list);
    }
}
