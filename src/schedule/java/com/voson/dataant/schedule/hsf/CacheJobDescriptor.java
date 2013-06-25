package com.voson.dataant.schedule.hsf;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voson.dataant.model.JobDescriptor;
import com.voson.dataant.model.JobStatus;
import com.voson.dataant.store.GroupManager;
import com.voson.dataant.util.Tuple;

public class CacheJobDescriptor {
	private static Logger log=LoggerFactory.getLogger(CacheJobDescriptor.class);
	private GroupManager groupManager;
	
	private final String jobId;
	private  JobDescriptor jobDescriptor;
	private Date lastTime=new Date();
	
	public CacheJobDescriptor(String jobId,GroupManager groupManager){
		this.jobId=jobId;
		this.groupManager=groupManager;
	}
	

	public JobDescriptor getJobDescriptor() {
		if(jobDescriptor==null || System.currentTimeMillis()-lastTime.getTime()>60*1000L){
			try {
				Tuple<JobDescriptor, JobStatus> job=groupManager.getJobDescriptor(jobId);
				if(job!=null){
					jobDescriptor=job.getX();
				}else{
					jobDescriptor=null;
				}
				lastTime=new Date();
			} catch (Exception e) {
				log.error("load job descriptor fail",e);
			}
		}
		return jobDescriptor;
	}
	
	public void refresh(){
		Tuple<JobDescriptor, JobStatus> job=groupManager.getJobDescriptor(jobId);
		if(job!=null){
			jobDescriptor=job.getX();
		}else{
			jobDescriptor=null;
		}
	}
		
}
