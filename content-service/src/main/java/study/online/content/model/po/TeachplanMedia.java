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
 * <p></p>
 *
 * @author newmaster
 * @since 2025-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teachplan_media")
public class TeachplanMedia implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 媒资文件id
     */
    @TableField("media_id")
    private String mediaId;

    /**
     * 课程计划标识
     */
    @TableField("teachplan_id")
    private Long teachplanId;

    /**
     * 课程标识
     */
    @TableField("course_id")
    private Long courseId;

    /**
     * 媒资文件原始名称
     */
    @TableField("media_fileName")
    private String mediaFilename;

    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 创建人
     */
    @TableField("create_people")
    private String createPeople;

    /**
     * 修改人
     */
    @TableField("change_people")
    private String changePeople;


}
