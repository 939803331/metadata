package cn.net.metadata.base.model.core;

import cn.net.metadata.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "role")
@ApiModel(description = "角色表")
public class Role extends BaseModel{
	private static final long serialVersionUID = 4046224847036817428L;
	
	@Basic
	@Column(name = "name")
	@ApiModelProperty("角色标识(名称）")
	private String name;   //角色标识(名称）
	
	@Basic
	@Column(name = "name_cn")
	@ApiModelProperty("角色释义（中文名称）")
	private String nameCn;   //角色释义（中文名称）
	
	// @JsonIgnore
	// @ApiModelProperty(hidden = true)
	// @ManyToMany(mappedBy = "roles")
	// @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	// // @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	// @Fetch(FetchMode.SUBSELECT)
	// private List<User> users = new ArrayList<>();
	
	@ApiModelProperty(hidden = true)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
	@Fetch(FetchMode.SUBSELECT)
	private List<Permission> permissions;
}
