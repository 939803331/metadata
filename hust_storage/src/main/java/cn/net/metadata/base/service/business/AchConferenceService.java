package cn.net.metadata.base.service.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import cn.net.metadata.base.model.business.AchConference;
import cn.net.metadata.base.repository.business.AchConferenceRepository;
import cn.net.metadata.base.service.BaseService;
import java.util.Optional;

/* 
 * AchConferenceService
 */
@Slf4j
@Service
@Transactional
public class AchConferenceService extends BaseService {
    
    public void delete(Long id)
    {
        achConferenceRepository.deleteById(id);
    }

    public Page<AchConference> find4Page(AchConference achConference, Pageable pageable)
    {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        return achConferenceRepository.findAll(Example.of(achConference, exampleMatcher), pageable);
    }

    public Optional<AchConference> findById(Long id)
    {
        return achConferenceRepository.findById(id);
    }

    public AchConference save(AchConference achConference)
    {
        return achConferenceRepository.save(achConference);
    }

    public AchConference update(AchConference achConference)
    {
        return achConferenceRepository.save(achConference);
    }

    @Autowired
    private AchConferenceRepository achConferenceRepository;
}