package com.voson.dataant.store.mysql.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.store.mysql.persistence.GroupPersistence;
import com.voson.dataant.store.mysql.persistence.JobPersistence;

public interface GroupPersistenceDao  extends PagingAndSortingRepository<GroupPersistence, Long>,  JpaSpecificationExecutor<GroupPersistence>{

	/**
	 * @return
	 */
	@Query(value="from com.voson.hornet.store.mysql.persistence.GroupPersistence where parent=") 
	 public List<GroupPersistence> findByParent(Long groupId); 
}