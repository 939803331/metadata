package cn.net.metadata.base.model.core;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "tables", schema = "information_schema")
public class TableEntity implements Serializable {

    private static final long serialVersionUID = -1272845948252164665L;

    @Id
    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "TABLE_COMMENT")
    private String tableComment;

}
