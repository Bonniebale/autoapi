package com.yg.api.common;

import com.yg.api.common.enums.UrlEnum;
import com.yg.api.common.utils.YamlReaderUtil;

import java.io.FileNotFoundException;

/**
 * @ClassName BaseInfo
 * @Description TODO
 * @Author flora
 * @Date 2024/7/13 15:46
 */
public class BaseInfo {
    private static final String YAML_PATH = "src/main/resources/application.yml";
    public static String ERP_URL;
    public static String API_URL;
    public static String API_WEB_URL;
    public static String OPEN_URL_OLD;

    static {
        try {
            ERP_URL = YamlReaderUtil.getYamlValue(YAML_PATH, "url", "erp");
            API_URL = YamlReaderUtil.getYamlValue(YAML_PATH, "url", "api");
            API_WEB_URL = YamlReaderUtil.getYamlValue(YAML_PATH, "url", "api_web");
            OPEN_URL_OLD = YamlReaderUtil.getYamlValue(YAML_PATH, "url", "open_old");
        } catch (YamlReaderUtil.KeyNotFoundException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取URL地址
     *
     * @param urlType url类型
     */
    public static String getUrlByUrlType(UrlEnum urlType) {
        return switch (urlType) {
            case ERP -> BaseInfo.ERP_URL;
            case API_WEB -> BaseInfo.API_WEB_URL;
            case OPEN_OLD -> BaseInfo.OPEN_URL_OLD;
            default -> BaseInfo.API_URL;
        };
    }

}