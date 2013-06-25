package com.voson.dataant.schedule.mvc.event;

import com.voson.dataant.model.JobStatus.TriggerType;
import com.voson.dataant.mvc.AppEvent;
/**
 * Job执行成功事件
 * @author zhoufang
 *
 */
public class JobSuccessEvent extends AppEvent{
	private String historyId;
	private String jobId;
	private TriggerType triggerType;
	public JobSuccessEvent(String jobId,TriggerType triggerType,String historyId) {
		super(Events.JobSucceed);
		this.jobId=jobId;
		this.triggerType=triggerType;
		this.historyId=historyId;
	}

	public String getJobId() {
		return jobId;
	}

	public String getHistoryId() {
		return historyId;
	}

	public TriggerType getTriggerType() {
		return triggerType;
	}


}
