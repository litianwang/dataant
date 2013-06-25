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

import com.voson.dataant.search.dao.MiaUrlClassDao;
import com.voson.dataant.search.model.MiaUrlClass;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class MiaUrlClassService {

	private MiaUrlClassDao miaUrlClassDao;

	public MiaUrlClass getMiaUrlClass(Long id) {
		return miaUrlClassDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveMiaUrlClass(MiaUrlClass entity) {
		miaUrlClassDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteMiaUrlClass(Long id) {
		miaUrlClassDao.delete(id);
	}

	public List<MiaUrlClass> getAllMiaUrlClass() {
		return (List<MiaUrlClass>) miaUrlClassDao.findAll();
	}

	public Page<MiaUrlClass> getMiaUrlClass(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<MiaUrlClass> spec = buildSpecification(searchParams);

		return miaUrlClassDao.findAll(spec, pageRequest);
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
	private Specification<MiaUrlClass> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<MiaUrlClass> spec = DynamicSpecifications.bySearchFilter(filters.values(), MiaUrlClass.class);
		return spec;
	}

	@Autowired
	public void setMiaUrlClassDao(MiaUrlClassDao miaUrlClassDao) {
		this.miaUrlClassDao = miaUrlClassDao;
	}
}
