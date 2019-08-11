package cn.net.metadata.base.repository.business;

import cn.net.metadata.base.model.business.FieldEnable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/* 
 * FieldEnableRepository
 */
public interface FieldEnableRepository
extends JpaRepository<FieldEnable, Long>, JpaSpecificationExecutor<FieldEnable>
{
}