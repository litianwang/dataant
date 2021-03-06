package com.voson.dataant.store;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.voson.dataant.model.GroupDescriptor;
import com.voson.dataant.model.JobDescriptor;
import com.voson.dataant.model.JobStatus;
import com.voson.dataant.model.JobDescriptor.JobRunType;
import com.voson.dataant.util.Tuple;
import com.voson.dataant.util.DataantException;


public interface GroupManager {
	/**
	 * 获取根节点的组ID
	 * @return
	 */
	String getRootGroupId();
	/**
	 * 获取根节点Group
	 * 包含完整的树结构信息
	 * @return
	 */
	GroupBean getGlobeGroupBean();
	/**
	 * 根据组ID查询组信息
	 * 向上查询该组上的所有组信息
	 * @param groupName
	 * @return
	 */
	GroupBean getUpstreamGroupBean(String groupId);
	/**
	 * 根据组ID查询组信息
	 * 向下查询该组下的所有组信息以及Job信息
	 * @param groupId
	 * @return
	 */
	GroupBean getDownstreamGroupBean(String groupId);
	
	GroupBean getDownstreamGroupBean(GroupBean parent);
	/**
	 * 根据groupId查询该组的记录
	 * @param groupId
	 * @return
	 */
	GroupDescriptor getGroupDescriptor(String groupId);
	/**
	 * 获取组下的组
	 * @param groupId
	 * @return
	 */
	List<GroupDescriptor> getChildrenGroup(String groupId);
	/**
	 * 根据JobId查询Job信息
	 * 向上查询所有的组信息
	 * @param jobId
	 * @return
	 */
	JobBean getUpstreamJobBean(String jobId);
	/**
	 * 根据jobid查询job的记录信息
	 * @param jobId
	 * @return
	 */
	Tuple<JobDescriptor,JobStatus> getJobDescriptor(String jobId);
	/**
	 * 获取组下的job
	 * @param groupId
	 * @return
	 */
	List<Tuple<JobDescriptor,JobStatus>> getChildrenJob(String groupId);
	/**
	 * 查询Job状态
	 * @param jobId
	 * @return
	 */
	JobStatus getJobStatus(String jobId);
	/**
	 * 批量查询Job信息
	 * @param jobIds
	 * @return
	 */
	Map<String, Tuple<JobDescriptor, JobStatus>> getJobDescriptor(Collection<String> jobIds);
	/**
	 * 创建一个group
	 * @param user
	 * @return
	 */
	GroupDescriptor createGroup(String user,String groupName,String parentGroup,boolean isDirectory) throws DataantException;
	/**
	 * 创建一个Job
	 * @param user
	 * @param group
	 * @return
	 */
	JobDescriptor createJob(String user,String jobName,String parentGroup,JobRunType jobType) throws DataantException;
	/**
	 * 删除组，成功删除需要的条件：
	 * 1.操作人是该组的创建者
	 * 2.该组下的任务没有被其他组依赖
	 * @param user
	 * @param groupId
	 * @return
	 */
	void deleteGroup(String user,String groupId) throws DataantException;
	/**
	 * 删除一个Job
	 * 1.该job没有被其他job依赖
	 * 删除操作完成后，全量重新加载配置
	 * @param user
	 * @param jobId
	 * @return
	 * @throws DataantException
	 */
	void deleteJob(String user,String jobId) throws DataantException;
	/**
	 * 更新Job
	 * @param job
	 * @return
	 */
	void updateJob(String user,JobDescriptor job) throws DataantException;
	/**
	 * 更新Group
	 * @param group
	 * @return
	 */
	void updateGroup(String user,GroupDescriptor group) throws DataantException;
	/**
	 * 更新Job状态
	 * @param jobStatus
	 * @throws DataantException
	 */
	void updateJobStatus(JobStatus jobStatus);
	
	void grantJobOwner(String granter,String uid,String jobId)throws DataantException;
	
	void grantGroupOwner(String granter,String uid,String groupId)throws DataantException;
	
	void moveJob(String uid,String jobId,String groupId) throws DataantException;
	
	void moveGroup(String uid,String groupId,String newParentGroupId) throws DataantException;
}
