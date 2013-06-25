package com.voson.dataant.search.service;
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

import com.voson.dataant.search.dao.SearchAppDao;
import com.voson.dataant.search.model.SearchApp;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class SearchAppService {

	private SearchAppDao searchAppDao;

	public SearchApp getSearchApp(Long id) {
		return searchAppDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveSearchApp(SearchApp entity) {
		searchAppDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteSearchApp(Long id) {
		searchAppDao.delete(id);
	}

	public List<SearchApp> getAllSearchApp() {
		return (List<SearchApp>) searchAppDao.findAll();
	}

	public Page<SearchApp> getSearchApp(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<SearchApp> spec = buildSpecification(searchParams);

		return searchAppDao.findAll(spec, pageRequest);
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
	private Specification<SearchApp> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SearchApp> spec = DynamicSpecifications.bySearchFilter(filters.values(), SearchApp.class);
		return spec;
	}

	@Autowired
	public void setSearchAppDao(SearchAppDao searchAppDao) {
		this.searchAppDao = searchAppDao;
	}
}
