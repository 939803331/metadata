package cn.net.metadata.base.service.core;

import cn.net.metadata.base.model.core.Role;
import cn.net.metadata.base.repository.core.RoleRepository;
import cn.net.metadata.base.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.service.core
 * @date 2018/7/20 12:58
 */
@Slf4j
@Service
@Transactional
public class RoleService extends BaseService {
	@Autowired
	RoleRepository roleRepository;
	
	@PreAuthorize("hasRole('ADMIN')")
	public Role save(Role role) {
		return roleRepository.save(role);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@CacheEvict(value = "user")
	public Role update(Role role) {
		return roleRepository.save(role);
	}
	
}
