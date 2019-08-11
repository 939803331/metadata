package cn.net.metadata.base.controller.business;

import cn.net.metadata.base.model.business.Scholar;
import cn.net.metadata.base.service.business.ScholarService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*
 * ScholarController
 */
@RestController
@RequestMapping("scholar")
@Api(tags = "管理", description = "管理相关API")
public class ScholarController {

	@DeleteMapping("{id}")
	@PreAuthorize("hasAuthority('scholar:delete')")
	public Boolean delete(@PathVariable Long id) {
		scholarService.delete(id);
		return true;
	}

	@ModelAttribute
	public Scholar fill(@RequestParam(required = false) Long id) {
		if (id != null) {
			return scholarService.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
		}
		return new Scholar();
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('scholar:get')")
	public Scholar get(@PathVariable Long id) {
		return scholarService.findById(id).orElse(null);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('scholar:list')")
	public Page<Scholar> list(Scholar scholar, Pageable pageable) {
		return scholarService.find4Page(scholar, pageable);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('scholar:save')")
	public Scholar save(@Validated Scholar scholar) {
		return scholarService.save(scholar);
	}

	@PutMapping
	@PreAuthorize("hasAuthority('scholar:udpate')")
	public Scholar update(@Validated Scholar scholar) {
		return scholarService.save(scholar);
	}

	@Autowired
	private ScholarService scholarService;
}