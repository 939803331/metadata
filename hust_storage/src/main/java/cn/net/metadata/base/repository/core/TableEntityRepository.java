package cn.net.metadata.base.repository.core;

import cn.net.metadata.base.model.core.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableEntityRepository extends JpaRepository<TableEntity, String> {

    @Query(value = "select TABLE_NAME, TABLE_COMMENT from information_schema.tables where table_schema=:tableSchema and TABLE_NAME like 'buss_%'",
            nativeQuery = true)
    List<TableEntity> findBussTableNames(@Param("tableSchema") String tableSchema);

    @Query(value = "select TABLE_NAME, TABLE_COMMENT from information_schema.tables where table_schema=:tableSchema and TABLE_NAME=:tableName and TABLE_NAME like 'buss_%'",
            nativeQuery = true)
    TableEntity findBussTableName(@Param("tableSchema") String tableSchema, @Param("tableName") String tableName);

}
