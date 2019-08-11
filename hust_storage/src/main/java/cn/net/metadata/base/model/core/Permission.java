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

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "permission")
@ApiModel(description = "权限表")
public class Permission extends BaseModel {
    private static final long serialVersionUID = 5536573901853892963L;

    @Basic
    @Column(name = "name")
    @ApiModelProperty("权限资源名称")
    private String name;   //权限资源名称

    @Basic
    @Column(name = "authority")
    @ApiModelProperty("权限资源标识")
    private String authority;   //权限资源标识

    @Basic
    @Column(name = "url")
    @ApiModelProperty("权限资源URL")
    private String url;   //权限资源URL

    @Basic
    @Column(name = "handle")
    @ApiModelProperty("对应操作(数据字典）")
    private String handle;   //对应操作(数据字典）

    @Basic
    @Column(name = "permission_type")
    @ApiModelProperty("权限类型（menu:菜单;permission:权限）")
    private String permissionType;

    @Basic
    @Column(name = "entity_name")
    @ApiModelProperty("实体类名称")
    private String entityName;

    @OneToMany
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Permission> children;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @ManyToMany
    @JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @Cascade(value = org.hibernate.annotations.CascadeType.MERGE)
    @Fetch(FetchMode.SUBSELECT)
    private List<Role> roles = new ArrayList<>();
}



