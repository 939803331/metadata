package cn.net.metadata.base.repository.core;

import cn.net.metadata.base.model.core.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.repository.core
 * @date 2018/7/18 17:41
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findFirstByName(String name);
	
}
