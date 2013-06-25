/**
 * This file created at 2013-6-17.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.search.schedule;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.voson.dataant.common.Task.hive.HiveTask;
import com.voson.dataant.common.Task.tool.ShellTask;
import com.voson.dataant.common.schedule.timer.BaseScheduleManager;
import com.voson.dataant.common.util.EnvironmentFirstAnalyze;
import com.voson.dataant.common.util.HiveQLlParserUtils;
import com.voson.dataant.search.dao.SearchAppDao;
import com.voson.dataant.search.dao.SearchUrlDao;
import com.voson.dataant.search.model.SearchApp;
import com.voson.dataant.search.model.SearchUrl;

/**
 * <code>{@link SearchSchedule}</code>
 *
 * 详单查询
 *
 * @author litianwang
 */
@Component
public class SearchSchedule extends BaseScheduleManager{
	
	String workDir = "";
	String resultDir = "";
	
	@Autowired
	private SearchAppDao searchAppDao;
	@Autowired
	private SearchUrlDao searchUrlDao;
	

	
	private ThreadPoolExecutor sqlldrImportPool = null;
	
	private final static int sqlldrMaxQueue = 20;
	
	@Override
	@PostConstruct
	public void init() {
		sqlldrImportPool = new ThreadPoolExecutor(4, 4, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		
		workDir = EnvironmentFirstAnalyze.getEnvConfigStirng("search.work.path");
		resultDir = EnvironmentFirstAnalyze.getEnvConfigStirng("search.result.path");
		File file=new File(workDir);
		if(!file.exists()){
			file.mkdirs();
		}
		file=new File(resultDir);
		if(!file.exists()){
			file.mkdirs();
		}
	}	
	

	@Override
	public void start() {
		// 1、取得所有有效的url查询
		List<SearchUrl> urls = searchUrlDao.findActiveSearchUrl();
		if(null != urls){
			String urlworkDir = workDir + File.separator + "searchurl";
			String urlresultDir = resultDir + File.separator + "searchurl";
			String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
			Map<String,String> config = getSearchUrlConfig();
			for (SearchUrl searchUrl : urls) {
				urlworkDir = urlworkDir + File.separator + date + File.separator + searchUrl.getId();
				if(null == config){
					
				}
				String queryHql = config.get("core");
				Map<String,String> params = new HashMap<String, String>();
				params.put("SEARCH_ID", searchUrl.getId().toString());
				params.put("USER_NO", searchUrl.getUserNo());
				params.put("HOST_A", searchUrl.getHostA());
				params.put("URI_C", searchUrl.getUriC());
				if(null != searchUrl.getUrlClassId()){
					params.put("URL_CLASS_ID", String.valueOf(searchUrl.getUrlClassId()));
				}
				if(null != searchUrl.getUrlInterestId()){
					params.put("URL_INTEREST_ID", String.valueOf(searchUrl.getUrlInterestId()));
				}
				if(null != searchUrl.getPartitionId() && searchUrl.getPartitionId() > 10000000){
					params.put("YEAR", String.valueOf(searchUrl.getPartitionId()).substring(0,4));
					params.put("MONTH", String.valueOf(searchUrl.getPartitionId()).substring(4,6));
					params.put("DAY", String.valueOf(searchUrl.getPartitionId()).substring(6,8));
				}
				
				queryHql = HiveQLlParserUtils.paser(queryHql, params);
				System.out.println(queryHql);
				
				// 执行规则，并修改规则的状态=执行中
				this.updateSearchUrlToRuning(searchUrl.getId(), "运行中");
				
				HiveTask hiveTask = new HiveTask(queryHql , "search_url_" + searchUrl.getId(), urlworkDir, urlresultDir);
				searchUrl.getMaxCnt();
				if(null != searchUrl.getMaxCnt() && searchUrl.getMaxCnt() > 0){
					hiveTask.setMaxCnt(searchUrl.getMaxCnt());
				}
				int exitCode = -999;
				try {
					exitCode = hiveTask.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(0 == exitCode){
					this.updateSearchUrlToFinish(searchUrl.getId(), "运行任务成功：");
					this.doSqlldrImportAsynch(urlresultDir, config);
				} else{
					this.updateSearchUrlToFinish(searchUrl.getId(), "运行任务失败：");
				}
			}
		}
		
		// 2、取得所有的有效的App查询
		List<SearchApp> apps = searchAppDao.findActiveSearchApp();
		if(null != apps){
			String appWorkDir = workDir + File.separator + "searchapp";
			String appResultDir = resultDir + File.separator + "searchapp";
			String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
			Map<String,String> config = getSearchAppConfig();
			for (SearchApp searchApp : apps) {
				if(null == config){
					
				}
				appWorkDir = appWorkDir + File.separator + date + File.separator + searchApp.getId();
				String queryHql = config.get("core");
				Map<String,String> params = new HashMap<String, String>();
				params.put("SEARCH_ID", searchApp.getId().toString());
				params.put("USER_NO", searchApp.getUserNo());
				if(null !=  searchApp.getAppId()){
					params.put("APP_ID", String.valueOf(searchApp.getAppId()));
				}
				if(null != searchApp.getPartitionId() && searchApp.getPartitionId() > 10000000){
					params.put("YEAR", String.valueOf(searchApp.getPartitionId()).substring(0,4));
					params.put("MONTH", String.valueOf(searchApp.getPartitionId()).substring(4,6));
					params.put("DAY", String.valueOf(searchApp.getPartitionId()).substring(6,8));
				}
				
				queryHql = HiveQLlParserUtils.paser(queryHql, params);
				// 执行规则，并修改规则的状态=执行中
				this.updateSearchAppToRuning(searchApp.getId(), "运行中");
				
				HiveTask hiveTask = new HiveTask(queryHql, "search_app_" + searchApp.getId(), appWorkDir, appResultDir);
				if(null != searchApp.getMaxCnt() && searchApp.getMaxCnt() > 0){
					hiveTask.setMaxCnt(searchApp.getMaxCnt());
				}
				int exitCode = -999;
				try {
					exitCode = hiveTask.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(0 == exitCode){
					this.updateSearchAppToFinish(searchApp.getId(), "运行任务成功：");
					this.doSqlldrImportAsynch(appResultDir, config);
				} else{
					this.updateSearchAppToFinish(searchApp.getId(), "运行任务失败：");
				}
			}
			
		}
	}
	
	/**
	 * 结果入oracle库【异步】<br/>
	 * 采用异步线程进行处理，不等待sqlldr的处理结果<br/>
	 * @param workPath
	 * @param config
	 */
	private void doSqlldrImportAsynch(final String workPath,
			final Map<String,String> urlConfig){
		// 如果超过最大任务队列，则停止任务进入队列，
		// 任务由以后的任务触发
		if(sqlldrImportPool.getQueue().size() > sqlldrMaxQueue){
			return;
		}
		sqlldrImportPool.execute(new Runnable() {
			@Override
			public void run() {
				try{
					ShellTask sqlldrImportTask = new ShellTask(urlConfig.get("result.import.shell"), workPath);
					sqlldrImportTask.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 更新状态为执行中
	 * @param currentTime
	 * @param id
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly = false) 
	public void updateSearchAppToRuning(final long id, final String msg){
		SearchApp searchApp = searchAppDao.findOne(id);
		// [1:待运行,2:运行中,3:结束,4:无效]
		searchApp.setStatus(2);
		searchAppDao.save(searchApp);
	}
	
	/**
	 * 更新状态为执行中
	 * @param currentTime
	 * @param id
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly = false) 
	public void updateSearchAppToFinish(final long id, final String msg){
		SearchApp searchApp = searchAppDao.findOne(id);
		// [1:待运行,2:运行中,3:结束,4:无效]
		searchApp.setStatus(3);
		searchAppDao.save(searchApp);
	}
	
	/**
	 * 更新状态为执行中
	 * @param currentTime
	 * @param id
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly = false) 
	public void updateSearchUrlToRuning(final long id, final String msg){
		SearchUrl searchUrl = searchUrlDao.findOne(id);
		// [1:待运行,2:运行中,3:结束,4:无效]
		searchUrl.setStatus("2");
		searchUrlDao.save(searchUrl);
	}
	
	/**
	 * 更新状态为执行中
	 * @param currentTime
	 * @param id
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly = false) 
	public void updateSearchUrlToFinish(final long id, final String msg){
		SearchUrl searchUrl = searchUrlDao.findOne(id);
		// [1:待运行,2:运行中,3:结束,4:无效]
		searchUrl.setStatus("3");
		searchUrlDao.save(searchUrl);
	}
	
	private Map<String,String> getSearchAppConfig(){
		URL urlxml = this.getClass().getClassLoader().getResource("/search/search.app.xml");
		File configF = new File(urlxml.getPath());
		if(configF.exists()){
			try {
				return parseXMLConfig(configF);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Map<String,String> getSearchUrlConfig(){
		URL urlxml = this.getClass().getClassLoader().getResource("/search/search.url.xml");
		File configF = new File(urlxml.getPath());
		if(configF.exists()){
			try {
				return parseXMLConfig(configF);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 解析查询配置文件
	 * @param xmlFile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> parseXMLConfig(File xmlFile) throws Exception{
		String configXML = FileUtils.readFileToString(xmlFile, "UTF-8");
		Map<String,String> config = new HashMap<String, String>();
		Document document = null;
		try {
			document = DocumentHelper.parseText(configXML);
		} catch (DocumentException e) {
			throw new Exception(e);
		}
		for (Iterator ie = document.getRootElement().elementIterator(); ie.hasNext();) {
	           Element element = (Element) ie.next();
	           config.put(element.getName(), element.getText());
		}
		return config;
		
	}

}
