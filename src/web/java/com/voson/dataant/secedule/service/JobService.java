package com.voson.dataant.secedule.service;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.voson.dataant.store.mysql.persistence.JobPersistence;
import com.voson.dataant.store.mysql.persistence.dao.JobPersistenceDao;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class JobService {
	 
	@Autowired
	JobPersistenceDao jobPersistenceDao;

	public JobPersistence getDataantJob(Long id) {
		return jobPersistenceDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveDataantJob(JobPersistence entity) {
		jobPersistenceDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteDataantJob(Long id) {
		jobPersistenceDao.delete(id);
	}

	public List<JobPersistence> getAllDataantJob() {
		return (List<JobPersistence>) jobPersistenceDao.findAll();
	}

	public Page<JobPersistence> getDataantJob(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<JobPersistence> spec = buildSpecification(searchParams);

		return jobPersistenceDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("ruleName".equals(sortType)) {
			sort = new Sort(Direction.ASC, "ruleName");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<JobPersistence> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<JobPersistence> spec = DynamicSpecifications.bySearchFilter(filters.values(), JobPersistence.class);
		return spec;
	}

}
