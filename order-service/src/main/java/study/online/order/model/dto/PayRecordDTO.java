package study.online.order.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import study.online.order.model.po.XcPayRecord;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class PayRecordDTO extends XcPayRecord {
	private String qrCode;//二维码
}
