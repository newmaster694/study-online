package com.study.online.content.model.dto;

import com.study.online.base.execption.ValidationGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * <p>添加课程DTO</p>
 *
 * @since 2025/6/6
 * @author newmaster
 */
@Data
public class AddCourseDTO {

	/*课程名称*/
	@NotEmpty(message = "课程名不能为空哦", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String name;

	/*适用人群*/
	@NotEmpty(message = "适用人群不能为空哦", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	@Size(message = "适用人群内容太少啦", min = 4, groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String users;

	/*课程标签*/
	private String tags;

	/*大分类*/
	@NotEmpty(message = "课程大分类不能为空哦", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String mt;

	/*小分类*/
	@NotEmpty(message = "课程小分类不能为空哦", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String st;

	/*课程等级*/
	@NotEmpty(message = "课程等级不能为空哦", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String grade;

	/*教学模式（普通，录播，直播等）*/
	@NotEmpty(message = "教学模式不能为空哦", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String teachmode;

	/*课程介绍*/
	private String description;

	/*课程图片*/
	private String pic;

	/*收费规则，对应数据字典*/
	@NotEmpty(message = "收费规则不能为空哦", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String charge;

	/*价格*/
	private Float price;

	/*原价*/
	private Float originalPrice;


	/*qq*/
	private String qq;

	/*微信*/
	private String wechat;

	/*电话*/
	private String phone;

	/*有效期*/
	private Integer validDays;
}
