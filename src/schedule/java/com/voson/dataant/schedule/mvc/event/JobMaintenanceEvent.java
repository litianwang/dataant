package com.voson.dataant.schedule.mvc.event;

import com.voson.dataant.mvc.AppEvent;
import com.voson.dataant.mvc.EventType;

public class JobMaintenanceEvent extends AppEvent {
	
	private final String jobId;
	public JobMaintenanceEvent(EventType type,String jobId){
		super(type);
		this.jobId=jobId;
	}
	public String getJobId() {
		return jobId;
	}

}
