/**
 * This file created at 2012-8-17.
 *
 * Copyright (c) 2002-2012 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.schedule.timer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <code>{@link TimerManager}</code>
 *
 *
 * @author litianwang
 */
public class TimerManager {

	//public static int count =0; 
	/**
	 * 线程方法
	 */
	private TimerManager(){
	}
	private  Map timers = new HashMap();
	private static TimerManager timerM = new TimerManager();
	public static TimerManager getInstance()
	{
		return timerM;
	}
	/**
	 * 创建一个新的定时器，并运行。
	 * @param timerName
	 * @param delay 延迟多久启动(毫秒)
	 * @param pollTime 时间间隔(毫秒)
	 * @param thread
	 * @return
	 */
	public boolean createTimer(String timerName,long delay, long pollTime, TimeThread thread)
	{
		RunTimer timer = new RunTimer(delay, pollTime,thread);
		timers.put(timerName,timer);
		timer.start();
		return true;
	}
	/**
	 * 停止所有的定时器
	 *
	 */
	public void stopAll()
	{
		Collection arr = timers.values();
		for(Iterator it=arr.iterator();it.hasNext();)
		{
			RunTimer timer = (RunTimer)it.next();
			timer.stopIt();
		}
	}
	/**
	 * 启动所有的定时器
	 *
	 */
	public void startAll()
	{
		Collection arr = timers.values();
		for(Iterator it=arr.iterator();it.hasNext();)
		{
			RunTimer timer = (RunTimer)it.next();
			timer.start();
		}
	}
	public boolean stop(String timerName)
	{
		RunTimer timer = (RunTimer)timers.get(timerName);
		if(timer == null)
			return false;
		timer.stopIt();
		return true;
	}
	public boolean start(String timerName)
	{
		RunTimer timer = (RunTimer)timers.get(timerName);
		if(timer == null)
			return false;
		timer.start();
		return true;
	}
	public static void main(String args[]){
		
	   
	}
}
