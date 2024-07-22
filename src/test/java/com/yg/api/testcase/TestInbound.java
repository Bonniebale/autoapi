package com.yg.api.testcase;

import com.yg.api.common.AssertionUtil;
import com.yg.api.common.DtoBuilder;
import com.yg.api.common.StockDataVerifyUtil;
import com.yg.api.common.enums.WhTypeEnum;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.entity.InboundDto;
import com.yg.api.service.PurInboundApiService;
import com.yg.api.service.UserApiService;
import com.yg.api.service.stock.BatchApiService;
import com.yg.api.service.stock.InventoryApiService;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName TestInbound
 * @Description 采购入库+ case
 * @Author Flora
 * @Date 2024/7/18 10:19
 */
public class TestInbound extends BaseTest {

    @Autowired
    InventoryApiService inventoryApiService;
    @Autowired
    PurInboundApiService purInboundApiService;
    @Autowired
    UserApiService userApiService;
    @Autowired
    BatchApiService batchApiService;

    @DataProvider(name = "inboundParam")
    public static Object[][] inboundParam() {
        return new Object[][]{
                {"nfsq,milk", "2", "1", "", "", false, true, true},
                // {"strawberry,milk", "3", "po"}
        };
    }

    @Test(dataProvider = "inboundParam")
    public void testPostRequest(String sku, String qty, String whTypeId, String inboundType, String supplierId, Boolean needSn, Boolean needPackSn, Boolean useExternalPackSn) {

        // 将参数存入Map
        Map<String, Object> paramMap = Map.of(
                "sku", sku,
                "qty", qty,
                "whTypeId", whTypeId,
                "inboundType", inboundType,
                "supplierId", supplierId,
                "needSn", needSn,
                "needPackSn", needPackSn,
                "useExternalPackSn", useExternalPackSn
                // 添加其他参数到Map...
        );

        // 使用Map构建InboundDto对象
        InboundDto inboundDto = DtoBuilder.buildDtoFromMap(InboundDto.class, paramMap);
        // 步骤
        step(inboundDto);

    }

    // 步骤和校验
    public void step(InboundDto inboundDto) {

        preprocessingParam(inboundDto);
        // 查询初始库存
        var originalStockMap = getMultipleStock(inboundDto);
        // 入库点数/入库装箱
        var orderInfo = purInboundApiService.createInboundOrder(inboundDto, inboundDto.getCompanyId(), inboundDto.getSubWhId());
        // 查询当前库存
        var currentStockMap = getMultipleStock(inboundDto);
        // 校验
        verification(inboundDto, orderInfo, originalStockMap, currentStockMap);

    }

    // 查询库存信息，包括商品库存、暂存位库存、分仓库存
    private Map<String, List<Map<String, Object>>> getMultipleStock(InboundDto inboundDto) {

        // 获取 storageId，若 subWhId 不为 0 则使用 subWhId，否则使用 companyId
        int storageId = (inboundDto.getSubWhId() != 0) ? inboundDto.getSubWhId() : inboundDto.getCompanyId();
        // 查询暂存位的公司id
        Integer tempCoId = inboundDto.isActive() ? storageId : null;
        // 若 isTempBatch 为 true，则将 batchId 设置为一个包含该 batchId 的列表
        String batchIds = inboundDto.isTempBatch() ? inboundDto.getBatchId() : null;

        if (inboundDto.isThirdPartyWarehouse()) {
            storageId = 0;
            tempCoId = inboundDto.getCompanyId();
        }
        return inventoryApiService.getMultipleStocks(inboundDto.getSku(), inboundDto.getWhTypeId(), List.of(storageId),
                tempCoId, null, null, batchIds, inboundDto.isActive(), null, null);
    }

    /**
     * 生成需要校验的库存字段、数量
     */
    private Map<String, Map<String, Integer>> generateVerifyField(InboundDto params) {

        var stockField = WhTypeEnum.getStockFieldById(params.getWhTypeId());
        Map<String, Integer> fieldAndQty = Map.of(String.valueOf(stockField), params.getQty());

        Map<String, Map<String, Integer>> qtyAndField = new HashMap<>();
        qtyAndField.put("stock", fieldAndQty);
        qtyAndField.put("subStock", fieldAndQty);

        // 添加 temp stock
        if (params.isActive()) {
            qtyAndField.put("tempStock", Map.of("qty", StringUtils.isNotBlank(params.getBin()) ? 0 : params.getQty()));
        }

        // 添加 batch stock
        if (!params.isActive() && StringUtils.isNotBlank(params.getBatchId())) {
            qtyAndField.put("batchStock", Map.of("qty", params.getQty()));
        }
        return qtyAndField;
    }


    /**
     * 参数预处理
     */
    private void preprocessingParam(InboundDto inboundDto) {

        inboundDto.setActive(true);
        inboundDto.setCompanyId(userApiService.getCompanyId());
        String num = CommonUtil.generateSpecifiedLengthStr(14);

        // 生成箱唯一码
        if (inboundDto.isNeedPackSn()) {
            String packSn = inboundDto.isUseExternalPackSn() ? "ext" + num : purInboundApiService.create_pack_sn().getString("ReturnValue");
            inboundDto.setPackSn(packSn);
        }
        // 生成序列号
        if (inboundDto.isNeedSn()) {
            inboundDto.setSNs(List.of(CommonUtil.generateSpecifiedLengthStr(20)));
        }
        // 生产批次
        if (inboundDto.isBatch() && null == inboundDto.getBatchId()) {
            var batchInfo = batchApiService.generateBatchInfo();
            inboundDto.setBatchId(batchInfo.get("batchId"));
            inboundDto.setProductionDate(batchInfo.get("productionDate"));
            inboundDto.setExpirationDate(batchInfo.get("expirationDate"));
        }

        // 处理订单逻辑
        if (inboundDto.isNeedOrder()) {
            handlerInboundWithOrder(inboundDto);
        } else {
            if (Objects.equals(inboundDto.getInboundType(), "bio")) {
                // 创建预约单 - 无采购单
                // var res_params = ReservationOrderModel(skus = params.skus, planArriveQty = params.qty, supplierId = params.supplierId);
                // inboundDto.setReservationId(ReservationOrderService.create_order_by_type(2, res_params));
                System.out.println("创建预约单 - 无采购单");
            }

        }


    }

    // 处理采购入库+ 使用单号进行入库时的前置单号处理逻辑
    private void handlerInboundWithOrder(InboundDto inboundDto) {
    }

    /**
     * 校验
     */
    private void verification(InboundDto inboundDto, Object order, Map<String, List<Map<String, Object>>> originalStock, Map<String, List<Map<String, Object>>> currentStock) {
        // 生成校验字段和qty
        var verifyStockField = generateVerifyField(inboundDto);

        // 过滤数据
        if (inboundDto.isTempBatch()) {
//            String expr = JmespathUtils.generateJmespathFilterExpr(params.getBatchId(), params.getProductionDate());
//            boolean isDataCountZero = (int) ((Map<String, Object>) originalStock.get("tempStock").get("dp")).get("DataCount") == 0;
//            VerificationDataUtil.filterStockByBatch(originalStock, currentStock, TypesConstant.TEMP_STOCK, expr, isDataCountZero, 2);
        }
        // 校验库存
        var verifyStockData = StockDataVerifyUtil.generateVerifyStockData(originalStock, currentStock, verifyStockField);
        AssertionUtil.batchVerifyStockInfo(verifyStockData);


    }

    @Test
    public void testPurRequest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        // 获取类的 Class 对象 com.yg.api.entity.InboundDto$InboundDtoBuilderImpl
        Class<?> clazz = InboundDto.class;

        Method[] declaredMethods = clazz.getDeclaredMethods();

        // 获取指定的方法（名字为 myMethod，参数类型为 String）
        Method method = clazz.getDeclaredMethod("setInboundType", String.class);

        // 创建类的实例
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // 设置方法为可访问（因为 myMethod 是私有的）
        method.setAccessible(true);

        // 调用方法，传入实例和参数
        method.invoke(instance, "Hello, World!");

        System.out.println();
        // InboundDto inboundDto = new InboundDto();
        // inboundDto.setSku();


    }


}
