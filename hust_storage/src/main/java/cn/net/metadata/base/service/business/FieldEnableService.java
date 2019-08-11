package cn.net.metadata.base.service.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import cn.net.metadata.base.model.business.FieldEnable;
import cn.net.metadata.base.repository.business.FieldEnableRepository;
import cn.net.metadata.base.service.BaseService;
import java.util.Optional;

/* 
 * FieldEnableService
 */
@Slf4j
@Service
@Transactional
public class FieldEnableService extends BaseService {
    
    public void delete(Long id)
    {
        fieldEnableRepository.deleteById(id);
    }

    public Page<FieldEnable> find4Page(FieldEnable fieldEnable, Pageable pageable)
    {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        return fieldEnableRepository.findAll(Example.of(fieldEnable, exampleMatcher), pageable);
    }

    public Optional<FieldEnable> findById(Long id)
    {
        return fieldEnableRepository.findById(id);
    }

    public FieldEnable save(FieldEnable fieldEnable)
    {
        return fieldEnableRepository.save(fieldEnable);
    }

    public FieldEnable update(FieldEnable fieldEnable)
    {
        return fieldEnableRepository.save(fieldEnable);
    }

    @Autowired
    private FieldEnableRepository fieldEnableRepository;
}