package com.yg.api.common.constant;


public class ApiConstant {
    // common
    public static final String LOGIN_API = "/erp/webapi/UserApi/WebLogin/Passport";// 登录
    public static final String GET_USER_INFO = "/erp/webapi/UserApi/Passport/GetUserInfo";

    // 库存
    public static final String STOCK = "/webapi/ItemApi/ItemSkuIm/GetPageListV2";// 商品库存
    public static final String PACK_ASPX = "/app/wms/Pack/PackItems.aspx";// 箱及仓位库存
    public static final String SUB_STOCK_ASPX = "/app/item/SkuStock/WmsSkuStock.aspx";// 分仓库存
    public static final String BATCH_STOCK_ASPX = "/app/wms/Pack/BatchPackItems.aspx";// 生产批次库存

    // 生产批次
    public static final String BATCH_ASPX = "/app/wms/batch/batch.aspx";

    // 采购
    public static final String CREATE_PURCHASE = "/erp/webapi/WmsApi/Purchase/CreatePurchasesV2";// 创建采购单
    public static final String GET_PURCHASE_DETAIL = "/erp/webapi/WmsApi/Purchase/LoadPurcahseItemPage";// 查询采购单明细
    public static final String PURCHASE_ASPX = "/app/scm/purchase/purchasemode.aspx";// 采购单aspx

    // 采购入库
    public static final String GET_PURCHASE_IN_ORDER_DETAIL = "/erp/webapi/WmsApi/purchasein/PurchaseInQuery";// 查询采购入库单
    public static final String PURCHASE_IN_ASPX = "/app/scm/purchasein/purchasein.aspx";// 采购入库aspx
}
