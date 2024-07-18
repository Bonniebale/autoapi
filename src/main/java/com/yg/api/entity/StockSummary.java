package com.yg.api.entity;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName StockSummary
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/18 14:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockSummary {
    private JSONObject stock;
    private JSONObject subStock;
    private JSONObject tempStock;
    private JSONObject binStock;
    private JSONObject comStock;
    private JSONObject batchStock;
}
