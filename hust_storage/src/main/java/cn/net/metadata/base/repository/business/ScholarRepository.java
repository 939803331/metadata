package cn.net.metadata.base.repository.business;

import cn.net.metadata.base.model.business.Scholar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/* 
 * ScholarRepository
 */
public interface ScholarRepository
extends JpaRepository<Scholar, Long>, JpaSpecificationExecutor<Scholar>
{
}