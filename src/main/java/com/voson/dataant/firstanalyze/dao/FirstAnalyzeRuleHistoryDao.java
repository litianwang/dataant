package com.voson.dataant.firstanalyze.dao;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.voson.dataant.firstanalyze.model.FirstAnalyzeRuleHistory;

/**
 * <code>{@link FirstAnalyzeRuleHistoryDao}</code>
 *
 *
 * @author litianwang
 */
public interface FirstAnalyzeRuleHistoryDao extends PagingAndSortingRepository<FirstAnalyzeRuleHistory, Long>, JpaSpecificationExecutor<FirstAnalyzeRuleHistory>{
	

}
