package com.yg.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName BaseDto
 * @Description TODO
 * @Author flora
 * @Date 2024/7/16 22:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BaseDto {
    // 公司id
    private int companyId;
    // 货主公司id
    private int ownerWhId;
    // 分仓id/授权仓库id
    private int subWhId;
    // 用户id
    private int userId;
    // 是否开启精细化
    private boolean isActive = true;
    // 是否第三方仓储
    private boolean isThirdPartyWarehouse = false;
    // 仓库类型id，默认为主仓，1: 主仓, 2: 销退仓 3: 进货仓, 4: 次品仓, 6: 自定义1仓
    private int whTypeId = 1;
    // 仓库类型 default:主仓, pick:拣货仓, in:进货仓, return:销退仓, defective:次品仓
    private String warehouseType;
}
