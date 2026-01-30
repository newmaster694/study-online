package study.online.learning.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author itcast
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("xc_course_tables")
public class XcCourseTables implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 选课记录id
     */
    private Long chooseCourseId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 机构id
     */
    private Long companyId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程类型
     */
    private String courseType;


    /**
     * 添加时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 开始服务时间
     */
    private LocalDateTime validtimeStart;

    /**
     * 到期时间
     */
    private LocalDateTime validtimeEnd;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

    /**
     * 备注
     */
    private String remarks;


}
