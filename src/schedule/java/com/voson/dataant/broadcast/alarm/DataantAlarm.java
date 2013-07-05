package com.voson.dataant.broadcast.alarm;

import com.voson.dataant.schedule.mvc.JobFailListener.ChainException;

public interface DataantAlarm {

	void alarm(String historyId,String title,String content,ChainException chain) throws Exception;
	
	void alarm(String historyId,String title,String content) throws Exception;
}
