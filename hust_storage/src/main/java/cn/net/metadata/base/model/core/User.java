package cn.net.metadata.base.model.core;

import cn.net.metadata.base.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "user")
@ApiModel(description = "用户表")
public class User extends BaseModel implements UserDetails {
	private static final long serialVersionUID = 8599732682582585648L;
	
	@Basic
	@Column(name = "name")
	@ApiModelProperty("用户名")
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9\u4E00-\u9FA5]{2,10}")
	private String name;   //用户姓名
	
	@Basic
	@Column(name = "code")
	@ApiModelProperty("编号")
	private String code;   //用户姓名
	
	@Basic
	@Column(name = "user_name")
	@ApiModelProperty("登录名")
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9_]{3,20}")
	private String userName;   //登录名
	
	@Basic
	@Column(name = "password")
	@ApiModelProperty("密码")
	// @NotBlank
	// @Pattern(regexp = "[a-zA-Z0-9&$/.#\\\\]{6,100}")
	private String password;   //密码
	
	@Basic
	@Column(name = "address")
	@ApiModelProperty("住址")
	@NotBlank
	private String address;   //住址
	
	@Basic
	@Column(name = "contact")
	@ApiModelProperty("联系电话")
	@NotBlank
	private String contact;   //联系电话
	
	@Basic
	@Column(name = "is_enabled")
	@ApiModelProperty("是否启用")
	private boolean enabled = false;   //是否启用
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	@Fetch(FetchMode.SUBSELECT)
	private List<Role> roles = new ArrayList<>();
	
	@Override
	@Transient
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (Role role : getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			for (Permission permission : role.getPermissions()) {
				if (!StringUtils.isEmpty(permission.getAuthority()))
					authorities.add(new SimpleGrantedAuthority(permission.getAuthority()));
			}
		}
		return authorities;
	}
	
	@Transient
	@JsonIgnore
	public HashMap<String, Object> buildTokenObject() {
		HashMap<String, Object> map = new HashMap<>();
		List<String> authUrls = getRoles()
				.stream().map(role -> role.getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toList())).collect(Collectors.toList())
				.stream().flatMap(Collection::stream).collect(Collectors.toList());
		map.put("userId", getId());
		map.put("userName", getUsername());
		map.put("urls", authUrls);
		map.put("role", getRoles().stream().map(Role::getName).collect(Collectors.joining(",")));
		return map;
	}
	
	@Transient
	@JsonIgnore
	public List<String> getPermissionList() {
		return getRoles()
				.stream().map(role -> role.getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toList()))
				.collect(Collectors.toList())
				.stream().flatMap(Collection::stream).filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList());
	}
	
	
	@Override
	public String getUsername() {
		return userName;
	}
	
	@Override
	@ApiModelProperty(hidden = true)
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	@ApiModelProperty(hidden = true)
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	@ApiModelProperty(hidden = true)
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
}
