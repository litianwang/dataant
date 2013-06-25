package com.voson.dataant.search.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.search.model.SearchApp;

/**
 * <code>{@link SearchAppDao}</code>
 *
 *
 * @author litianwang
 */
public interface SearchAppDao extends PagingAndSortingRepository<SearchApp, Long>, JpaSpecificationExecutor<SearchApp>{
	

	/**
	 * 执行原生的SQL语句
	 * @return
	 */
	@Query(value="select * from SEARCH_APP where STATUS ='1'",nativeQuery=true) 
	 public List<SearchApp> findActiveSearchApp(); 
}
