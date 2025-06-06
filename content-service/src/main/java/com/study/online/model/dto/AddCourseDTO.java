package com.study.online.model.dto;

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
	private String name;

	/*适用人群*/
	private String users;

	/*课程标签*/
	private String tags;

	/*大分类*/
	private String mt;

	/*小分类*/
	private String st;

	/*课程等级*/
	private String grade;

	/*教学模式（普通，录播，直播等）*/
	private String teachmode;

	/*课程介绍*/
	private String description;

	/*课程图片*/
	private String pic;

	/*收费规则，对应数据字典*/
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
