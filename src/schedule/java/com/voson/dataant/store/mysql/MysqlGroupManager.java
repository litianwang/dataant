package com.voson.dataant.store.mysql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.voson.dataant.model.GroupDescriptor;
import com.voson.dataant.model.JobDescriptor;
import com.voson.dataant.model.JobStatus;
import com.voson.dataant.model.JobDescriptor.JobRunType;
import com.voson.dataant.model.JobDescriptor.JobScheduleType;
import com.voson.dataant.store.GroupBean;
import com.voson.dataant.store.GroupManager;
import com.voson.dataant.store.GroupManagerTool;
import com.voson.dataant.store.JobBean;
import com.voson.dataant.store.mysql.persistence.GroupPersistence;
import com.voson.dataant.store.mysql.persistence.JobPersistence;
import com.voson.dataant.store.mysql.persistence.dao.GroupPersistenceDao;
import com.voson.dataant.store.mysql.persistence.dao.JobPersistenceDao;
import com.voson.dataant.store.mysql.tool.PersistenceAndBeanConvert;
import com.voson.dataant.util.Tuple;
import com.voson.dataant.util.DataantException;
public class MysqlGroupManager implements GroupManager{
	

	@Autowired
	GroupPersistenceDao groupPersistenceDao; 
	@Autowired
	JobPersistenceDao JobPersistenceDao;
	
	@Override
	public void deleteGroup(String user, String groupId) throws DataantException {
		
		GroupBean group=getDownstreamGroupBean(groupId);
		if(group.isDirectory()){
			if(!group.getChildrenGroupBeans().isEmpty()){
				throw new DataantException("该组下不为空，无法删除");
			}
		}else{
			if(!group.getJobBeans().isEmpty()){
				throw new DataantException("该组下不为空，无法删除");
			}
		}
		groupPersistenceDao.delete(Integer.valueOf(groupId));
		//getHibernateTemplate().delete(getHibernateTemplate().get(GroupPersistence.class, Integer.valueOf(groupId)));
	}

	@Override
	public void deleteJob(String user, String jobId) throws DataantException {
		GroupBean root= getGlobeGroupBean();
		JobBean job=root.getAllSubJobBeans().get(jobId);
		if(!job.getDepender().isEmpty()){
			List<String> deps=new ArrayList<String>();
			for(JobBean jb:job.getDepender()){
				deps.add(jb.getJobDescriptor().getId());
			}
			throw new DataantException("该Job正在被其他Job"+deps.toString()+"依赖，无法删除");
		}
		JobPersistenceDao.delete(Long.valueOf(jobId));
		//getHibernateTemplate().delete(getHibernateTemplate().get(JobPersistence.class, Long.valueOf(jobId)));
	}


	@Override
	public GroupBean getDownstreamGroupBean(String groupId) {
		GroupDescriptor group=getGroupDescriptor(groupId);
		GroupBean result=new GroupBean(group);
		return getDownstreamGroupBean(result);
	}
	@Override
	public GroupBean getDownstreamGroupBean(GroupBean parent) {
		if(parent.isDirectory()){
			List<GroupDescriptor> children=getChildrenGroup(parent.getGroupDescriptor().getId());
			for(GroupDescriptor child:children){
				GroupBean childBean=new GroupBean(child);
				getDownstreamGroupBean(childBean);
				childBean.setParentGroupBean(parent);
				parent.getChildrenGroupBeans().add(childBean);
			}
		}else{
			List<Tuple<JobDescriptor, JobStatus>> jobs=getChildrenJob(parent.getGroupDescriptor().getId());
			for(Tuple<JobDescriptor, JobStatus> tuple:jobs){
				JobBean jobBean=new JobBean(tuple.getX(),tuple.getY());
				jobBean.setGroupBean(parent);
				parent.getJobBeans().put(tuple.getX().getId(), jobBean);
			}
		}
		
		return parent;
	}

	@Override
	public GroupBean getGlobeGroupBean() {
		return GroupManagerTool.buildGlobeGroupBean(this);
	}
	
	/**
	 * 获取叶子组下所有的Job
	 * @param groupId
	 * @return
	 */
	@Override
	public List<Tuple<JobDescriptor, JobStatus>> getChildrenJob(String groupId){
		// List<JobPersistence> list=getHibernateTemplate().find("from com.voson.hornet.store.mysql.persistence.JobPersistence where groupId="+groupId);
		List<JobPersistence> list = JobPersistenceDao.findByGroupId(Integer.valueOf(groupId));
		List<Tuple<JobDescriptor, JobStatus>> result=new ArrayList<Tuple<JobDescriptor, JobStatus>>();
		if(list!=null){
			for(JobPersistence j:list){
				result.add(PersistenceAndBeanConvert.convert(j));
			}
		}
		return result;
	}
	/**
	 * 获取组的下级组列表
	 * @param groupId
	 * @return
	 */
	@Override
	public List<GroupDescriptor> getChildrenGroup(String groupId){
		List<GroupPersistence> list = groupPersistenceDao.findByParent(Integer.valueOf(groupId));
		// List<GroupPersistence> list=getHibernateTemplate().find("from com.voson.hornet.store.mysql.persistence.GroupPersistence where parent="+groupId);
		List<GroupDescriptor> result=new ArrayList<GroupDescriptor>();
		if(list!=null){
			for(GroupPersistence p:list){
				result.add(PersistenceAndBeanConvert.convert(p));
			}
		}
		return result;
	}

	@Override
	public GroupDescriptor getGroupDescriptor(String groupId) {
		GroupPersistence persist = groupPersistenceDao.findOne(Integer.valueOf(groupId));
		// GroupPersistence persist=(GroupPersistence)getHibernateTemplate().get(GroupPersistence.class, Integer.valueOf(groupId));
		if(persist!=null){
			return PersistenceAndBeanConvert.convert(persist);
		}
		return null;
	}

	@Override
	public Tuple<JobDescriptor,JobStatus> getJobDescriptor(String jobId) {
		JobPersistence persist=getJobPersistence(jobId);
		if(persist==null){
			return null;
		}
		return PersistenceAndBeanConvert.convert(persist);
	}
	
	private JobPersistence getJobPersistence(String jobId){
		JobPersistence persist = JobPersistenceDao.findOne(Long.valueOf(jobId));
		//JobPersistence persist=(JobPersistence) getHibernateTemplate().get(JobPersistence.class, Long.valueOf(jobId));
		if(persist==null){
			return null;
		}
		return persist;
	}
	
	@Override
	public String getRootGroupId() {
		Page<GroupPersistence> list = groupPersistenceDao.findAll(new PageRequest(0, 1, new Sort(Direction.ASC, "id")));
		if(list==null || list.getNumberOfElements()==0){
			GroupPersistence persist=new GroupPersistence();
			persist.setName("总部");
			persist.setOwner("litianwang");
			persist.setDirectory(0);
			groupPersistenceDao.save(persist);
			if(persist.getId()==null){
				return null;
			}
			return String.valueOf(persist.getId());
		}
		return String.valueOf(list.iterator().next().getId());

	}

	@Override
	public GroupBean getUpstreamGroupBean(String groupId) {
		return GroupManagerTool.getUpstreamGroupBean(groupId, this);
	}

	@Override
	public JobBean getUpstreamJobBean(String jobId) {
		return GroupManagerTool.getUpstreamJobBean(jobId, this);
	}

	@Override
	public void updateGroup(String user,GroupDescriptor group) throws DataantException{
		// GroupPersistence old=(GroupPersistence) getHibernateTemplate().get(GroupPersistence.class, Integer.valueOf(group.getId()));
		GroupPersistence old= groupPersistenceDao.findOne(Integer.valueOf(group.getId()));
		updateGroup(user, group, old.getOwner(),old.getParent()==null?null:old.getParent().toString());
	}
	
	public void updateGroup(String user,GroupDescriptor group,String owner,String parent) throws DataantException{
		GroupPersistence old= groupPersistenceDao.findOne(Integer.valueOf(group.getId()));
		//GroupPersistence old=(GroupPersistence) getHibernateTemplate().get(GroupPersistence.class, Integer.valueOf(group.getId()));
		
		GroupPersistence persist=PersistenceAndBeanConvert.convert(group);
		
		persist.setOwner(owner);
		if(parent!=null){
			persist.setParent(Integer.valueOf(parent));
		}
		
		
		//以下属性不允许修改，强制采用老的数据
		persist.setDirectory(old.getDirectory());
		persist.setGmtCreate(old.getGmtCreate());
		persist.setGmtModified(new Date());
		
		// getHibernateTemplate().update(persist);
		groupPersistenceDao.save(persist);
	}
	

	@Override
	public void updateJob(String user,JobDescriptor job) throws DataantException {
		JobPersistence orgPersist = JobPersistenceDao.findOne(Long.valueOf(job.getId()));
		// JobPersistence orgPersist=(JobPersistence) getHibernateTemplate().get(JobPersistence.class, Long.valueOf(job.getId()));
		updateJob(user, job, orgPersist.getOwner(),orgPersist.getGroupId().toString());
	}
	
	public void updateJob(String user,JobDescriptor job,String owner,String groupId) throws DataantException {
		JobPersistence orgPersist = JobPersistenceDao.findOne(Long.valueOf(job.getId()));
		//JobPersistence orgPersist=(JobPersistence) getHibernateTemplate().get(JobPersistence.class, Long.valueOf(job.getId()));
		if(job.getScheduleType()==JobScheduleType.Independent){
			job.setDependencies(new ArrayList<String>());
		}else if(job.getScheduleType()==JobScheduleType.Dependent){
			job.setCronExpression("");
		}
		job.setOwner(owner);
		job.setGroupId(groupId);
		//以下属性不允许修改，强制采用老的数据
		JobPersistence persist=PersistenceAndBeanConvert.convert(job);
		persist.setGmtCreate(orgPersist.getGmtCreate());
		persist.setGmtModified(new Date());
		persist.setRunType(orgPersist.getRunType());
		persist.setStatus(orgPersist.getStatus());
		persist.setReadyDependency(orgPersist.getReadyDependency());
		
//		if(jobValidate.valide(job)){
			// getHibernateTemplate().update(persist);
			JobPersistenceDao.save(persist);
//		}
	}
	////@Autowired
	////private JobValidate jobValidate;
	

	@Override
	public GroupDescriptor createGroup(String user, String groupName,
			String parentGroup, boolean isDirectory) throws DataantException {
		if(parentGroup==null){
			throw new DataantException("parent group may not be null");
		}
		GroupDescriptor group=new GroupDescriptor();
		group.setOwner(user);
		group.setName(groupName);
		group.setParent(parentGroup);
		group.setDirectory(isDirectory);
		
		
		
		//GroupValidate.valide(group);
		
		GroupPersistence persist=PersistenceAndBeanConvert.convert(group);
		persist.setGmtCreate(new Date());
		persist.setGmtModified(new Date());
		
		// getHibernateTemplate().save(persist);
		groupPersistenceDao.save(persist);
		return PersistenceAndBeanConvert.convert(persist);
	}

	@Override
	public JobDescriptor createJob(String user, String jobName,
			String parentGroup,JobRunType jobType) throws DataantException {
		GroupDescriptor parent=getGroupDescriptor(parentGroup);
		if(parent.isDirectory()){
			throw new DataantException("目录组下不得创建Job");
		}
		JobDescriptor job=new JobDescriptor();
		job.setOwner(user);
		job.setName(jobName);
		job.setGroupId(parentGroup);
		job.setJobType(jobType);
		////job.setPreProcessers(Arrays.asList((Processer)new DownloadProcesser()));
		JobPersistence persist=PersistenceAndBeanConvert.convert(job);
		persist.setGmtCreate(new Date());
		persist.setGmtModified(new Date());
		//getHibernateTemplate().save(persist);
		JobPersistenceDao.save(persist);
		return PersistenceAndBeanConvert.convert(persist).getX();
	}

	
	@Override
	public Map<String, Tuple<JobDescriptor, JobStatus>> getJobDescriptor(final Collection<String> jobIds) {
		List<Long> ids=new ArrayList<Long>();
		for(String i:jobIds){
			ids.add(Long.valueOf(i));
		}
		List<JobPersistence> list= JobPersistenceDao.findByIdIn(ids);
		List<Tuple<JobDescriptor, JobStatus>> result =new ArrayList<Tuple<JobDescriptor, JobStatus>>();
		if(list!=null && !list.isEmpty()){
			for(JobPersistence persist:list){
				result.add(PersistenceAndBeanConvert.convert(persist));
			}
		}
		
		Map<String, Tuple<JobDescriptor, JobStatus>> map=new HashMap<String, Tuple<JobDescriptor, JobStatus>>();
		for(Tuple<JobDescriptor, JobStatus> jd:result){
			map.put(jd.getX().getId(), jd);
		}
		return map;
	}

	@Override
	public void updateJobStatus(JobStatus jobStatus) {
		JobPersistence persistence=getJobPersistence(jobStatus.getJobId());
		persistence.setGmtModified(new Date());
		
		//只修改状态  和  依赖 2个字段
		JobPersistence temp=PersistenceAndBeanConvert.convert(jobStatus);
		persistence.setStatus(temp.getStatus());
		persistence.setReadyDependency(temp.getReadyDependency());
		persistence.setHistoryId(temp.getHistoryId());
		
		// getHibernateTemplate().update(persistence);
		JobPersistenceDao.save(persistence);
	}

	@Override
	public JobStatus getJobStatus(String jobId) {
		Tuple<JobDescriptor, JobStatus> tuple=getJobDescriptor(jobId);
		if(tuple==null){
			return null;
		}
		return tuple.getY();
	}

	@Override
	public void grantGroupOwner(String granter, String uid, String groupId) throws DataantException {
		GroupDescriptor gd=getGroupDescriptor(groupId);
		if(gd!=null){
			updateGroup(granter, gd,uid,gd.getParent());
		}
	}

	@Override
	public void grantJobOwner(String granter, String uid, String jobId)throws DataantException {
		Tuple<JobDescriptor, JobStatus> job=getJobDescriptor(jobId);
		if(job!=null){
			job.getX().setOwner(uid);
			updateJob(granter, job.getX(),uid,job.getX().getGroupId());
		}
	}

	@Override
	public void moveJob(String uid,String jobId, String groupId) throws DataantException {
		JobDescriptor jd=getJobDescriptor(jobId).getX();
		GroupDescriptor gd=getGroupDescriptor(groupId);
		if(gd.isDirectory()){
			throw new DataantException("非法操作");
		}
		updateJob(uid, jd, jd.getOwner(), groupId);
	}

	@Override
	public void moveGroup(String uid,String groupId, String newParentGroupId)
			throws DataantException {
		GroupDescriptor gd=getGroupDescriptor(groupId);
		GroupDescriptor parent=getGroupDescriptor(newParentGroupId);
		if(!parent.isDirectory()){
			throw new DataantException("非法操作");
		}
		updateGroup(uid, gd, gd.getOwner(), newParentGroupId);
	}

}