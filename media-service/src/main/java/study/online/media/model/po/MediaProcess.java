package study.online.media.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author newmaster
 * @since 2025-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("media_process")
public class MediaProcess implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /*文件标识*/
    private String fileId;

    /*文件名称*/
    private String filename;

    /*存储桶*/
    private String bucket;

    /*存储路径*/
    private String filePath;

    /*状态,1:未处理，2：处理成功  3处理失败*/
    private String status;

    /*上传时间*/
    private LocalDateTime createDate;

	/*任务开始时间*/
	private LocalDateTime startTime;

    /*完成时间*/
    private LocalDateTime finishDate;

    /*失败次数*/
    private Integer failCount;

    /*媒资文件访问地址*/
    private String url;

    /*失败原因*/
    private String errormsg;


}
