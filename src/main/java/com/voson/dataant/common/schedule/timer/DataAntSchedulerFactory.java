/**
 * This file created at 2013-2-7.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.schedule.timer;

import java.util.List;

/**
 * <code>{@link DataAntSchedulerFactory}</code>
 *
 *定时调度管理器工厂类<br/>
 *通过注入调度管理器，可以自动调度任务执行
 *
 * @author litianwang
 */
public class DataAntSchedulerFactory {
	
	List<BaseScheduleManager> scheduleManagers;

	public List<BaseScheduleManager> getScheduleManagers() {
		return scheduleManagers;
	}

	public void setScheduleManagers(List<BaseScheduleManager> scheduleManagers) {
		this.scheduleManagers = scheduleManagers;
	}
	
}
