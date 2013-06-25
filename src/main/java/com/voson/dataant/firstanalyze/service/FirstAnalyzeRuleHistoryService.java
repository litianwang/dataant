package com.voson.dataant.firstanalyze.service;
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

import com.voson.dataant.firstanalyze.dao.FirstAnalyzeRuleHistoryDao;
import com.voson.dataant.firstanalyze.model.FirstAnalyzeRuleHistory;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class FirstAnalyzeRuleHistoryService {

	private FirstAnalyzeRuleHistoryDao firstAnalyzeRuleHistoryDao;

	public FirstAnalyzeRuleHistory getFirstAnalyzeRuleHistory(Long id) {
		return firstAnalyzeRuleHistoryDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveFirstAnalyzeRuleHistory(FirstAnalyzeRuleHistory entity) {
		firstAnalyzeRuleHistoryDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteFirstAnalyzeRuleHistory(Long id) {
		firstAnalyzeRuleHistoryDao.delete(id);
	}

	public List<FirstAnalyzeRuleHistory> getAllFirstAnalyzeRuleHistory() {
		return (List<FirstAnalyzeRuleHistory>) firstAnalyzeRuleHistoryDao.findAll();
	}

	public Page<FirstAnalyzeRuleHistory> getFirstAnalyzeRuleHistory(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<FirstAnalyzeRuleHistory> spec = buildSpecification(searchParams);

		return firstAnalyzeRuleHistoryDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("runDate".equals(sortType)) {
			sort = new Sort(Direction.ASC, "runDate");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<FirstAnalyzeRuleHistory> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<FirstAnalyzeRuleHistory> spec = DynamicSpecifications.bySearchFilter(filters.values(), FirstAnalyzeRuleHistory.class);
		return spec;
	}

	@Autowired
	public void setFirstAnalyzeRuleHistoryDao(FirstAnalyzeRuleHistoryDao firstAnalyzeRuleHistoryDao) {
		this.firstAnalyzeRuleHistoryDao = firstAnalyzeRuleHistoryDao;
	}
}
