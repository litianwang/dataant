package com.voson.dataant.store.mysql.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.store.mysql.persistence.JobHistoryPersistence;

public interface JobHistoryPersistenceDao extends PagingAndSortingRepository<JobHistoryPersistence, Long>, JpaSpecificationExecutor<JobHistoryPersistence> {
	/**
	 * 执行原生的SQL语句
	 * @return
	 */
	@Query(value="select id,job_id,start_time,end_time,execute_host,status,trigger_type,illustrate,operator,properties from zeus_job_history where start_time >?1",nativeQuery=true) 
	 public List<Object[]> findRecentRunningHistory(Date date); 
}
