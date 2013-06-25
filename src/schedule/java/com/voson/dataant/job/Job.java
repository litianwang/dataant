package com.voson.dataant.job;




public interface Job {

	Integer run() throws Exception;
	
	void cancel();
	
	JobContext getJobContext();
	
	boolean isCanceled();
	
	
}
