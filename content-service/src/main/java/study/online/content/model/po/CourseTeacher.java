package study.online.content.model.po;

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
 * <p>课程-教师关系表</p>
 *
 * @author newmaster
 * @since 2025-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_teacher")
public class CourseTeacher implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 课程标识
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 教师标识
     */
    @TableField("teacher_name")
    private String teacherName;

    /**
     * 教师职位
     */
    @TableField("position")
    private String position;

    /**
     * 教师简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 照片
     */
    @TableField("photograph")
    private String photograph;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;


}
