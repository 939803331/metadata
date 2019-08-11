package cn.net.metadata.base.controller.core;

import cn.net.metadata.base.model.core.Role;
import cn.net.metadata.base.repository.core.RoleRepository;
import cn.net.metadata.base.service.core.RoleService;
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

import java.util.ArrayList;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.controller
 * @date 2018/7/20 12:45
 */
@RestController
@RequestMapping("role")
@Api(tags = "角色管理", description = "角色管理相关API")
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@ModelAttribute
	public Role fill(@RequestParam(required = false) Long id) {
		if (id != null) {
			Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("该数据不存在，请检查对应的ID。"));
			// 如果不置空，则修改权限无效
			role.setPermissions(new ArrayList<>());
			return role;
		}
		return new Role();
	}
	
	@GetMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Role get(@PathVariable Long id) {
		return roleRepository.findById(id).orElse(null);
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Page<Role> list(Role role, Pageable pageable) {
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("createByName", ExampleMatcher.GenericPropertyMatchers.contains())
				.withMatcher("nameCn", ExampleMatcher.GenericPropertyMatchers.contains());
		return roleRepository.findAll(Example.of(role, matcher), pageable);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Role save(@Validated Role role) {
		return roleService.save(role);
	}
	
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public Role update(@Validated Role role) {
		return roleService.update(role);
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Boolean delete(@PathVariable Long id) {
		roleRepository.deleteById(id);
		return true;
	}
	
}
