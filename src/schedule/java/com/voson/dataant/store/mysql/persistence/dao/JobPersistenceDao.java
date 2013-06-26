package com.voson.dataant.store.mysql.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.store.mysql.persistence.JobPersistence;
public interface JobPersistenceDao  extends PagingAndSortingRepository<JobPersistence, Long>,  JpaSpecificationExecutor<JobPersistence> {
	/**
	 * @return
	 */
	@Query(value="from com.voson.hornet.store.mysql.persistence.JobPersistence where groupId=?1") 
	 public List<JobPersistence> findByGroupId(Long groupId); 
	
	
}
