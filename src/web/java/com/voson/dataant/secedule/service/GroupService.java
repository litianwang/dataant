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

import com.voson.dataant.store.mysql.persistence.GroupPersistence;
import com.voson.dataant.store.mysql.persistence.dao.GroupPersistenceDao;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class GroupService {

	@Autowired
	GroupPersistenceDao groupPersistenceDao; 

	public GroupPersistence getDataantGroup(Integer id) {
		return groupPersistenceDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveDataantGroup(GroupPersistence entity) {
		groupPersistenceDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteDataantGroup(Integer id) {
		groupPersistenceDao.delete(id);
	}

	public List<GroupPersistence> getAllDataantGroup() {
		return (List<GroupPersistence>) groupPersistenceDao.findAll();
	}

	public Page<GroupPersistence> getDataantGroup(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<GroupPersistence> spec = buildSpecification(searchParams);

		return groupPersistenceDao.findAll(spec, pageRequest);
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
	private Specification<GroupPersistence> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GroupPersistence> spec = DynamicSpecifications.bySearchFilter(filters.values(), GroupPersistence.class);
		return spec;
	}

}
