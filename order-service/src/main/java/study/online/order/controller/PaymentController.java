package study.online.order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import study.online.base.constant.ErrorMessage;
import study.online.base.exception.BaseException;
import study.online.base.utils.SecurityUtil;
import study.online.order.model.dto.AddOrderDTO;
import study.online.order.model.dto.PayRecordDTO;
import study.online.order.model.po.XcPayRecord;
import study.online.order.service.IOrderService;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

	private final IOrderService orderService;

	private final AliPayApiConfig config;

	@RequestMapping("/payment/alipay/{payNo}")
	public void alipay(@PathVariable String payNo, HttpServletResponse response) throws AlipayApiException, IOException {
		XcPayRecord record = orderService.getPayRecordByPayNo(payNo);
		if (record == null) {
			BaseException.cast(ErrorMessage.USELESS_QRCODE);
		}

		String status = record.getStatus();
		if (status.equals("601002")) {
			BaseException.cast(ErrorMessage.ERROR_ORDER_STATUS);
		}

		AliPayApiConfigKit.setThreadLocalAliPayApiConfig(config);

		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

		model.setOutTradeNo(record.getPayNo().toString());
		model.setProductCode("QUICK_WAP_WAY");
		model.setSubject(record.getOrderName());
		model.setTotalAmount(record.getTotalPrice().toString());

		String returnURL = "http://domain.com/CallBack/return_url.jsp";
		String notifyURL = "http://domain.com/CallBack/notify_url.jsp";

		AliPayApi.wapPay(response, model, returnURL, notifyURL);//直接生成表单
	}

	/**
	 * 生成支付二维码
	 * @return 订单创建信息
	 */
	@ResponseBody
	@PostMapping("/payment/generate/qrcode")
	public PayRecordDTO generatePayQrcode(@RequestBody AddOrderDTO addOrderDTO) {
		SecurityUtil.XcUser user = SecurityUtil.getUser();
		if (user == null) {
			BaseException.cast(ErrorMessage.UNKNOW_USER_WITH_CHOOSE_COURSE);
		}

		return orderService.createOrder(user.getId(), addOrderDTO);
	}
}
