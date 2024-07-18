package com.yg.api.entity;

import com.yg.api.common.enums.InTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @ClassName InboundDTO
 * @Description TODO
 * @Author flora
 * @Date 2024/7/16 22:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class InboundDto extends BaseSkuDto {
    /**
     * 接口字段
     */
    // 订单单号(采购单、预约单、加工单、物流单号等)
    private int orderId;
    // 箱号
    private String packId;
    // 箱唯一码
    private String packSn;
    // 箱数
    private int packQty;
    // 送货单号
    private String deliveryNo;
    // 进仓类型
    // PO/1 :采购入库, MO/2:加工入库库, BIO/4:预约入库, QimenWms/5:奇门WMS, LID/6:物流单号
    private InTypeEnum inboundType = InTypeEnum.PO;
    // 是否进仓装箱
    private boolean inByPack = false;
    // 是否批量装箱
    private boolean isBulkPack = false;

    // 商品信息
    // 唯一码
    private List<String> serialNumber;

    /**
     * 自定义字段
     */

    // 预约单单号
    private int reservationId;
    // 采购单单号
    private int purchaseId;
    // 是否超入采购单
    private boolean isOverRated = false;
    // 采购单允许超入比例
    private int overRate;
    // 采购数量
    private int purchaseQty;
    // 物流单号
    private String logisticsId;
    // 加工单单号
    private int manufactureId;
    // 是否需要箱唯一码
    private boolean needPackSn = false;
    // 是否使用外部箱码
    private boolean useExternalPackSn = false;
    // 是否需要唯一码/序列号
    private boolean needSn = false;
    // 是否需要单号入库
    private boolean needOrder = false;
    // 是否需要送货单号
    private boolean needDeliveryNo = false;
    // 是否开启暂存位生产批次
    private boolean tempBatch = false;
    // 货位
    private String bin;
}
