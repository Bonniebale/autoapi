package com.yg.api.common.constant;


public class ApiConstant {
    // common
    public static final String LOGIN_API = "/erp/webapi/UserApi/WebLogin/Passport";//登录
    public static final String GET_USER_INFO = "/erp/webapi/UserApi/Passport/GetUserInfo";

    // 库存
    public static final String PACK_ASPX = "/app/wms/Pack/PackItems.aspx";// 箱及仓位库存
    public static final String STOCK = "/webapi/ItemApi/ItemSkuIm/GetPageListV2";// 商品库存
    public static final String SUB_STOCK_ASPX = "/app/item/SkuStock/WmsSkuStock.aspx";// 分仓库存
    public static final String BATCH_STOCK_ASPX = "/app/wms/Pack/BatchPackItems.aspx";// 生产批次库存

    // 生产批次
    public static final String BATCH_ASPX = "/app/wms/batch/batch.aspx";
}
