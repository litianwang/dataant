/**
 * This file created at 2012-8-17.
 *
 * Copyright (c) 2002-2012 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.schedule.timer;


/**
 * <code>{@link SchedulerManagerTimeThread}</code>
 *
 *
 * @author litianwang
 */
public class SchedulerManagerTimeThread implements TimeThread{

	private BaseScheduleManager manager;
	
	public SchedulerManagerTimeThread(BaseScheduleManager manager){
		this.manager = manager;
	}

	public void run() {
		
		// 开始
		try {
			/**
			 * 采用同步的方式调用
			 */
			if(manager.tryLock()){
				try{
					manager.start();
				} finally{
					manager.unlock();
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	public TimeThread newThread() {
		// TODO 自动生成方法存根
		return new SchedulerManagerTimeThread(manager);
	}
}
