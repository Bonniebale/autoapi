package com.yg.api.testcase;

import com.yg.api.assertion.PIOInfoAssertion;
import com.yg.api.assertion.POInfoAssertion;
import com.yg.api.assertion.SnInfoAssertion;
import com.yg.api.common.AssertionUtil;
import com.yg.api.common.DtoBuilder;
import com.yg.api.common.StockDataVerifyUtil;
import com.yg.api.common.enums.WhTypeEnum;
import com.yg.api.common.utils.CommonUtil;
import com.yg.api.entity.InboundDto;
import com.yg.api.service.*;
import com.yg.api.service.stock.BatchApiService;
import com.yg.api.service.stock.InventoryApiService;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;

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
    @Autowired
    PurchaseOrderApiService purchaseOrderApiService;
    @Autowired
    PurInOrderApiService purInOrderApiService;
    @Autowired
    SerialNumberApiService serialNumberApiService;

    @DataProvider(name = "inboundParam")
    public static Object[][] inboundParam() {
        return new Object[][]{
                {"nfsq", "2", "1", "po", "", false, false, false},
                // {"strawberry,milk", "3", "po"}
        };
    }

    @Test(dataProvider = "inboundParam")
    public void testPostRequest(String sku, String qty, String whTypeId, String inboundType, String supplierId, Boolean needSn, Boolean needPackSn, Boolean isExternalPackSn) {

        // 将参数存入Map
        Map<String, Object> paramMap = Map.of(
                "sku", sku,
                "qty", qty,
                "whTypeId", whTypeId,
                "inboundType", inboundType,
                "supplierId", supplierId,
                "needSn", needSn,
                "needPackSn", needPackSn,
                "isExternalPackSn", isExternalPackSn
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
        Map<String, Object> orderInfo = purInboundApiService.createInboundOrder(inboundDto, inboundDto.getCompanyId(), inboundDto.getSubWhId()).getMap("ReturnValue");
        // 查询当前库存
        var currentStockMap = getMultipleStock(inboundDto);
        // 生成库存校验数据
        var verifyStockData = StockDataVerifyUtil.generateVerifyStockData(originalStockMap, currentStockMap, generateVerifyField(inboundDto));
        // 校验
        verification(inboundDto, orderInfo, verifyStockData);

    }

    /**
     * 查询库存信息（商品库存、暂存位库存、分仓库存）
     */
    private Map<String, List<Map<String, Object>>> getMultipleStock(InboundDto inboundDto) {

        // 获取 storageId，若 subWhId 不为 0 则使用 subWhId，否则使用 companyId
        int storageId = (inboundDto.getSubWhId() != 0) ? inboundDto.getSubWhId() : inboundDto.getCompanyId();
        // 查询暂存位的公司id
        Integer tempCoId = inboundDto.isActive() ? storageId : null;
        // 若 isTempBatch 为 true，则将 batchId 设置为一个包含该 batchId 的列表
        String batchId = inboundDto.isTempBatch() ? inboundDto.getBatchId() : null;

        if (inboundDto.isThirdPartyWarehouse()) {
            storageId = 0;
            tempCoId = inboundDto.getCompanyId();
        }
        return inventoryApiService.getMultipleStocks(inboundDto.getSku(), inboundDto.getWhTypeId(), Collections.singletonList(storageId),
                tempCoId, null, null, batchId, inboundDto.isActive(), null, null);
    }


    /**
     * 参数预处理
     */
    private void preprocessingParam(InboundDto inboundDto) {

        inboundDto.setActive(true);
        inboundDto.setCompanyId(userApiService.getCompanyId());

        // 生成箱唯一码
        if (inboundDto.isNeedPackSn()) {
            String packSn = inboundDto.isExternalPackSn() ? "ext" + CommonUtil.generateSpecifiedLengthStr(14) : purInboundApiService.createPackSn();
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
     * 生成需要校验的库存字段、数量
     */
    private Map<String, Map<String, Integer>> generateVerifyField(InboundDto params) {

        String stockField = WhTypeEnum.getStockFieldById(params.getWhTypeId());
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
     * 校验
     */
    private void verification(InboundDto inboundDto, Map<String, Object> order, List<Map<String, Object>> verifyStockData) {

        // 校验采购入库单字段信息
        var pioInfo = purInOrderApiService.getOrderInfo(Collections.singletonList((Integer)order.get("ioId")));
        PIOInfoAssertion.verifyFieldValue(order, pioInfo);

        // 校验库存
        AssertionUtil.batchVerifyStockInfo(verifyStockData);

        // 采购单校验
        if (Objects.equals(inboundDto.getInboundType(), "po") && inboundDto.isNeedOrder()) {
            // 明细校验
            var po_result = purchaseOrderApiService.getOrderInfo(List.of(inboundDto.getPurchaseId()));
            POInfoAssertion.verifyFieldValue(po_result, order);
        }


        // 唯一码/序列号信息校验
        if (inboundDto.isNeedSn()) {
            var snInfo = serialNumberApiService.getTrackingInfo(Collections.singletonList(inboundDto.getSerialNumber()), null);
            SnInfoAssertion.verifySnTrackingInfo(snInfo, inboundDto, order);
        }

        // 箱唯一码信息校验
        if (inboundDto.isNeedPackSn()) {
            List<String> sn = Collections.singletonList(inboundDto.getPackSn());
            var packSnInfo = serialNumberApiService.getTrackingInfo(sn, sn);
            SnInfoAssertion.verifySnTrackingInfo(packSnInfo, inboundDto, order);
        }


    }


}
