package com.yg.api.common.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    // 把[a,b,c] 转成"a,b,c"
    private static String convertToString(Object value) {
        if (value instanceof List) {
            return ((List<?>)value).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        } else {
            return value.toString();
        }
    }

    // 生成对应仓库的暂存位id
    public static String generateTempId(int companyId, int whTypeId) {
        return String.format("9%d00000%d000", companyId, whTypeId);
    }

    /**
     * 生成指定长度和字符集的string
     *
     * @param length 长度
     * @param characters 字符集，默认：大写字母和0-9数字的组合
     * @param prefixStr 指定前缀
     */
    public static String generateSpecifiedLengthStr(int length, String characters, String prefixStr) {
        if (characters == null || characters.isEmpty()) {
            characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        }

        // 使用指定前缀或当前日期时间字符串
        String prefix = (prefixStr != null) ? prefixStr : getSpecifiedDateOrTimeStr("yyyyMMddHHmmss");
        // 计算随机部分的长度
        int randomPartLength = Math.max(length - prefix.length(), 0);

        // StringBuilder randomPart = new StringBuilder(randomPartLength);
        // for (int i = 0; i < randomPartLength; i++) {
        //     randomPart.append(characters.charAt(RANDOM.nextInt(characters.length())));
        // }

        // 生成随机部分
        String randomPart = new SecureRandom().ints(randomPartLength, 0, characters.length())
                .mapToObj(characters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        return prefix + randomPart;
    }

    // 生成指定长度和字符集的string，使用默认字符集和当前时间前缀
    public static String generateSpecifiedLengthStr(int length) {
        return generateSpecifiedLengthStr(length, null, null);
    }


    /**
     * 返回指定时间的指定格式的字符串表示
     *
     * @param days 天数，正数表示未来，负数表示过去，默认值为0表示当前日期
     * @param pattern 指定时间的字符串表示，如"yyyy-MM-dd HH:mm:ss"
     */
    public static String getSpecifiedDateOrTimeStr(int days, String pattern) {
        // 创建日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.now().plusDays(days).format(formatter);
    }

    /**
     * 返回当前时间的指定格式的字符串表示
     *
     * @param pattern 指定时间的字符串表示，如"yyyy-MM-dd HH:mm:ss"
     */
    public static String getSpecifiedDateOrTimeStr(String pattern) {
        return getSpecifiedDateOrTimeStr(0, pattern);
    }


}
