package com.yg.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @ClassName PurchaseOrderDto
 * @Description TODO
 * @Author flora
 * @Date 2024/7/25 22:39
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class PurchaseOrderDto extends BaseSkuDto {
    private boolean isConfirm = true;
}
