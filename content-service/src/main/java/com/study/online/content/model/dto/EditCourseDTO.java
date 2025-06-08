package com.study.online.content.model.dto;

import com.study.online.base.execption.ValidationGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditCourseDTO extends AddCourseDTO{
	/*课程id*/
	@NotNull(message = "课程id不能为空哦", groups = {ValidationGroup.Update.class})
	private Long id;
}
