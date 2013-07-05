package com.voson.dataant.broadcast.alarm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.voson.dataant.model.JobHistory;
import com.voson.dataant.model.JobStatus.TriggerType;
import com.voson.dataant.schedule.mvc.JobFailListener.ChainException;
import com.voson.dataant.store.GroupManager;
import com.voson.dataant.store.JobHistoryManager;

public abstract class AbstractDataantAlarm implements DataantAlarm{
	protected static Logger log=LoggerFactory.getLogger(AbstractDataantAlarm.class);
	@Autowired
	protected JobHistoryManager jobHistoryManager;
	@Autowired
	@Qualifier("groupManager")
	protected GroupManager groupManager;
	
	@Override
	public void alarm(String historyId, String title, String content,ChainException chain)
			throws Exception {
		JobHistory history=jobHistoryManager.findJobHistory(historyId);
		TriggerType type=history.getTriggerType();
		String jobId=history.getJobId();
		List<String> users=new ArrayList<String>();
		if(type==TriggerType.SCHEDULE){
			//TODO： 获取需要通知的用户
			// users=;
		}else{
			users.add(groupManager.getJobDescriptor(jobId).getX().getOwner());
			if(history.getOperator()!=null){
				if(!users.contains(history.getOperator())){
					users.add(history.getOperator());
				}
			}
		}
		List<String> result=new ArrayList<String>();
		if(chain==null){
			result=users;
		}else{
			for(String uid:users){
				Integer count=chain.getUserCountMap().get(uid);
				if(count==null){
					count=1;
					chain.getUserCountMap().put(uid, count);
				}
				if(count<20){//一个job失败，最多发给同一个人20个报警,防止大量广播
					chain.getUserCountMap().put(uid, ++count);
					result.add(uid);
				}
			}
		}
		alarm(result, title, content);
	}
	
	@Override
	public void alarm(String historyId, String title, String content)
			throws Exception {
		alarm(historyId, title, content, null);
	}
	/**
	 * 
	 * @param users 用户域账号id
	 * @param title
	 * @param content
	 * @throws Exception
	 */
	public abstract void alarm(List<String> users,String title,String content) throws Exception;

}
