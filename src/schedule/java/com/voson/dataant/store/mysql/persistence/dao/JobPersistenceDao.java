package com.voson.dataant.store.mysql.persistence.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.store.mysql.persistence.JobPersistence;
public interface JobPersistenceDao  extends PagingAndSortingRepository<JobPersistence, Long>,  JpaSpecificationExecutor<JobPersistence> {
	/**
	 * @return
	 */
	public List<JobPersistence> findByGroupId(Long groupId); 
	

	public List<JobPersistence> findByIdIn(Collection<Long> IdList); 
	
	
}
