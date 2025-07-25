package study.online.api.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import study.online.api.model.po.MediaFiles;


@Data
@EqualsAndHashCode(callSuper = true)
public class UploadFileResultDTO extends MediaFiles {
}
