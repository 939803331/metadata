package cn.net.metadata.base.model.business;

import cn.net.metadata.base.model.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/* 
 * FieldEnable
 */
@Getter
@Setter
@Entity
@Table(name = "buss_field_enable")
@ApiModel(description = "")
@org.hibernate.annotations.Table(appliesTo = "buss_field_enable", comment = "")
public class FieldEnable extends BaseModel implements Serializable {
    
    private static final long serialVersionUID = -1L;

    /* 
     * 
     */@Column(name = "is_enabled")
    private Integer isEnabled;

    /* 
     * 
     */@Column(name = "name")
    private String name;
}