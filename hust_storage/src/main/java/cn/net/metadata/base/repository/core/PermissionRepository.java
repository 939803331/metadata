package cn.net.metadata.base.repository.core;

import cn.net.metadata.base.model.core.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.repository.core
 * @date 2018/7/18 17:42
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    void deleteByAuthorityIsNull();

    Page<Permission> findAllByAuthorityIsNull(Pageable pageable);

    @Query(value = "select * from permission where parent_id is null", nativeQuery = true)
    Page<Permission> findTree(Pageable pageable);

    Permission findByEntityNameAndPermissionType(String entityName, String permissionType);
}
