package cn.net.metadata.base.model.core;

import cn.net.metadata.base.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Getter
@Setter
@ApiModel(description = "数据字典项目表")
@Entity
@Table(name = "dictionary_data")
public class DictionaryData extends BaseModel {
	
	private static final long serialVersionUID = -2965126618087550133L;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
	@JsonIgnore
	private Dictionary dictionary;   //字典表主键
	
	@Basic
	@Column(name = "dict_value")
	@ApiModelProperty("字典值")
	private String dictValue;   //表名
	
	@Basic
	@Column(name = "dict_data_name")
	@ApiModelProperty("字典数据名称")
	@NotBlank
	private String dictDataName;   //字典数据名称
	
	@Basic
	@Column(name = "dict_data_value")
	@ApiModelProperty("字典数据值")
	@NotBlank
	private String dictDataValue;   //字典数据值
	
	@Basic
	@Column(name = "comment")
	@ApiModelProperty("释义")
	private String comment;   //释义
	
	
}
