package study.online.media.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 上传普通文件请求参数
 *
 * @since 2025/6/14
 * @author newmaster
 */
@Data
@Accessors(chain = true)
public class UploadFileParamsDTO {

	/*文件名*/
	private String filename;

	/*文件类型：文档；音频，视频*/
	private String fileType;

	/*文件大小*/
	private Long fileSize;

	/*标签*/
	private String tags;

	/*上传人*/
	private String username;

	/*备注*/
	private String remark;
}
