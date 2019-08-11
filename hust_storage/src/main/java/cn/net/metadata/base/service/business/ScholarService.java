package cn.net.metadata.base.service.business;

import cn.net.metadata.base.model.business.Scholar;
import cn.net.metadata.base.repository.business.ScholarRepository;
import cn.net.metadata.base.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
 * ScholarService
 */
@Slf4j
@Service
@Transactional
public class ScholarService extends BaseService {

	public void delete(Long id) {
		scholarRepository.deleteById(id);
	}

	public Page<Scholar> find4Page(Scholar scholar, Pageable pageable) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		return scholarRepository.findAll(Example.of(scholar, exampleMatcher), pageable);
	}

	public Optional<Scholar> findById(Long id) {
		return scholarRepository.findById(id);
	}

	public Scholar save(Scholar scholar) {
		return scholarRepository.save(scholar);
	}

	public Scholar update(Scholar scholar) {
		return scholarRepository.save(scholar);
	}

	@Autowired
	private ScholarRepository scholarRepository;
}