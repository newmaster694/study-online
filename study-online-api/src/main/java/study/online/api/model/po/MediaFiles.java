package study.online.api.model.po;

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
 * 媒资信息
 *
 * @author newmaster
 * @since 2025-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("media_files")
public class MediaFiles implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /*文件id,md5值*/
    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /*机构ID*/
    private Long companyId;

    /*机构名称*/
    private String companyName;

    /*文件名称*/
    private String filename;

    /*文件类型（图片、文档，视频）*/
    private String fileType;

    /*标签*/
    private String tags;

    /*存储目录*/
    private String bucket;

    /*存储路径*/
    private String filePath;

    /*文件id*/
    private String fileId;

    /*媒资文件访问地址*/
    private String url;

    /*上传人*/
    private String username;

    /*上传时间*/
    private LocalDateTime createDate;

    /*修改时间*/
    private LocalDateTime changeDate;

    /*状态,1:正常，0:不展示*/
    private String status;

    /*备注*/
    private String remark;

    /*审核状态*/
    private String auditStatus;

    /*审核意见*/
    private String auditMind;

    /*文件大小*/
    private Long fileSize;


}
