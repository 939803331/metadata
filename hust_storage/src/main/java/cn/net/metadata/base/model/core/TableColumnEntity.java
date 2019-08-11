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
@Table(name = "columns", schema = "information_schema")
public class TableColumnEntity implements Serializable {

    private static final long serialVersionUID = -1272845948252164665L;

    @Id
    @Column(name = "COLUMN_NAME")
    private String columnName;

    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "DATA_TYPE")
    private String dataType;

    @Column(name = "COLUMN_COMMENT")
    private String columnComment;
}
