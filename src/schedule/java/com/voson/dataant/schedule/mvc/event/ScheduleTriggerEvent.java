package com.voson.dataant.schedule.mvc.event;

import com.voson.dataant.mvc.AppEvent;

public class ScheduleTriggerEvent extends AppEvent{

	private final String jobId;
	public ScheduleTriggerEvent(String jobId) {
		super(Events.ScheduleTrigger);
		this.jobId=jobId;
	}
	public String getJobId() {
		return jobId;
	}

}
