package com.voson.dataant.schedule.mvc.event;

import com.voson.dataant.model.JobHistory;
import com.voson.dataant.model.JobStatus.TriggerType;
import com.voson.dataant.mvc.AppEvent;
import com.voson.dataant.schedule.mvc.DataantJobException;
/**
 * Job失败触发的事件
 * @author litianwang
 *
 */
public class JobFailedEvent extends AppEvent{

	private final JobHistory history;
	private final String jobId;
	private TriggerType triggerType;
	private final DataantJobException jobException;
	
	public JobFailedEvent(String jobId,TriggerType triggerType) {
		this(jobId,triggerType,null,null);
	}
	
	public JobFailedEvent(String jobId,TriggerType triggerType,JobHistory history,DataantJobException t){
		super(Events.JobFailed);
		this.jobId=jobId;
		this.triggerType=triggerType;
		this.history=history;
		this.jobException=t;
	}
	
	public String getJobId() {
		return jobId;
	}


	public TriggerType getTriggerType() {
		return triggerType;
	}

	public JobHistory getHistory() {
		return history;
	}

	public DataantJobException getJobException() {
		return jobException;
	}


	
}
