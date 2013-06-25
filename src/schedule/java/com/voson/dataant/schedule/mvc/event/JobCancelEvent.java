package com.voson.dataant.schedule.mvc.event;

import com.voson.dataant.mvc.AppEvent;

public class JobCancelEvent extends AppEvent{

	private String jobId;
	public JobCancelEvent(String jobId) {
		super(Events.JobCancel);
		this.jobId=jobId;
	}

}
