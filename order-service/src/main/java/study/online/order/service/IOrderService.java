package study.online.order.service;

import study.online.order.model.dto.AddOrderDTO;
import study.online.order.model.dto.PayRecordDTO;
import study.online.order.model.po.XcPayRecord;

public interface IOrderService {

	/**
	 * 创建订单
	 *
	 * @param userId      用户id
	 * @param addOrderDTO 订单创建信息
	 * @return 支付记录
	 */
	PayRecordDTO createOrder(Long userId, AddOrderDTO addOrderDTO);

	XcPayRecord getPayRecordByPayNo(String payNo);
}
