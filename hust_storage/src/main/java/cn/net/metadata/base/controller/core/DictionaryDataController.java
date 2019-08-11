package cn.net.metadata.base.controller.core;

import cn.net.metadata.base.model.core.DictionaryData;
import cn.net.metadata.base.repository.core.DictionaryDataRepository;
import cn.net.metadata.base.service.core.DictionaryDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("dictionary/data")
@Api(tags = "字典值管理", description = "字典值管理相关API")
public class DictionaryDataController {
	
	@Autowired
	DictionaryDataService dictionaryDataService;
	
	@Autowired
	private DictionaryDataRepository dictionaryDataRepository;
	
	@ModelAttribute
	public DictionaryData fill(@RequestParam(required = false) Long id) {
		if (id != null)
			return dictionaryDataRepository.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
		return new DictionaryData();
	}
	
	@GetMapping("{id}")
	@PreAuthorize("hasAnyAuthority('dict:data:get','ROLE_ADMIN')")
	@Cacheable(value = "dict_data")
	public DictionaryData get(@PathVariable Long id) {
		return dictionaryDataRepository.findById(id).orElse(null);
	}
	
	@GetMapping("{dictValue}/{dictDataValue}")
	@PreAuthorize("hasAnyAuthority('dict:data:get','ROLE_ADMIN')")
	@Cacheable(value = "dict_data")
	@ApiOperation(value = "快捷检索")
	public DictionaryData getByDictDataValue(@PathVariable String dictValue, @PathVariable String dictDataValue) {
		return dictionaryDataRepository.findFirstByDictValueAndDictDataValue(dictValue, dictDataValue).orElse(null);
	}
	
	
	@GetMapping
	@PreAuthorize("hasAnyAuthority('dict:data:list','ROLE_ADMIN')")
	public Page<DictionaryData> list(DictionaryData dictionaryData, Pageable pageable) {
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withMatcher("dictDataName", ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("dictDataValue", ExampleMatcher.GenericPropertyMatchers.contains());
		return dictionaryDataRepository.findAll(Example.of(dictionaryData, matcher), pageable);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@CachePut(value = "dict_data")
	public DictionaryData save(@Validated DictionaryData dictionaryData) {
		try {
			dictionaryData = dictionaryDataService.save(dictionaryData);
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException("同一个字典的数据值不能重复。");
		}
		
		return dictionaryData;
	}
	
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	@CachePut(value = "dict_data")
	public DictionaryData update(@Validated DictionaryData dictionaryData) {
		return dictionaryDataService.update(dictionaryData);
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@CacheEvict(value = "dict_data")
	public Boolean delete(@PathVariable Long id) {
		dictionaryDataRepository.deleteById(id);
		return true;
	}
	
}
