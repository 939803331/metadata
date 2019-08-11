package cn.net.metadata.base.repository.core;

import cn.net.metadata.base.model.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.repository.core
 * @date 2018/7/18 17:40
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	Optional<User> findByUserName(String userName);
}
