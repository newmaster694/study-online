package study.online.order.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddOrderDTO {

	private Float totalPrice;//总价格
	private String orderType;//订单类型
	private String orderName;//订单名称
	private String orderDescrip;//订单描述
	private String orderDetail;//订单类型；JSON字符串
	private String outBusinessId;//外部业务系统id

}
