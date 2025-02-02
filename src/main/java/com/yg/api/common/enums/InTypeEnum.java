package com.yg.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName InTypeEnum
 * @Description 入库类型enum
 * @Author Flora
 * @Date 2024/7/18 10:56
 */
@Getter
@AllArgsConstructor
public enum InTypeEnum {
    PO("po", "采购入库"),
    MO("mo", "加工入库"),
    BIO("bio", "预约入库"),
    QM_WMS("QimenWms", "奇门WMS"),
    LID("lid", "物流单号入库");

    private final String value;
    private final String desc;

}
