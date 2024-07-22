package com.yg.api.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName WhTypeEnum
 * @Description 仓库类型
 * @Author Flora
 * @Date 2024/7/18 14:41
 */
@Getter
@AllArgsConstructor
public enum WhTypeEnum {

    DEFAULT(1, "default", "qty"),//主仓
    RETURN(2, "return", "return_qty"),//销退仓
    IN(3, "in", "in_qty"),//进货仓
    DEFECTIVE(4, "defective", "defective_qty"),//次品仓
    CUSTOMIZE1(6, "customize_1", "customize_qty_1"),//自定义1
    PICK(100, "pick", "");//拣货仓

    private final int id;
    private final String type;
    private final String stockField;
    private static final Map<Integer, WhTypeEnum> ID_TO_ENUM_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(WhTypeEnum::getId, e -> e));


    // 根据 id 获取 stockField
    public static Optional<String> getStockFieldById(int id) {
        return Optional.ofNullable(ID_TO_ENUM_MAP.get(id)).map(WhTypeEnum::getStockField);
    }

}
