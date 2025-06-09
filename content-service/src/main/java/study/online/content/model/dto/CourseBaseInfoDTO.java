package study.online.content.model.dto;

import study.online.content.model.po.CourseBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>课程基本信息DTO</p>
 *
 * @author newmaster
 * @since 2025/6/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CourseBaseInfoDTO extends CourseBase {

	/*收费规则，对应数据字典*/
	private String charge;

	/*价格*/
	private Float price;

	/*原价*/
	private Float originalPrice;

	/*咨询qq*/
	private String qq;

	/*微信*/
	private String wechat;

	/*电话*/
	private String phone;

	/*有效期天数*/
	private Integer validDays;

	/*大分类名称*/
	private String mtName;

	/*小分类名称*/
	private String stName;

}
