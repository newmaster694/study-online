package study.online.media.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import study.online.media.model.po.MediaFiles;

@Data
@EqualsAndHashCode(callSuper = true)
public class UploadFileResultDTO extends MediaFiles {
}
