package study.online.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.online.base.constant.ErrorMessage;
import study.online.base.exception.BaseException;
import study.online.order.mapper.XcOrdersGoodsMapper;
import study.online.order.mapper.XcOrdersMapper;
import study.online.order.mapper.XcPayRecordMapper;
import study.online.order.model.dto.AddOrderDTO;
import study.online.order.model.dto.PayRecordDTO;
import study.online.order.model.po.XcOrders;
import study.online.order.model.po.XcOrdersGoods;
import study.online.order.model.po.XcPayRecord;
import study.online.order.service.IOrderService;
import study.online.order.utils.QRCodeUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

	private final XcOrdersMapper ordersMapper;
	private final XcOrdersGoodsMapper ordersGoodsMapper;
	private final XcPayRecordMapper payRecordMapper;

	@Value("pay.qrcode-url")
	private String qrcodeURL;

	@Override
	@Transactional
	public PayRecordDTO createOrder(Long userId, AddOrderDTO addOrderDTO) {
		//添加商品订单
		XcOrders order = this.saveOrder(userId, addOrderDTO);

		//添加支付记录
		XcPayRecord record = this.createPayRecord(order);

		//生成支付二维码
		String qrcode = null;
		try {
			String url = String.format(qrcodeURL, record.getPayNo());
			qrcode = new QRCodeUtil().createQRCode(url, 200, 200);
		} catch (IOException exception) {
			BaseException.cast(ErrorMessage.ERROR_GENERATE_QRCODE);
		}

		PayRecordDTO payRecordDTO = BeanUtil.copyProperties(record, PayRecordDTO.class);
		payRecordDTO.setQrCode(qrcode);

		return payRecordDTO;
	}

	@Override
	public XcPayRecord getPayRecordByPayNo(String payNo) {
		LambdaQueryWrapper<XcPayRecord> queryWrapper = new LambdaQueryWrapper<XcPayRecord>()
			.eq(XcPayRecord::getPayNo, Long.parseLong(payNo));

		return payRecordMapper.selectOne(queryWrapper);
	}

	/**
	 * 创建支付记录
	 */
	private XcPayRecord createPayRecord(XcOrders orders) {
		if (orders == null) {
			BaseException.cast(ErrorMessage.EMPTY_ORDER);
		}

		if (orders.getStatus().equals("600002")) {
			BaseException.cast(ErrorMessage.ERROR_ORDER_STATUS);
		}

		Long payNo = IdUtil.getSnowflakeNextId();//生成支付交易流水号

		XcPayRecord record = XcPayRecord.builder()
			.payNo(payNo)
			.orderId(orders.getId())
			.orderName(orders.getOrderName())
			.totalPrice(orders.getTotalPrice())
			.currency("CNY")
			.createDate(LocalDateTime.now())
			.status("601001")//未支付
			.userId(orders.getUserId())
			.build();

		payRecordMapper.insert(record);
		return record;
	}

	/**
	 * 创建订单
	 */
	private XcOrders saveOrder(Long userId, AddOrderDTO addOrderDTO) {
		//幂等性处理
		XcOrders order = this.getOrderByBusinessId(addOrderDTO.getOutBusinessId());
		if (order != null) {
			return order;
		}

		Long orderId = IdUtil.getSnowflakeNextId();//生成订单id

		order = XcOrders.builder()
			.id(orderId)
			.totalPrice(addOrderDTO.getTotalPrice())
			.createDate(LocalDateTime.now())
			.status("600001")//未支付
			.userId(userId)
			.orderType(addOrderDTO.getOrderType())
			.orderName(addOrderDTO.getOrderName())
			.orderDetail(addOrderDTO.getOrderDetail())
			.orderDescrip(addOrderDTO.getOrderDescrip())
			.outBusinessId(addOrderDTO.getOutBusinessId())//选课记录id
			.build();

		ordersMapper.insert(order);

		String orderDetailJson = addOrderDTO.getOrderDetail();
		List<XcOrdersGoods> ordersGoodsList = JSON.parseArray(orderDetailJson, XcOrdersGoods.class);

		ordersGoodsList.forEach(item -> {
			item.setOrderId(orderId);
			ordersGoodsMapper.insert(item);
		});

		return order;
	}

	/**
	 * 根据业务系统id查询订单
	 */
	private XcOrders getOrderByBusinessId(String businessId) {
		LambdaQueryWrapper<XcOrders> queryWrapper = new LambdaQueryWrapper<XcOrders>()
			.eq(XcOrders::getOutBusinessId, businessId);

		return ordersMapper.selectOne(queryWrapper);
	}
}
