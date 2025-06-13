package study.online.system.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.online.system.model.po.Dictionary;
import study.online.system.service.IDictionaryService;

import java.util.List;

@Slf4j
@RestController
public class DictionaryController {

	@Resource
	private IDictionaryService dictionaryService;

	@GetMapping("/dictionary/all")
	public List<Dictionary> queryAll() {
		return dictionaryService.queryAll();
	}

	@GetMapping("/dictionary/code/{code}")
	public Dictionary getByCode(@PathVariable String code) {
		return dictionaryService.getByCode(code);
	}
}
