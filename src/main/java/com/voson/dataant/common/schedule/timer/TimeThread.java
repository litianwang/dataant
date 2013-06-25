/**
 * This file created at 2012-8-17.
 *
 * Copyright (c) 2002-2012 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.schedule.timer;

/**
 * <code>{@link TimeThread}</code>
 *
 * TODO : document me
 *
 * @author litianwang
 */
public interface TimeThread extends Runnable{
	/**
	 * 创建一个多线程类
	 * @return
	 */
	public TimeThread newThread();
}
