package cn.net.metadata.base;

import cn.net.metadata.base.repository.core.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author xiaopo
 * @package cn.net.metadata.base
 * @date 2018/10/24 17:36
 */
@Component
public class StartUpApplicationRunner implements ApplicationRunner {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 检查是否初始化了管理员账户
		// User user = userRepository.findByUserName("admin").orElse(null);
		// 初始化数据
		// if(user == null){
		// jdbcTemplate.execute(String.format("insert into user (create_by) values ()", ""));
		// }
	}
}
