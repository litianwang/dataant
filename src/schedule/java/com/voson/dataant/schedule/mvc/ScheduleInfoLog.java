package com.voson.dataant.schedule.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleInfoLog {

	private static Logger log=LoggerFactory.getLogger(ScheduleInfoLog.class);
	
	public static void info(String msg){
		log.info(msg);
	}
	
	public static void error(String msg,Exception e){
		log.error(msg,e);
	}
	
}
