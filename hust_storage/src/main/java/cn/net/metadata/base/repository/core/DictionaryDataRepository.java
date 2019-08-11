package cn.net.metadata.base.repository.core;

import cn.net.metadata.base.model.core.DictionaryData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.service.core
 * @date 2018/7/24 16:21
 */
public interface DictionaryDataRepository extends JpaRepository<DictionaryData, Long> {
	
	Optional<DictionaryData> findFirstByDictValueAndDictDataValue(String dictValue, String dictDataValue);
	
}
