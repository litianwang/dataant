/**
 * This file created at 2012-8-17.
 *
 * Copyright (c) 2002-2012 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.schedule.timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <code>{@link RunTimer}</code>
 *
 * TODO : document me
 *
 * @author litianwang
 */
public class RunTimer {

	 public RunTimer(){}
	private Timer timer;
	private long delay; // 延迟多久执行
	private long pollTimer = 6000;//默认为1分钟
	private TimeThread thread ;
	//	是否中止的标志
	private boolean IsStop = false;
	
	/**
	 * @param delay 延迟多久启动(毫秒)
	 * @param polltimer 时间间隔(毫秒)
	 * @param classname 要操作的类名
	 * @param method 要操作的方法名
	 */
	RunTimer(long delay, long polltimer,TimeThread thread){
		 timer = new Timer();
		 pollTimer = polltimer ; 
		 this.thread = thread;
	}
	
	
	/**
	 *
	 * 以当前时间开始，每隔 pollTimer 毫秒就启动计划任务 
	 */
	public void start() 
	{ 
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run() {	
				scan();
			}
			public void scan()
			{
				 
//				是否中止
				if (IsStop)  {
					timer.cancel();
					return ;
				}
				
				try
				{
					
					new Thread(thread.newThread()).start(); 
					
				}
				catch(Exception m)
				{
//					出错就中断
					System.out.println("处理定时查询出错");
					timer.cancel();
				}

			}
			},delay,pollTimer);
	}

	/**
	 * 停止定时监控
	 */
	public void stopIt()
	{
		IsStop = true;
	}
}
