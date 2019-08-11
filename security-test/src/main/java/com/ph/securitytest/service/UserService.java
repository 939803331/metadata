package com.ph.securitytest.service;

import com.ph.securitytest.entity.Permission;
import com.ph.securitytest.entity.Role;
import com.ph.securitytest.entity.User;
import com.ph.securitytest.mapper.PermissionMapper;
import com.ph.securitytest.mapper.RoleMapper;
import com.ph.securitytest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description:
 *
 * @Author: PiHao
 * Date: Created in 2019-08-10
 * Time: 9:26
 */
@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		List<User> userList = userMapper.findByUserName(userName);
		if (userList == null || userList.isEmpty()) {
			throw new UsernameNotFoundException("用户不存在");
		}
		User user = userList.get(0);
		if (!user.getEnabled()) {
			throw new DisabledException("用户被禁用");
		}

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		List<Role> roleList = roleMapper.findByUserId(user.getId());
		for (Role role : roleList) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			List<Permission> permissionList = permissionMapper.findByRoleId(role.getId());
			for (Permission permission : permissionList) {
				authorities.add(new SimpleGrantedAuthority(permission.getAuthority()));
			}
		}
		return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
	}
}
