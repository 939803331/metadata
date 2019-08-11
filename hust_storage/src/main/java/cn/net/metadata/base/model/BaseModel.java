package cn.net.metadata.base.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.model
 * @date 2018/7/20 13:04
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", nullable = false, columnDefinition = "bigint(20)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	protected Long id;   //主键
	
	@Basic
	@Column(name = "create_by")
	@CreatedBy
	@ApiModelProperty(hidden = true)
	protected Long createBy;   //该条记录创建人ID
	
	@Basic
	@Column(name = "create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
	@CreatedDate
	@ApiModelProperty(hidden = true)
	protected Date createTime;
	
	@Basic
	@Column(name = "update_by")
	@LastModifiedBy
	@ApiModelProperty(hidden = true)
	protected Long updateBy;   //可为空
	
	@Basic
	@Column(name = "update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
	@LastModifiedDate
	@ApiModelProperty(hidden = true)
	protected Date updateTime;   //可为空
	
	@Basic
	@Column(name = "del_flag", insertable = false, columnDefinition = "tinyint default 0")
	@ApiModelProperty("是否删除、逻辑删除标识。（0：正常 1：已删除）")
	private Integer delFlag;   //0：正常 1：已删除
}
