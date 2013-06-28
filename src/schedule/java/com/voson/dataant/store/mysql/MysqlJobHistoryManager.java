package com.voson.dataant.store.mysql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.voson.dataant.model.JobHistory;
import com.voson.dataant.store.JobHistoryManager;
import com.voson.dataant.store.mysql.persistence.JobHistoryPersistence;
import com.voson.dataant.store.mysql.persistence.dao.JobHistoryPersistenceDao;
import com.voson.dataant.store.mysql.tool.PersistenceAndBeanConvert;
public class MysqlJobHistoryManager implements JobHistoryManager{
	

	@Autowired
	JobHistoryPersistenceDao jobPersistenceDao; 


	@Override
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void updateJobHistoryLog(final String id, final String log) {
		jobPersistenceDao.updateLog(log, Long.valueOf(id));
	}

	@Override
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void updateJobHistory(JobHistory history) {
		JobHistoryPersistence org = jobPersistenceDao.findOne(Long.valueOf(history.getId()));
		final JobHistoryPersistence persist=PersistenceAndBeanConvert.convert(history);
		persist.setGmtModified(new Date());
		persist.setGmtCreate(org.getGmtCreate());
		persist.setLog(org.getLog());
		jobPersistenceDao.save(persist);
	}

	@Override
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public JobHistory addJobHistory(JobHistory history) {
		JobHistoryPersistence persist=PersistenceAndBeanConvert.convert(history);
		persist.setGmtCreate(new Date());
		persist.setGmtModified(new Date());
		Long id = jobPersistenceDao.save(persist).getId();
		history.setId(id.toString()); 
		return history;
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public JobHistory findJobHistory(String id) {
		JobHistoryPersistence persist= jobPersistenceDao.findOne(Long.valueOf(id));
		
		return PersistenceAndBeanConvert.convert(persist);
	}
 
//	@Override
//	public Map<String, JobHistory> findLastHistoryByList(final List<String> jobIds) {
//		if(jobIds.isEmpty()){
//			return Collections.emptyMap();
//		}
//		final List<Long> ids=(List<Long>) getHibernateTemplate().execute(new HibernateCallback() {
//			public Object doInHibernate(Session session) throws HibernateException,
//					SQLException {
//				String sql="select max(id) as m_id,job_id  from dataant_job_history where job_id in (:idList) group by job_id";
//				SQLQuery query=session.createSQLQuery(sql);
//				query.setParameterList("idList", jobIds);
//				List<Object[]> list= query.list();
//				List<Long> ids=new ArrayList<Long>();
//				for(Object[] o:list){
//					ids.add(((Number)o[0]).longValue());
//				}
//				return ids;
//			}
//		});
//		List<JobHistory> list=(List<JobHistory>) getHibernateTemplate().execute(new HibernateCallback() {
//			public Object doInHibernate(Session session) throws HibernateException,
//					SQLException {
//				if(ids==null || ids.isEmpty()){
//					return Collections.emptyList();
//				}
//				String sql="select id,job_id,start_time,end_time,execute_host,status,trigger_type,illustrate,operator,properties from dataant_job_history where id in (:ids)";
//				SQLQuery query=session.createSQLQuery(sql);
//				query.setParameterList("ids", ids);
//				List<Object[]> list= query.list();
//				List<JobHistory> result=new ArrayList<JobHistory>();
//				for(Object[] o:list){
//					JobHistoryPersistence p=new JobHistoryPersistence();
//					p.setId(((Number)o[0]).longValue());
//					p.setJobId(((Number)o[1]).longValue());
//					p.setStartTime((Date)o[2]);
//					p.setEndTime((Date)o[3]);
//					p.setExecuteHost((String)o[4]);
//					p.setStatus((String)o[5]);
//					p.setTriggerType((Integer)o[6]);
//					p.setIllustrate((String)o[7]);
//					p.setOperator((String)o[8]);
//					p.setProperties((String)o[9]);
//					result.add(PersistenceAndBeanConvert.convert(p));
//				}
//				return result;
//			}
//		});
//		
//		
//		Map<String, JobHistory> map=new HashMap<String, JobHistory>();
//		for(JobHistory p:list){
//			map.put(p.getJobId(),p);
//		}
//		return map;
//	}

	@Override
	public List<JobHistory> findRecentRunningHistory() {
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		List<Object[]> list = jobPersistenceDao.findRecentRunningHistory(cal.getTime());
		List<JobHistory> result=new ArrayList<JobHistory>();
		for(Object[] o:list){
			JobHistoryPersistence p=new JobHistoryPersistence();
			p.setId(((Number)o[0]).longValue());
			p.setJobId(((Number)o[1]).longValue());
			p.setStartTime((Date)o[2]);
			p.setEndTime((Date)o[3]);
			p.setExecuteHost((String)o[4]);
			p.setStatus((String)o[5]);
			p.setTriggerType((Integer)o[6]);
			p.setIllustrate((String)o[7]);
			p.setOperator((String)o[8]);
			p.setProperties((String)o[9]);
			result.add(PersistenceAndBeanConvert.convert(p));
		}
		return result;
	}

}
