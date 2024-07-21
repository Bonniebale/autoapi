package com.yg.api.service;

import com.yg.api.assemblyParams.AssemblyCustomConfigParams;
import com.yg.api.common.constant.ApiConstant;
import com.yg.api.common.utils.RequestUtil;
import io.restassured.path.json.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CustomApiService
 * @Description 用户设置
 * @Author Flora
 * @Date 2024/7/17 10:23
 */
@Service
public class CustomApiService extends BaseApiService {

    @Autowired
    AssemblyCustomConfigParams assemblyCustomConfigParams;

    private static final Map<Integer, String> CUSTOM_CONFIG_KEYS = new HashMap<>();

    static {
        // 唯一码配置
        CUSTOM_CONFIG_KEYS.put(1, "sku.sn");// 是否开启唯一码
        CUSTOM_CONFIG_KEYS.put(2, "wms.seriesnumber.sku");// 是否开启序列号
        CUSTOM_CONFIG_KEYS.put(3, "sku.sn.controlstatus");// 设置唯一码管控状态  1 弱 2 中 3 强
        CUSTOM_CONFIG_KEYS.put(4, "wms.pick.mustscansn");// 拣货、播种必须扫描唯一码
        CUSTOM_CONFIG_KEYS.put(5, "wms.forcescanskusn.pack");// 仓内操作强制扫描唯一码
        CUSTOM_CONFIG_KEYS.put(6, "wms.forcescanskusn.checkout");// 验货出库强制扫描唯一码
        CUSTOM_CONFIG_KEYS.put(7, "pack.sku.sn");// 箱唯一码流程
        // 生产批次配置
        CUSTOM_CONFIG_KEYS.put(10, "wms.produced.batch");// 是否开启生产批次
        CUSTOM_CONFIG_KEYS.put(11, "wms.produced.batchbybin");// 是否开启拣货区生产批次
        CUSTOM_CONFIG_KEYS.put(12, "produced.batchdefaultpack"); // 是否开启暂存位生产批次
        CUSTOM_CONFIG_KEYS.put(13, "producedbin.skubatchrule");// 设置仓位生产批次, 单批次1, 多批次2
        CUSTOM_CONFIG_KEYS.put(14, "wms.produced.batchtailafter");// 是否开启进出仓单据跟踪生产批次
        CUSTOM_CONFIG_KEYS.put(15, "wms.vs.pick");// 拣货暂存位禁止负库存
    }

    public static String getCustomConfigKey(int configType) {
        return CUSTOM_CONFIG_KEYS.get(configType);
    }

    //设置基础设置
    public <T> JsonPath setConfig(int configType, T configValue) {
        String key = getCustomConfigKey(configType);
        var data = assemblyCustomConfigParams.generateSetConfigParams(key, configValue);
        return RequestUtil.sendPostUrlenc(ApiConstant.CUSTOM_CONFIG_ASPX, data);
    }

}
