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
import java.time.LocalDateTime;

/**
 * <p>课程基本信息</p>
 *
 * @author newmaster
 * @since 2025-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_base")
public class CourseBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 机构ID
     */
    @TableField("company_id")
    private Long companyId;

    /**
     * 机构名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 课程名称
     */
    @TableField("name")
    private String name;

    /**
     * 适用人群
     */
    @TableField("users")
    private String users;

    /**
     * 课程标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 大分类
     */
    @TableField("mt")
    private String mt;

    /**
     * 小分类
     */
    @TableField("st")
    private String st;

    /**
     * 课程等级
     */
    @TableField("grade")
    private String grade;

    /**
     * 教育模式(common普通，record 录播，live直播等）
     */
    @TableField("teachmode")
    private String teachmode;

    /**
     * 课程介绍
     */
    @TableField("description")
    private String description;

    /**
     * 课程图片
     */
    @TableField("pic")
    private String pic;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField("change_date")
    private LocalDateTime changeDate;

    /**
     * 创建人
     */
    @TableField("create_people")
    private String createPeople;

    /**
     * 更新人
     */
    @TableField("change_people")
    private String changePeople;

    /**
     * 审核状态
     */
    @TableField("audit_status")
    private String auditStatus;

    /**
     * 课程发布状态 未发布  已发布 下线
     */
    @TableField("status")
    private String status;


}
