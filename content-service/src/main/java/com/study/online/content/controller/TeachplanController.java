package com.study.online.content.controller;

import com.study.online.content.model.dto.TeachplanDTO;
import com.study.online.content.service.ITeachplanService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
