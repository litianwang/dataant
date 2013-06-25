/**
 * This file created at 2012-8-19.
 *
 * Copyright (c) 2002-2012 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.schedule.timer;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <code>{@link BaseScheduleManager}</code>
 *
 * 调度器接口
 *
 * @author litianwang
 */
public abstract class BaseScheduleManager {
	
	protected final ReentrantLock lock = new ReentrantLock();

	/**
	 * 初始化方法
	 */
	public abstract void  init();
	
	/**
	 * 开始调度
	 */
	public abstract void start();
	
	public  boolean tryLock(){
		return lock.tryLock();
	}
	
	public void unlock(){
		lock.unlock();
	}
}
