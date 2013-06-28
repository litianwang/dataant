package com.voson.dataant.schedule.mvc;

import com.voson.dataant.util.DataantException;


public class DataantJobException extends DataantException{

	private static final long serialVersionUID = 1L;
	private String causeJobId;
	
	public DataantJobException(String jobId,String msg){
		super(msg);
		this.causeJobId=jobId;
	}
	
	public DataantJobException(String jobId,String msg,Throwable cause){
		super(msg, cause);
		this.causeJobId=jobId;
		
	}

	public String getCauseJobId() {
		return causeJobId;
	}
}
