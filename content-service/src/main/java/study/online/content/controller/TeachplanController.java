package study.online.content.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import study.online.content.model.dto.BindTeachPlanMediaDTO;
import study.online.content.model.dto.SaveTeachplanDTO;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.model.po.TeachplanMedia;
import study.online.content.service.ITeachplanService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/teachplan")
public class TeachplanController {

	@Resource
	private ITeachplanService teachplanService;

	/**
	 * 查询课程计划树结构
	 *
	 * @param courseId 课程id
	 * @return {@code List<TeachplanDTO>}
	 */
	@GetMapping("/{courseId}/tree-nodes")
	public List<TeachplanDTO> getTreeNodes(@PathVariable Long courseId) {
		return teachplanService.findTeachplanTree(courseId);
	}

	/**
	 * 新增/修改课程计划
	 *
	 * @param saveTeachplanDTO 课程计划信息
	 */
	@PostMapping
	public void saveTeachplan(@RequestBody SaveTeachplanDTO saveTeachplanDTO) {
		teachplanService.saveTeachplan(saveTeachplanDTO);
	}

	/**
	 * 删除课程计划
	 *
	 * @param teachplanId 课程计划id
	 */
	@DeleteMapping("/{teachplanId}")
	public void deleteTeachplan(@PathVariable Long teachplanId) {
		teachplanService.deleteTeachplan(teachplanId);
	}

	/**
	 * 章节的上移与下移操作接口
	 *
	 * @param moveType    动作，上移还是下移
	 * @param teachplanId 课程计划id
	 */
	@PostMapping("/{moveType}/{teachplanId}")
	public void move(@PathVariable String moveType, @PathVariable Long teachplanId) {
		teachplanService.move(moveType, teachplanId);
	}

	/**
	 * 课程计划绑定媒资文件
	 *
	 * @param bindTeachPlanMediaDTO 绑定记录相关DTO
	 * @return {@code TeachplanMedia}
	 */
	@PostMapping("/association/media")
	public TeachplanMedia associationMedia(@RequestBody BindTeachPlanMediaDTO bindTeachPlanMediaDTO) {
		return teachplanService.associationMedia(bindTeachPlanMediaDTO);
	}

	@DeleteMapping("/association/media/{teachplanId}/{mediaId}")
	public void unbindMedia(@PathVariable Long teachplanId, @PathVariable String mediaId) {
		teachplanService.unbindMedia(teachplanId, mediaId);
	}
}
