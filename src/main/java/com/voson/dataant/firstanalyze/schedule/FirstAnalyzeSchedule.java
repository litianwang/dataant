/**
 * This file created at 2013-6-7.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.firstanalyze.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.voson.dataant.common.Task.hive.HiveTask;
import com.voson.dataant.common.Task.tool.HdfsDirCheckTask;
import com.voson.dataant.common.Task.tool.ShellTask;
import com.voson.dataant.common.schedule.timer.BaseScheduleManager;
import com.voson.dataant.common.util.Environment;
import com.voson.dataant.firstanalyze.dao.FirstAnalyzeRuleDao;
import com.voson.dataant.firstanalyze.dao.FirstAnalyzeRuleHistoryDao;
import com.voson.dataant.firstanalyze.model.FirstAnalyzeRule;
import com.voson.dataant.firstanalyze.model.FirstAnalyzeRuleHistory;

/**
 * <code>{@link FirstAnalyzeSchedule}</code>
 *
 *
 * @author tivan
 */
//Spring Service Bean的标识.
@Component
public class FirstAnalyzeSchedule extends BaseScheduleManager{
	
	@Autowired
	private FirstAnalyzeRuleDao firstAnalyzeRuleDao;
	
	@Autowired
	private FirstAnalyzeRuleHistoryDao firstAnalyzeRuleHistoryDao;
	
	private ThreadPoolExecutor sqlldrImportPool = null;
	
	private final static int sqlldrMaxQueue = 20;
	
	private long seq = 0;

	/* (non-Javadoc)
	 * @see com.voson.common.schedule.timer.IScheduleManager#init()
	 */
	@Override
	@PostConstruct
	public void init() {
		sqlldrImportPool = new ThreadPoolExecutor(6, 6, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	/* (non-Javadoc)
	 * @see com.voson.common.schedule.timer.IScheduleManager#start()
	 */
	@Override
	public void start() {
		/**
		 * 处理方式说明：
		 * 1、将未处理的一批数据移动到处理中的目录中。
		 * 2、对这批数据进行规则处理（当前所有生效的规则，安装优先级排序处理）
		 * 3、当所有的规则处理完毕之后，将处理这批数据移动到已经处理的目录下
		 * 4、将这批数据的文件文件，移动到响应的分区下（根据文件的名称中的日期和小时）。
		 */
		System.out.println("hashcode：" + this.hashCode() + " time:" + new Date());
		// url
		Map<String,String> urlConfig = getUrlConfig();
		// app
		Map<String,String> appConfig = getAppConfig();
		if(null == urlConfig || null == appConfig){
			return;
		}
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String baseWorkPath = Environment.getFirstAnalyzePath() + File.separator + date ;
		boolean isEmpty = checkExistData(baseWorkPath, urlConfig, appConfig);
		if(isEmpty){
			System.out.println("目录空！");
			return ;
		}
		//===================
		// 移动数据
		String taskSeq = getNextSEQ();
		this.preProcess(baseWorkPath, date, taskSeq, urlConfig, appConfig);
		
		int exitCode = 0;
		
		//===================
		// 处理规则运算
		List<FirstAnalyzeRule> rules = firstAnalyzeRuleDao.findActiveFirstAnalyzeRule();
		for (FirstAnalyzeRule rule : rules) {
			
			Date currentTime =  new Date();
			
			FirstAnalyzeRuleHistory history = new FirstAnalyzeRuleHistory();
			history.setStartTime(currentTime);
			history.setStatus("running");
			history.setRunDate(date);
			history.setRuleId(rule.getId());
			history.setRuleCode(rule.getRuleCode());
			history.setSeq(Long.valueOf(taskSeq));
			history.setTriggerType(1);
			this.updateRuleHistory(history);
			
			// 执行规则，并修改规则的状态=执行中
			this.updateRuleToRuning(currentTime.getTime(), rule.getId());
			String workPath = baseWorkPath + File.separator + rule.getRuleCode();
			String taskId = rule.getRuleCode() + "_" + date + "_" + taskSeq;
			System.out.println("runing task：" + taskId);
			exitCode = -999;
			try {
				File file=new File(workPath);
				if(!file.exists()){
					file.mkdirs();
				}
				exitCode = analyzeUrl(rule, workPath, taskId, history, urlConfig);
				if(0 == exitCode){
					exitCode = analyzeApp(rule, workPath, taskId, history, appConfig);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(0 == exitCode){
				this.updateRuleToFinish(currentTime.getTime(), rule.getId(), "运行任务成功：" + taskId);
				history.setWorkDir(workPath);
				history.setResultDir(Environment.getFirstAnalyzeResultPath());
				history.setEndTime(new Date());
				history.setStatus("success");
				this.updateRuleHistory(history);
			} else{
				this.updateRuleToFinish(currentTime.getTime(), rule.getId(), "运行任务失败：" + taskId);
				history.setWorkDir(workPath);
				history.setResultDir(Environment.getFirstAnalyzeResultPath());
				history.setEndTime(new Date());
				history.setStatus("failed");
				this.updateRuleHistory(history);
			}
			//=============================
			// 分析结果入oracle库【异步】
			long st = System.nanoTime();
			this.doSqlldrImportAsynch(baseWorkPath, urlConfig, appConfig);
			System.out.println(System.nanoTime() - st + " 纳秒(ns)");
		}
		// ======================
		// 数据移动到已经处理目录
		this.postProcess(baseWorkPath, date, taskSeq, urlConfig, appConfig);
		
		// ============================
		// 入分区表
		this.moveProcessedDataToPartition(baseWorkPath, urlConfig, appConfig);
		
		
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(System.nanoTime());
	}
	
	/**
	 * 一次分析结果入oracle库【异步】<br/>
	 * 采用异步线程进行处理，不等待sqlldr的处理结果<br/>
	 * @param baseWorkPath
	 * @param urlConfig
	 * @param appConfig
	 */
	private void doSqlldrImportAsynch(final String baseWorkPath,
			final Map<String,String> urlConfig, 
			final Map<String,String> appConfig){
		// 如果超过最大任务队列，则停止任务进入队列，
		// 任务由以后的任务触发
		if(sqlldrImportPool.getQueue().size() > sqlldrMaxQueue){
			return;
		}
		sqlldrImportPool.execute(new Runnable() {
			@Override
			public void run() {
				try{
					ShellTask urlImportTask = new ShellTask(urlConfig.get("result.import.shell"), baseWorkPath);
					urlImportTask.run();
					ShellTask appImportTask = new ShellTask(appConfig.get("result.import.shell"), baseWorkPath);
					appImportTask.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	/**
	 * 将已经处理的数据移动到，分区表
	 * @return
	 */
	private int moveProcessedDataToPartition(String baseWorkPath,
			Map<String,String> urlConfig, 
			Map<String,String> appConfig){
		int ret = 0;
		try{
			ShellTask urlCheckTask = new ShellTask(urlConfig.get("partition.shell"), baseWorkPath);
			ret = urlCheckTask.run();
			ShellTask appCheckTask = new ShellTask(appConfig.get("partition.shell"), baseWorkPath);
			ret = appCheckTask.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	
	

	/**
	 * 判断是否存在未处理的数据
	 * @param baseWorkPath
	 * @return
	 */
	private boolean checkExistData(String baseWorkPath,
			Map<String,String> urlConfig, 
			Map<String,String> appConfig) {
		boolean isEmpty = true;
		try{
			HdfsDirCheckTask urlCheckTask = new HdfsDirCheckTask(urlConfig.get("unprocessed.dir"), baseWorkPath);
			int ret = urlCheckTask.run();
			if(ret == 0){
				System.out.println("url-files:" + urlCheckTask.getHdfsFileNum());
				if(urlCheckTask.getHdfsFileNum() > 0){
					isEmpty = false;
				}
			}
			HdfsDirCheckTask appCheckTask = new HdfsDirCheckTask(appConfig.get("unprocessed.dir"), baseWorkPath);
			ret = appCheckTask.run();
			if(ret == 0){
				System.out.println("app-files:" + appCheckTask.getHdfsFileNum());
				if(appCheckTask.getHdfsFileNum() > 0){
					isEmpty = false;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isEmpty;
	}
	
	/**
	 * 前置移动数据处理
	 * @param baseWorkPath
	 * @param date
	 * @param taskSeq
	 * @param urlConfig
	 * @param appConfig
	 * @return
	 */
	private int preProcess(String baseWorkPath,
			String date,
			String taskSeq,
			Map<String,String> urlConfig, 
			Map<String,String> appConfig){
		String workPath = baseWorkPath + File.separator + "pre";
		String urlPre = urlConfig.get("pre");
		String preTaskId = "PRE_" + date + "_" + taskSeq;
		HiveTask urlMoveTask = new HiveTask(urlPre, preTaskId + "_url", workPath, null);
		int exitCode = -999;
		try {
			exitCode = urlMoveTask.run();
			System.out.println("url-move-exitcode=" + exitCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String appPre = appConfig.get("pre");
		HiveTask appMoveTask = new HiveTask(appPre, preTaskId + "_app", workPath, null);
		try {
			exitCode = appMoveTask.run();
			System.out.println("app-move-exitcode=" + exitCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exitCode;
	}
	
	/**
	 * 后置移动数据处理
	 * @param baseWorkPath
	 * @param date
	 * @param taskSeq
	 * @param urlConfig
	 * @param appConfig
	 * @return
	 */
	private int postProcess(String baseWorkPath,
			String date,
			String taskSeq,
			Map<String,String> urlConfig, 
			Map<String,String> appConfig){
		String workPath = baseWorkPath + File.separator + "post";
		String urlPost = urlConfig.get("post");
		String postTaskId = "POST_" + date + "_" + taskSeq;
		HiveTask urlMoveTask = new HiveTask(urlPost , postTaskId + "_url", workPath, null);
		int exitCode = -999;
		try {
			exitCode = urlMoveTask.run();
			System.out.println("url-move-exitcode=" + exitCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String appPost = appConfig.get("post");
		HiveTask appMoveTask = new HiveTask(appPost , postTaskId + "_app", workPath, null);
		try {
			exitCode = appMoveTask.run();
			System.out.println("app-move-exitcode=" + exitCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exitCode;
	}

	/**
	 * 获取app的处理配置文件
	 */
	private Map<String,String> getAppConfig() {
		URL appXml = this.getClass().getClassLoader().getResource("/first.analyze/first.analyze.app.xml");
		File appConfigFile = new File(appXml.getPath());
		Map<String,String> appConfig = null;
		if(appConfigFile.exists()){
			String configXML;
			try {
				configXML = FileUtils.readFileToString(appConfigFile, "UTF-8");
				appConfig = parseXMLConfig(configXML);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return appConfig;
	}

	/**
	 * 获取url的处理配置文件
	 */
	private Map<String,String> getUrlConfig() {
		URL urlXml = this.getClass().getClassLoader().getResource("/first.analyze/first.analyze.url.xml");
		File urlConfigFile = new File(urlXml.getPath());
		Map<String,String> urlConfig = null;
		if(urlConfigFile.exists()){
			String configXML;
			try {
				configXML = FileUtils.readFileToString(urlConfigFile, "UTF-8");
				urlConfig = parseXMLConfig(configXML);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return urlConfig;
	}
	
	

	/**
	 * @param rule
	 * @param workPath
	 * @param taskId
	 * @param exitCode
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private int analyzeUrl(FirstAnalyzeRule rule, 
			String workPath,
			String taskId, 
			FirstAnalyzeRuleHistory history,
			Map<String,String> config) throws IOException, Exception {
		int exitCode = 0;
		String condition = "";
		String core = config.get("core");
		
		String RULE_URL = rule.getRuleUrl();
		if(StringUtils.isNotBlank(RULE_URL)){
			String[] urls = StringUtils.split(RULE_URL, ",");
			for (String url : urls) {
				condition += " OR HOST_A like '%" + url + "%' ";
			}
		}
		String RULE_KEYWORD = rule.getRuleKeyword();
		if(StringUtils.isNotBlank(RULE_KEYWORD)){
			String[] keyWords = StringUtils.split(RULE_KEYWORD, ",");
			for (String keyword : keyWords) {
				condition += " OR URI_C like '%" + keyword + "%' ";
			}
		}
		if(StringUtils.isBlank(condition)){
			return 0;
		}
		condition = " ( " + condition.substring(3) + " ) ";
		
		
		core = core + " and " + condition;
		core = StringUtils.replace(core, "#TASK_ID#", taskId);
		core = StringUtils.replace(core, "#KEY_WORD#", RULE_KEYWORD);
		core = StringUtils.replace(core, "#RULE_CODE#", rule.getRuleCode());
		
		HiveTask task = new HiveTask(core, taskId + "_url", workPath, Environment.getFirstAnalyzeResultPath());
		exitCode =  task.run();
		history.setUrlNum(Math.min(task.getLineNum(),task.getMaxCnt()));
		return exitCode;
	}
	/**
	 * @param rule
	 * @param workPath
	 * @param taskId
	 * @param exitCode
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	private int analyzeApp(FirstAnalyzeRule rule, String workPath,
			String taskId, 
			FirstAnalyzeRuleHistory history,
			Map<String,String> config) throws IOException, Exception {
		int exitCode =0;
		String core = config.get("core");
		String condition = new String();
		String RULE_APPID = (String)rule.getRuleAppid();
		if(StringUtils.isNotBlank(RULE_APPID)){
			String[] appids = StringUtils.split(RULE_APPID, ",");
			for (String appId : appids) {
				condition += " OR APP_ID= '" + appId + "' ";
			}
		}
		if(StringUtils.isBlank(condition)){
			return exitCode;
		}
		condition = " ( " + condition.substring(3) + " ) ";
		core = core + " and " + condition;
		core = StringUtils.replace(core, "#TASK_ID#", taskId);
		core = StringUtils.replace(core, "#RULE_CODE#", rule.getRuleCode());
		HiveTask task = new HiveTask(core, taskId + "_app", workPath, Environment.getFirstAnalyzeResultPath());
		exitCode =  task.run();
		history.setAppNum(Math.min(task.getLineNum(),task.getMaxCnt()));
		return exitCode;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> parseXMLConfig(String xml) throws Exception{
		Map<String,String> config = new HashMap<String, String>();
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			throw new Exception(e);
		}
		;
		for (Iterator ie = document.getRootElement().elementIterator(); ie.hasNext();) {
	           Element element = (Element) ie.next();
	           config.put(element.getName(), element.getText());
		}
		return config;
		
	}
	
	/**
	 * 计算两个时间的间隔（小时数）
	 * @param currentTime
	 * @param lastRunTime
	 * @return
	 */
	public static int calculateInterval(Date currentTime, Date lastRunTime){
		Long interval = (currentTime.getTime() - lastRunTime.getTime())/(1000 * 60 *60);
		if(interval > 48){
			interval=48L;
		}
		if(interval <= 0){
			interval =0L;
		}
		return  interval.intValue();
	}
	
	/**
	 * 获取创建分区表脚本
	 * @param time
	 * @param interval
	 * @param tableName
	 * @return
	 */
	public static String getAddPartitionScript(Date time, int interval, String tableName){
		StringBuilder sb = new StringBuilder();
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(time); 
		calendar.add(Calendar.HOUR_OF_DAY, -2);
		for (int i = 0; i < interval; i++) {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			sb.append(String.format(" alter table %5$s add IF NOT EXISTS partition (year='%1$s', month='%2$02d', day='%3$02d', hour='%4$02d') location '%1$s/%2$02d/%3$02d/%4$02d';", 
				year, month, day, hour, tableName));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * 获取查询分区条件
	 * @param time
	 * @param interval
	 * @return
	 */
	public static String getQueryPartitionCondition(Date time, int interval){
		StringBuilder sb = new StringBuilder();
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(time); 
		calendar.add(Calendar.HOUR_OF_DAY, -2);
		sb.append(" ( ");
		for (int i = 0; i < interval; i++) {
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			if(i != 0){
				sb.append(" OR ");
			}
			sb.append(String.format("(year = '%1$s' and month='%2$02d' and day='%3$02d' and hour='%4$02d') " ,year, month, day, hour));
			sb.append("\n");
		}
		sb.append(" ) ");
		return sb.toString();
	}

	/**
	 * 更新规则状态为执行中
	 * @param currentTime
	 * @param id
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly = false) 
	public void updateRuleToRuning(final long currentTime, final long id){
		FirstAnalyzeRule rule = firstAnalyzeRuleDao.findOne(id);
		rule.setLastRunTime(new Date(currentTime));
		rule.setRunStatus(3L);
		firstAnalyzeRuleDao.save(rule);
		
	}
	
	/**
	 * 更新规则状态为执行中
	 * @param currentTime
	 * @param id
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly = false) 
	public void updateRuleToFinish(final long currentTime, final long id, final String msg){
		FirstAnalyzeRule rule = firstAnalyzeRuleDao.findOne(id);
		rule.setLastRunTime(new Date(currentTime));
		rule.setRunStatus(1L);
		rule.setCostTime(((System.currentTimeMillis() - currentTime)/1000));
		rule.setRemarks(msg);
		firstAnalyzeRuleDao.save(rule);
	}
	
	/**
	 * 更新规则状态为执行中
	 * @param currentTime
	 * @param id
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW,readOnly = false) 
	public void updateRuleHistory(final FirstAnalyzeRuleHistory history){
		firstAnalyzeRuleHistoryDao.save(history);
	}
	
	public String getNextSEQ(){
		seq = firstAnalyzeRuleDao.getTaskIdSeq().longValue();
		if(seq > 9999){
			seq = 1;
		}
		if(seq < 10){
			return "000" + seq;
		} else if(seq < 100){
			return "00" + seq;
		} else if(seq < 1000){
			return "0" + seq;
		} else {
			return "" + seq;
		}
	}	
	
	public void test(String workDir, String cmd){

		/**
		 * 测试代码
		 */
		Process process;
		ProcessBuilder builder = new ProcessBuilder(partitionCommandLine(cmd));
		builder.directory(new File("/home/hadoop/dgch/dataant/work"));
		try {
			process=builder.start();
		
			final InputStream inputStream = process.getInputStream();
			final InputStream errorStream = process.getErrorStream();
			
			String threadName= "test=";
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
						String line;
						while((line=reader.readLine())!=null){
							System.out.println("std##" + line);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			},threadName).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						BufferedReader reader=new BufferedReader(new InputStreamReader(errorStream));
						String line;
						while((line=reader.readLine())!=null){
							System.out.println("err##" + line);
							
						}
					} catch (Exception e) {
							e.printStackTrace();
						}
				}
			},threadName).start();
			int exitCode = -999;
			try {
				exitCode = process.waitFor();
			} catch (InterruptedException e) {
				// log(e);
			} finally{
				process=null;
			}
			if(exitCode!=0){
				System.out.println("exitCode=" + exitCode);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static String[] partitionCommandLine(String command) {
		
		ArrayList<String> commands = new ArrayList<String>();
		
		String os=System.getProperties().getProperty("os.name");
		if(os!=null && (os.startsWith("win") || os.startsWith("Win"))){
			commands.add("CMD.EXE");
			commands.add("/C");
			commands.add(command);
		}else{
			int index = 0;

	        StringBuffer buffer = new StringBuffer(command.length());

	        boolean isApos = false;
	        boolean isQuote = false;
	        while(index < command.length()) {
	            char c = command.charAt(index);

	            switch(c) {
	                case ' ':
	                    if(!isQuote && !isApos) {
	                        String arg = buffer.toString();
	                        buffer = new StringBuffer(command.length() - index);
	                        if(arg.length() > 0) {
	                            commands.add(arg);
	                        }
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                case '\'':
	                    if(!isQuote) {
	                        isApos = !isApos;
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                case '"':
	                    if(!isApos) {
	                        isQuote = !isQuote;
	                    } else {
	                        buffer.append(c);
	                    }
	                    break;
	                default:
	                    buffer.append(c);
	            }

	            index++;
	        }

	        if(buffer.length() > 0) {
	            String arg = buffer.toString();
	            commands.add(arg);
	        }
		}
        return commands.toArray(new String[commands.size()]);
	}
}
