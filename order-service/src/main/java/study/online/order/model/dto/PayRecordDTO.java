package study.online.order.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import study.online.order.model.po.XcPayRecord;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PayRecordDTO extends XcPayRecord {
	private String qrCode;//二维码
}
