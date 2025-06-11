package study.online.content.controller;

import org.springframework.web.bind.annotation.*;
import study.online.content.model.dto.SaveTeachplanDTO;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.service.ITeachplanService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/content")
public class TeachplanController {

	@Resource
	private ITeachplanService teachplanService;

	@GetMapping("/teachplan/{courseId}/tree-nodes")
	public List<TeachplanDTO> getTreeNodes(@PathVariable Long courseId) {
		return teachplanService.findTeachplanTree(courseId);
	}

	@PostMapping("/teachplan")
	public void saveTeachplan(@RequestBody SaveTeachplanDTO saveTeachplanDTO) {
		teachplanService.saveTeachplan(saveTeachplanDTO);
	}
}
