package com.yg.api.assemblyParams;

import com.yg.api.common.utils.RequestDataHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName AssemblyCustomConfigParams
 * @Description 基础设置params
 * @Author Flora
 * @Date 2024/7/17 10:31
 */
@Component
public class AssemblyCustomConfigParams {

    // 基础设置params
    public <T> Map<String, Object> generateSetConfigParams(String key, T configValue) {
        Map<String, Object> params = Map.of(
                "key", key,
                "type", "uconfig",
                "builder_type", "enabled",
                "saved_value", configValue
        );
        return RequestDataHandler.generateReqParamACall("Save", params);
    }
}
