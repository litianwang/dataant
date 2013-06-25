package com.voson.dataant.common.schedule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.voson.dataant.common.schedule.timer.SchedulerManagerTimeThread;
import com.voson.dataant.common.schedule.timer.TimerManager;
import com.voson.dataant.firstanalyze.schedule.FirstAnalyzeSchedule;
import com.voson.dataant.search.schedule.SearchSchedule;



public class WebInitial extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(WebInitial.class);
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		
		firstAnalyze();
		search();
	}

	/**
	 * 
	 */
	private void firstAnalyze() {
		TimerManager timerManager = TimerManager.getInstance();
		ApplicationContext ac2 = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		FirstAnalyzeSchedule firstAnalyzeSchedule = ac2.getBean("firstAnalyzeSchedule", FirstAnalyzeSchedule.class);
		logger.info("启动定时任务调度器！");
		timerManager.createTimer(firstAnalyzeSchedule.getClass().toString(), 
				1000 * 30, 
				1000 * 60 * 3, 
				new SchedulerManagerTimeThread(firstAnalyzeSchedule));
	}
	
	/**
	 * 
	 */
	private void search() {
		TimerManager timerManager = TimerManager.getInstance();
		ApplicationContext ac2 = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		SearchSchedule searchSchedule = ac2.getBean("searchSchedule", SearchSchedule.class);
		logger.info("启动定时任务调度器！");
		timerManager.createTimer(searchSchedule.getClass().toString(), 
				1000 * 30, 
				1000 * 60, 
				new SchedulerManagerTimeThread(searchSchedule));
	}
}
