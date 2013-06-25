/**
 * This file created at 2013-6-8.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.Task.hive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voson.dataant.common.Task.ProcessTask;

/**
 * <code>{@link HiveTask}</code>
 *
 * TODO : document me
 *
 * @author litianwang
 */
public class HiveTask extends ProcessTask {
	private static Logger log = LoggerFactory.getLogger(HiveTask.class);
	private String coreScript;
	private String taskId;
	private String workDir;
	private long lineNum = 0;
	private String resultDir;
	// 最大输出数量：默认1000万
	private long maxCnt = 10000000;
	private  OutputStreamWriter hiveLogtWriter = null;
	
	public HiveTask(String coreScript, String taskId, String workDir, String resultDir){
		this.coreScript = coreScript;
		this.taskId =taskId;
		this.workDir =workDir;
		this.resultDir = resultDir;
		File file = new File(workDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		if(StringUtils.isNotBlank(resultDir)){
			file = new File(resultDir);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.voson.common.Task.Task#cancel()
	 */
	@Override
	public void cancel() {

	}

	/* (non-Javadoc)
	 * @see com.voson.common.Task.Task#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.voson.common.Task.Task#run()
	 */
	@Override
	public Integer run() throws Exception {
		
		File hiveLogF = new File(workDir + File.separator+ taskId + ".hive.log");
		if (!hiveLogF.exists()) {
			hiveLogF.createNewFile();
		}
		hiveLogtWriter = new OutputStreamWriter(new FileOutputStream(hiveLogF),Charset.forName("utf-8"));
		int exitCode = 0;
		
		try{
			if(StringUtils.isNotBlank(coreScript)){
				File f = new File(workDir + File.separator + taskId + ".hive");
				if (!f.exists()) {
					f.createNewFile();
				}
				OutputStreamWriter writer = null;
				try {
					writer = new OutputStreamWriter(new FileOutputStream(f),
							Charset.forName("utf-8"));
					writer.write(coreScript.replaceAll("^--.*", ""));
				} catch (Exception e) {
					return -1;
				} finally {
					IOUtils.closeQuietly(writer);
				}
				StringBuffer sb = new StringBuffer();
				sb.append("hive");
				// 引入常用udf函数
				sb.append(" -f ").append(f.getAbsolutePath());
				// 执行shell
				exitCode = this.coreRun(sb.toString());
			}
		} finally {
			IOUtils.closeQuietly(hiveLogtWriter);
		}
		
		return exitCode;
	}

	/**
	 * @param s
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private int coreRun(String s) throws IOException, FileNotFoundException {
		String os=System.getProperties().getProperty("os.name");
		if(os!=null && (os.startsWith("win") || os.startsWith("Win"))){
			// windows 系统开发测试，不运行
			// TODO:delete
			return 0;
		} else {
			
		}
		ProcessBuilder builder = new ProcessBuilder(partitionCommandLine(s));
		builder.directory(new File(workDir));
		// builder.environment().putAll(envMap);
		process=builder.start();
		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();
		
		String threadName= "task=" + taskId;
		File hiveResultF = null;
		if(StringUtils.isNotBlank(resultDir)){
			hiveResultF = new File(resultDir + File.separator+ taskId + ".tmp");
			if (!hiveResultF.exists()) {
				hiveResultF.createNewFile();
			}
		}
		final OutputStreamWriter hiveQueryresultWriter = createResultWrite(hiveResultF);
		lineNum = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
					String line;
					while((line=reader.readLine())!=null){
						lineNum++;
						// 最大输出数量
						if(lineNum <=maxCnt){
							hiveQueryresultWriter.write(line + "\n");
						}
					}
				}catch(Exception e){
					log.error("接收日志出错，推出日志接收");
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
						hiveLogtWriter.write(line + "\n");
						
					}
				} catch (Exception e) {
						log.error("接收日志出错，推出日志接收");
					}
			}
		},threadName).start();
		int exitCode = -999;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			log.error("", e);
		} finally{
			if(StringUtils.isNotBlank(resultDir)){
				IOUtils.closeQuietly(hiveQueryresultWriter);
			}
			process=null;
		}
		if(exitCode!=0){
			return exitCode;
		} else {
			// 重命名结果文件
			if(null != hiveResultF && hiveResultF.exists()){
				if(lineNum == 0){
					hiveResultF.delete();
				} else {
					hiveResultF.renameTo(new File(resultDir + File.separator+ taskId + ".txt"));
				}
			}
		}
		return 0;
	}
	
	public OutputStreamWriter createResultWrite(File hiveResultFile) throws IOException{
		if(null != hiveResultFile){
			return  new OutputStreamWriter(new FileOutputStream(hiveResultFile),Charset.forName("utf-8"));
		} else {
			return  hiveLogtWriter;
		}
	}
	
	public long getMaxCnt() {
		return maxCnt;
	}
	
	public void setMaxCnt(long maxCnt) {
		this.maxCnt = maxCnt;
	}

	public long getLineNum() {
		return lineNum;
	}

	public void setLineNum(long lineNum) {
		this.lineNum = lineNum;
	}

}
