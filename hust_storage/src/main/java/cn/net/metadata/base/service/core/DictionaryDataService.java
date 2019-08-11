package cn.net.metadata.base.service.core;

import cn.net.metadata.base.model.core.DictionaryData;
import cn.net.metadata.base.repository.core.DictionaryDataRepository;
import cn.net.metadata.base.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiaopo
 * @package cn.net.metadata.novelty.service.core
 * @date 2018/7/20 12:58
 */
@Slf4j
@Service
@Transactional
public class DictionaryDataService extends BaseService {
	
	@Autowired
	DictionaryDataRepository dictionaryDataRepository;
	
	
	public DictionaryData save(DictionaryData dictionaryData) {
		return dictionaryDataRepository.save(dictionaryData);
	}
	
	public DictionaryData update(DictionaryData dictionaryData) {
		return dictionaryDataRepository.save(dictionaryData);
	}
	
}
