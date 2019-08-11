package cn.net.metadata.base.repository.business;

import cn.net.metadata.base.model.business.AchConference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/* 
 * AchConferenceRepository
 */
public interface AchConferenceRepository
extends JpaRepository<AchConference, Long>, JpaSpecificationExecutor<AchConference>
{
}