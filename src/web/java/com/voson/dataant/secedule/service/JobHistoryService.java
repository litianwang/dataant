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

import com.voson.dataant.store.mysql.persistence.JobHistoryPersistence;
import com.voson.dataant.store.mysql.persistence.dao.JobHistoryPersistenceDao;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class JobHistoryService {
	 
	@Autowired
	JobHistoryPersistenceDao jobHistoryPersistenceDao;

	public JobHistoryPersistence getDataantJobHistory(Long id) {
		return jobHistoryPersistenceDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveDataantJobHistory(JobHistoryPersistence entity) {
		jobHistoryPersistenceDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteDataantJobHistory(Long id) {
		jobHistoryPersistenceDao.delete(id);
	}

	public List<JobHistoryPersistence> getAllDataantJobHistory() {
		return (List<JobHistoryPersistence>) jobHistoryPersistenceDao.findAll();
	}

	public Page<JobHistoryPersistence> getDataantJobHistory(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<JobHistoryPersistence> spec = buildSpecification(searchParams);

		return jobHistoryPersistenceDao.findAll(spec, pageRequest);
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
	private Specification<JobHistoryPersistence> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<JobHistoryPersistence> spec = DynamicSpecifications.bySearchFilter(filters.values(), JobHistoryPersistence.class);
		return spec;
	}
}
