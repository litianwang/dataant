package com.voson.dataant.store.mysql.persistence.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.store.mysql.persistence.DistributeLock;

public interface DistributeLockDao extends PagingAndSortingRepository<DistributeLock, Integer>, JpaSpecificationExecutor<DistributeLock> {
	
	public Page<DistributeLock> findBySubgroupOrderByIdDesc(String subgroup, Pageable pageable);

}
