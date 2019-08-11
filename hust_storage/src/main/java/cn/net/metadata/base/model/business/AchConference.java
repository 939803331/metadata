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
 * AchConference
 */
@Getter
@Setter
@Entity
@Table(name = "buss_ach_conference")
@ApiModel(description = "")
@org.hibernate.annotations.Table(appliesTo = "buss_ach_conference", comment = "")
public class AchConference extends BaseModel implements Serializable {
    
    private static final long serialVersionUID = -1L;

    /* 
     * 
     */@Column(name = "author_first")
    private String authorFirst;

    /* 
     * 
     */@Column(name = "title_ch")
    private String titleCh;

    /* 
     * 
     */@Column(name = "title_en")
    private String titleEn;
}