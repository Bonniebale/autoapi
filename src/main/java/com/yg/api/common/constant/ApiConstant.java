package com.yg.api.common.constant;

/**
 * path
 */
public class ApiConstant {
    // 基础
    public static final String LOGIN_API = "/erp/webapi/UserApi/WebLogin/Passport";// 登录
    public static final String GET_USER_INFO = "/erp/webapi/UserApi/Passport/GetUserInfo";
    public static final String CUSTOM_CONFIG_ASPX = "/app/user/SettingBuilder/Builder.aspx";// 用户基础设置

    // 库存
    public static final String STOCK = "/webapi/ItemApi/ItemSkuIm/GetPageListV2";// 商品库存
    public static final String PACK_ASPX = "/app/wms/Pack/PackItems.aspx";// 箱及仓位库存
    public static final String SUB_STOCK_ASPX = "/app/item/SkuStock/WmsSkuStock.aspx";// 分仓库存
    public static final String BATCH_STOCK_ASPX = "/app/wms/Pack/BatchPackItems.aspx";// 生产批次库存

    // 采购
    public static final String CREATE_PURCHASE = "/erp/webapi/WmsApi/Purchase/CreatePurchasesV2";// 创建采购单
    public static final String GET_PURCHASE_DETAIL = "/erp/webapi/WmsApi/Purchase/LoadPurcahseItemPage";// 查询采购单明细
    public static final String PURCHASE_ASPX = "/app/scm/purchase/purchasemode.aspx";// 采购单aspx

    // 采购入库
    public static final String GET_PURCHASE_IN_ORDER_DETAIL = "/erp/webapi/WmsApi/purchasein/PurchaseInQuery";// 查询采购入库单
    public static final String PURCHASE_IN_ASPX = "/app/scm/purchasein/purchasein.aspx";// 采购入库aspx
    public static final String IN_COUNT_ASPX = "/app/wms/InCount/InCountTrial.aspx";// 采购入库+ 入库点数

    // 预约入库
    public static final String CREATE_RESERVATION_ORDER = "/erp/webapi/WmsApi/Purchase/CreatePurchaseInbookingSubBin"; // 创建预约入库单

    // 库存调拨
    public static final String CREATE_INVENTORY_TRANSFER = "/erp/webapi/WmsApi/Allocate/CreateAndConfirmAllocateOut";// 创建并审核或确认调拨出

    // 唯一码
    public static final String GET_SN_TRACKING_INFO = "/app/item/TrackingInfo/SkusnTracking.aspx";// 唯一码跟踪-查询

    // 生产批次
    public static final String BATCH_ASPX = "/app/wms/batch/batch.aspx";

}
