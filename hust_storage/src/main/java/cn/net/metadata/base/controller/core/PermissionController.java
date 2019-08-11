package cn.net.metadata.base.controller.core;

import cn.net.metadata.base.model.core.Permission;
import cn.net.metadata.base.repository.core.PermissionRepository;
import cn.net.metadata.base.service.core.PermissionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("permission")
@Api(tags = "权限管理", description = "权限管理相关API")
public class PermissionController {
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	@ModelAttribute
	public Permission fill(@RequestParam(required = false) Long id) {
		if (id != null)
			return permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
		return new Permission();
	}
	
	@GetMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Permission get(@PathVariable Long id) {
		return permissionRepository.findById(id).orElse(null);
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Page<Permission> list(Permission role, Pageable pageable) {
		return permissionRepository.findTree(pageable);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Permission save(@Validated Permission permission) {
		return permissionService.save(permission);
	}
	
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Permission update(@Validated Permission permission) {
		return permissionService.update(permission);
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Boolean delete(@PathVariable Long id) {
		permissionRepository.deleteById(id);
		return true;
	}
	
	@GetMapping("init")
	@PreAuthorize("hasRole('ADMIN')")
	public Boolean init() {
		permissionService.initPermission();
		return true;
	}
	
}
