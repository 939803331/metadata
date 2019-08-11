package cn.net.metadata.base.service.core;

import cn.net.metadata.base.model.core.Role;
import cn.net.metadata.base.model.core.User;
import cn.net.metadata.base.repository.core.RoleRepository;
import cn.net.metadata.base.repository.core.UserRepository;
import cn.net.metadata.base.service.BaseService;
import cn.net.metadata.base.utility.MDStringUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.service.core
 * @date 2018/7/18 18:04
 */
@Service
@Transactional
public class UserService extends BaseService implements UserDetailsService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    // @Cacheable(value = "user", key = "#username")
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在。"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        return userRepository.save(user);
    }


    public User register(User user) {
        // preInsert(user, token);
        user.setCreateBy(-1L);
        user.setCreateTime(new Date());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setEnabled(true);
        // 设置默认用户角色
        Role role = roleRepository.findFirstByName("ROLE_USER").orElseThrow(() -> new RuntimeException("未获取到默认USER角色，请联系管理员。"));
        user.setRoles(Lists.newArrayList(role));
        return userRepository.save(user);
    }

    @CacheEvict(value = "user", key = "#user.username")
    public User update(User user) {
        // 修改密码
        if (MDStringUtils.isNotBlank(user.getPassword()) && !user.getPassword().contains("$"))
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }


}
