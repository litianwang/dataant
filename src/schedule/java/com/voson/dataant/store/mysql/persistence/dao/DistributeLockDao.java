package com.voson.dataant.store.mysql.persistence.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.voson.dataant.firstanalyze.model.FirstAnalyzeRule;

public interface DistributeLockDao extends  JpaSpecificationExecutor<FirstAnalyzeRule> {

}
