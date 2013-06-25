package com.voson.dataant.schedule.mvc;

import com.voson.dataant.util.ZeusException;


public class ZeusJobException extends ZeusException{

	private static final long serialVersionUID = 1L;
	private String causeJobId;
	
	public ZeusJobException(String jobId,String msg){
		super(msg);
		this.causeJobId=jobId;
	}
	
	public ZeusJobException(String jobId,String msg,Throwable cause){
		super(msg, cause);
		this.causeJobId=jobId;
		
	}

	public String getCauseJobId() {
		return causeJobId;
	}
}
