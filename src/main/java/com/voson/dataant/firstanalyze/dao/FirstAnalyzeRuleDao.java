/**
 * This file created at 2013-6-14.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.firstanalyze.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.firstanalyze.model.FirstAnalyzeRule;

/**
 * <code>{@link FirstAnalyzeRuleDao}</code>
 *
 *
 * @author litianwang
 */
public interface FirstAnalyzeRuleDao extends PagingAndSortingRepository<FirstAnalyzeRule, Long>, JpaSpecificationExecutor<FirstAnalyzeRule>{
	
	/**
	 * 执行原生的SQL语句
	 * @return
	 */
	@Query(value="select * from first_analyze_rule where RUN_STATUS ='1' order by TASK_PRIORITY ASC",nativeQuery=true) 
	 public List<FirstAnalyzeRule> findActiveFirstAnalyzeRule(); 
	
	
	
	@Query(value="SELECT FIRST_ANALYZE_RULE_TASK_ID_SEQ.NEXTVAL FROM dual",nativeQuery=true) 
	public java.math.BigDecimal getTaskIdSeq();

}
