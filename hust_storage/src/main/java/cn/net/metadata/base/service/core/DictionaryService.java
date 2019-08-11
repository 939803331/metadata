package cn.net.metadata.base.service.core;

import cn.net.metadata.base.model.core.Dictionary;
import cn.net.metadata.base.repository.core.DictionaryRepository;
import cn.net.metadata.base.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DictionaryService extends BaseService {

    @Autowired
    DictionaryRepository dictionaryRepository;

    public Dictionary save(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    public Dictionary update(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

}
