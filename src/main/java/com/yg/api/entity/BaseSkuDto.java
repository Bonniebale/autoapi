package com.yg.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @ClassName BaseSkuDto
 * @Description TODO
 * @Author Flora
 * @Date 2024/7/18 10:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseSkuDto extends BaseDto {
    // 基础信息
    private List<String> sku = List.of("nfsq");
    private int qty = 1;
    // 是否组合装
    private boolean isCombine = false;
    // 组合装子商品
    private List<String> subItems;

    // 供应商
    private int supplierId = 12324563;
    private String supplierName = "KFC";
    // 序列号/唯一码
    private String serialNumber;

    // 生产批次信息
    // 是否生产批次
    private boolean isBatch = false;
    // 生产批次号
    private String batchId;
    // 生产日期
    private String productionDate;
    // 到期日期
    private String expirationDate;
}
