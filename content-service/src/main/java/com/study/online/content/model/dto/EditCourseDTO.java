package com.study.online.content.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditCourseDTO extends AddCourseDTO{
	/*课程id*/
	private Long id;
}
