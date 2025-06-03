package com.study.online.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>课程营销信息</p>
 *
 * @author newmaster
 * @since 2025-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_market")
public class CourseMarket implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键，课程id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 收费规则，对应数据字典
     */
    @TableField("charge")
    private String charge;

    /**
     * 现价
     */
    @TableField("price")
    private Float price;

    /**
     * 原价
     */
    @TableField("original_price")
    private Float originalPrice;

    /**
     * 咨询qq
     */
    @TableField("qq")
    private String qq;

    /**
     * 微信
     */
    @TableField("wechat")
    private String wechat;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 有效期天数
     */
    @TableField("valid_days")
    private Integer validDays;


}
