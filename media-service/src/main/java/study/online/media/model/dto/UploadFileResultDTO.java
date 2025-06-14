package study.online.media.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import study.online.media.model.po.MediaFiles;

@EqualsAndHashCode(callSuper = true)
@Data
public class UploadFileResultDTO extends MediaFiles {
}
