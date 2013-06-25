package com.voson.dataant.search.dao;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.search.model.MiaUrlClass;

/**
 * <code>{@link MiaUrlClassDao}</code>
 *
 *
 * @author litianwang
 */
public interface MiaUrlClassDao extends PagingAndSortingRepository<MiaUrlClass, Long>, JpaSpecificationExecutor<MiaUrlClass>{
	

}
