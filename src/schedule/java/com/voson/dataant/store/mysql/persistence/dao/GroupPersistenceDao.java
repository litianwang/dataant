package com.voson.dataant.store.mysql.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.store.mysql.persistence.GroupPersistence;

public interface GroupPersistenceDao  extends PagingAndSortingRepository<GroupPersistence, Integer>,  JpaSpecificationExecutor<GroupPersistence>{

	/**
	 * @return
	 */
	 public List<GroupPersistence> findByParent(Integer parent); 
}