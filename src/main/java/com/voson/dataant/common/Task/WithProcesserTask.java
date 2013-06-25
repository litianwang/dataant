/**
 * This file created at 2013-6-24.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.Task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <code>{@link WithProcesserTask}</code>
 *
 * TODO : document me
 *
 * @author litianwang
 */
public class WithProcesserTask extends ProcessTask {
	
	private static Logger logger = LoggerFactory.getLogger(WithProcesserTask.class);
	private List<Task> pres;
	private List<Task> posts;
	private Task running;
	private Task task;
	
	
	@Override
	public Integer run() throws Exception {
		//前置任务执行
		Integer preExitCode=-1;
		for(Task task:pres){
			if(isCanceled()){
				break;
			}
			try {
				running=task;
				log("开始执行前置处理单元："+ task.getClass().getSimpleName());
				preExitCode=task.run();
			} catch (Exception e) {
				log(e);
			} finally{
				log("前置处理单元："+task.getClass().getSimpleName()+" 处理完毕");
				running=null;
			}
		}
		//核心任务执行
		Integer exitCode=-1;
		try {
			if(!isCanceled()){
				log("开始执行核心Job任务");
				running=task;
				exitCode=task.run();
			}
		} catch (Exception e) {
			log(e);
		} finally{
			log("核心Job任务处理完毕");
			running=null;
		}
		//后置任务执行
		Integer postExitCode=-1;
		for(Task task:posts){
			if(isCanceled()){
				break;
			}
			try {
				log("开始执行后置处理单元："+task.getClass().getSimpleName());
				running=task;
				postExitCode=task.run();
			} catch (Exception e) {
				log(e);
			} finally{
				log("后置处理单元："+task.getClass().getSimpleName()+"处理完毕");
				running=null;
			}
		}
		
		return exitCode;
	}

	@Override
	public void cancel() {
		// log("开始执行取消任务命令");
		canceled=true;
		if(running!=null){
			running.cancel();
		}
		// log("结束取消任务命令");
	}
	
	protected void log(String log){
		logger.info(log);
	}
	protected void log(Exception e){
		logger.error("", e);
	}
}
