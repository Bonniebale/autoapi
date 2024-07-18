package com.yg.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName WhTypeEnum
 * @Description 仓库类型
 * @Author Flora
 * @Date 2024/7/18 14:41
 */
@Getter
@AllArgsConstructor
public enum WhTypeEnum {
    DEFAULT(1, "default"),//主仓
    RETURN(2, "return"),//销退仓
    IN(3, "in"),//进货仓
    DEFECTIVE(4, "defective"),//次品仓
    PICK(100, "pick");//拣货仓

    private final int id;
    private final String type;
}
