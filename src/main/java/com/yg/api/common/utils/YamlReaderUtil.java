package com.yg.api.common.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName YamlReaderUtil
 * @Description TODO
 * @Author flora
 * @Date 2024/7/13 01:46
 */
public class YamlReaderUtil {
    /**
     * 读取 YAML 文件
     *
     * @param filePath 文件路径
     * @return YAML 数据
     * @throws FileNotFoundException 文件未找到异常
     * @throws YAMLException 读取 YAML 文件时发生错误
     */
    public static Map<String, Object> readYaml(String filePath) throws FileNotFoundException, YAMLException {
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new FileNotFoundException("错误：找不到文件 " + filePath);
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            return yaml.load(fileInputStream);
        } catch (IOException e) {
            throw new YAMLException("读取 YAML 文件时发生错误：" + e.getMessage(), e);
        }
    }

    /**
     * 获取 YAML 数据中指定键的值
     *
     * @param yamlData YAML 数据
     * @param keys 键路径
     * @return 指定键的值
     * @throws KeyNotFoundException 找不到指定的键路径异常
     */
    public static String getYamlValue(Map<String, Object> yamlData, String... keys) throws KeyNotFoundException {
        try {
            Object value = yamlData;
            for (String key : keys) {
                if (value instanceof Map) {
                    value = ((Map<String, Object>) value).get(key);
                } else {
                    throw new KeyNotFoundException("找不到指定的键路径：" + String.join(", ", keys));
                }
                if (value == null) {
                    throw new KeyNotFoundException("找不到指定的键路径：" + String.join(", ", keys));
                }
            }
            return Objects.toString(value, null);
        } catch (ClassCastException e) {
            throw new KeyNotFoundException("键路径类型错误：" + String.join(", ", keys), e);
        }
    }

    public static String getYamlValue(String path,String... keys) throws KeyNotFoundException, FileNotFoundException {
        Map<String, Object> stringObjectMap = readYaml(path);
        return getYamlValue(stringObjectMap,keys);
    }

    public static class KeyNotFoundException extends Exception {
        public KeyNotFoundException(String message) {
            super(message);
        }

        public KeyNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
