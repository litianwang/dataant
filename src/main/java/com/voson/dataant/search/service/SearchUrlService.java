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

import com.voson.dataant.search.dao.SearchUrlDao;
import com.voson.dataant.search.model.SearchUrl;


//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class SearchUrlService {

	private SearchUrlDao searchUrlDao;

	public SearchUrl getSearchUrl(Long id) {
		return searchUrlDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveSearchUrl(SearchUrl entity) {
		searchUrlDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteSearchUrl(Long id) {
		searchUrlDao.delete(id);
	}

	public List<SearchUrl> getAllSearchUrl() {
		return (List<SearchUrl>) searchUrlDao.findAll();
	}

	public Page<SearchUrl> getSearchUrl(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<SearchUrl> spec = buildSpecification(searchParams);

		return searchUrlDao.findAll(spec, pageRequest);
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
	private Specification<SearchUrl> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<SearchUrl> spec = DynamicSpecifications.bySearchFilter(filters.values(), SearchUrl.class);
		return spec;
	}

	@Autowired
	public void setSearchUrlDao(SearchUrlDao searchUrlDao) {
		this.searchUrlDao = searchUrlDao;
	}
}
