package com.voson.dataant.search.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.search.model.SearchUrl;


/**
 * <code>{@link SearchUrlDao}</code>
 *
 *
 * @author litianwang
 */
public interface SearchUrlDao extends PagingAndSortingRepository<SearchUrl, Long>, JpaSpecificationExecutor<SearchUrl>{
	
	/**
	 * 执行原生的SQL语句
	 * @return
	 */
	@Query(value="select * from SEARCH_URL where STATUS ='1'",nativeQuery=true) 
	 public List<SearchUrl> findActiveSearchUrl(); 

}
