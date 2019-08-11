package cn.net.metadata.base.controller.core;

import cn.net.metadata.base.model.core.Dictionary;
import cn.net.metadata.base.repository.core.DictionaryRepository;
import cn.net.metadata.base.service.core.DictionaryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.controller
 * @date 2018/7/20 12:45
 */
@RestController
@RequestMapping("dictionary")
@Api(tags = "字典管理", description = "字典管理相关API")
public class DictionaryController {
	
	@Autowired
	DictionaryService dictionaryService;
	
	@Autowired
	private DictionaryRepository dictionaryRepository;
	
	@ModelAttribute
	public Dictionary fill(@RequestParam(required = false) Long id) {
		if (id != null)
			return dictionaryRepository.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
		return new Dictionary();
	}
	
	@GetMapping("{id}")
	@PreAuthorize("hasAnyAuthority('dict:get','ROLE_ADMIN')")
	public Dictionary get(@PathVariable Long id) {
		return dictionaryRepository.findById(id).orElse(null);
	}
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('dict:list','ROLE_ADMIN')")
	public Page<Dictionary> list(Dictionary dictionary, Pageable pageable) {
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withMatcher("dictName", ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("dictValue", ExampleMatcher.GenericPropertyMatchers.contains());
		return dictionaryRepository.findAll(Example.of(dictionary, matcher), pageable);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Dictionary save(@Validated Dictionary dictionary) {
		return dictionaryService.save(dictionary);
	}
	
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Dictionary update(@Validated Dictionary dictionary) {
		return dictionaryService.update(dictionary);
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Boolean delete(@PathVariable Long id) {
		dictionaryRepository.deleteById(id);
		return true;
	}
	
}
