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

import com.voson.dataant.firstanalyze.dao.FirstAnalyzeRuleDao;
import com.voson.dataant.firstanalyze.model.FirstAnalyzeRule;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class FirstAnalyzeService {

	private FirstAnalyzeRuleDao firstAnalyzeRuleDao;

	public FirstAnalyzeRule getFirstAnalyzeRule(Long id) {
		return firstAnalyzeRuleDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveFirstAnalyzeRule(FirstAnalyzeRule entity) {
		firstAnalyzeRuleDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteFirstAnalyzeRule(Long id) {
		firstAnalyzeRuleDao.delete(id);
	}

	public List<FirstAnalyzeRule> getAllFirstAnalyzeRule() {
		return (List<FirstAnalyzeRule>) firstAnalyzeRuleDao.findAll();
	}

	public Page<FirstAnalyzeRule> getFirstAnalyzeRule(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<FirstAnalyzeRule> spec = buildSpecification(searchParams);

		return firstAnalyzeRuleDao.findAll(spec, pageRequest);
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
	private Specification<FirstAnalyzeRule> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<FirstAnalyzeRule> spec = DynamicSpecifications.bySearchFilter(filters.values(), FirstAnalyzeRule.class);
		return spec;
	}

	@Autowired
	public void setFirstAnalyzeRuleDao(FirstAnalyzeRuleDao firstAnalyzeRuleDao) {
		this.firstAnalyzeRuleDao = firstAnalyzeRuleDao;
	}
}
