package cn.net.metadata.base.repository.core;

import cn.net.metadata.base.model.core.TableColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableColumnEntityRepository extends JpaRepository<TableColumnEntity, String> {

    @Query(value = "select COLUMN_NAME, TABLE_NAME, DATA_TYPE, COLUMN_COMMENT from information_schema.columns where table_name=:tableName order by ORDINAL_POSITION asc",
            nativeQuery = true)
    List<TableColumnEntity> findByTableName(@Param("tableName") String tableName);
}
