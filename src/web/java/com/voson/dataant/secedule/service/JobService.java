package com.voson.dataant.secedule.service;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.voson.dataant.model.JobHistory;
import com.voson.dataant.model.JobStatus.Status;
import com.voson.dataant.model.JobStatus.TriggerType;
import com.voson.dataant.socket.protocol.Protocol.ExecuteKind;
import com.voson.dataant.socket.worker.ClientWorker;
import com.voson.dataant.store.JobHistoryManager;
import com.voson.dataant.store.mysql.persistence.JobPersistence;
import com.voson.dataant.store.mysql.persistence.dao.JobPersistenceDao;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
public class JobService {
	
	private static Logger log = LoggerFactory.getLogger(JobService.class);

	@Autowired
	private JobHistoryManager jobHistoryManager;
	@Autowired
	private ClientWorker worker;
	
	@Autowired
	JobPersistenceDao jobPersistenceDao;

	public JobPersistence getJob(Long id) {
		return jobPersistenceDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveJob(JobPersistence entity) {
		jobPersistenceDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteJob(Long id) {
		jobPersistenceDao.delete(id);
	}

	public List<JobPersistence> getAllJob() {
		return (List<JobPersistence>) jobPersistenceDao.findAll();
	}

	public Page<JobPersistence> getJob(Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<JobPersistence> spec = buildSpecification(searchParams);

		return jobPersistenceDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("ruleName".equals(sortType)) {
			sort = new Sort(Direction.ASC, "ruleName");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<JobPersistence> buildSpecification(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<JobPersistence> spec = DynamicSpecifications.bySearchFilter(filters.values(), JobPersistence.class);
		return spec;
	}
	
	/**
	 * 运行作业
	 * @param jobId
	 * @param type
	 * @throws Exception
	 */
	public void runJob(String jobId, int type) throws Exception{

		TriggerType triggerType=null;
		if(type==1){
			triggerType=TriggerType.MANUAL;
		}else if(type==2){
			triggerType=TriggerType.MANUAL_RECOVER;
		}
		JobHistory history=new JobHistory();
		history.setJobId(jobId);
		history.setTriggerType(triggerType);
		history.setOperator("litianwang");
		history.setIllustrate("触发人："+"litianwang");
		history.setStatus(Status.RUNNING);
		jobHistoryManager.addJobHistory(history);
		ExecuteKind kind=null;
		if(triggerType==TriggerType.MANUAL){
			kind=ExecuteKind.ManualKind;
		}else if(triggerType==TriggerType.MANUAL_RECOVER){
			kind=ExecuteKind.ScheduleKind;
		}
		try {
			worker.executeJobFromWeb(kind, history.getId());
		} catch (Exception e) {
			log.error("error",e);
			throw new Exception(e.getMessage());
		}
	
	}

}
