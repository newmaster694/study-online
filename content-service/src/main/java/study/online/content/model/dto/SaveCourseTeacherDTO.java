package study.online.content.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import study.online.base.execption.ValidationGroup;

@Data
@ToString
public class SaveCourseTeacherDTO {

	@NotNull(message = "更新状态下id不能为空", groups = {ValidationGroup.Update.class})
	private Long id;

	@NotNull(message = "课程id不能为空", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private Long courseId;

	@NotEmpty(message = "教师名字不能为空", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String teacherName;

	@NotEmpty(message = "教师职位不能为空", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String position;

	@NotEmpty(message = "教师简介不能为空", groups = {ValidationGroup.Inster.class, ValidationGroup.Update.class})
	private String introduction;

	private String photograph;
}
