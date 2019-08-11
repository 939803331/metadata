package cn.net.metadata.base.model.core;

import cn.net.metadata.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@ApiModel(description = "数据字典表")
@Entity
@Table(name = "dictionary")
public class Dictionary extends BaseModel {
	
	private static final long serialVersionUID = -5412951243008664978L;
	
	@Basic
	@Column(name = "dict_name")
	@ApiModelProperty("字典名称")
	private String dictName;   //字典名称
	
	@Basic
	@Column(name = "dict_value")
	@ApiModelProperty("字典值")
	private String dictValue;   //字典值
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dictionary")
	private Set<DictionaryData> dictionaryDatas;
	
}
