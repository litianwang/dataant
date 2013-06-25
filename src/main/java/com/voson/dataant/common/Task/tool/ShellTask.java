/**
 * This file created at 2013-6-8.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.Task.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voson.dataant.common.Task.ProcessTask;

/**
 * <code>{@link ShellTask}</code>
 * 
 *运行shell文件
 *
 * @author litianwang
 */
public class ShellTask extends ProcessTask {
	private static Logger log = LoggerFactory.getLogger(ShellTask.class);
	private String shellFile;
	private String workDir;
	
	public ShellTask(String shellFile, String workDir){
		this.shellFile = shellFile;
		this.workDir =workDir;
		File file = new File(workDir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	@Override
	@SuppressWarnings("unused")
	public Integer run() throws Exception {
		String os=System.getProperties().getProperty("os.name");
		if(os!=null && (os.startsWith("win") || os.startsWith("Win"))){
			// windows 系统开发测试，不运行
			// TODO:delete
			return 0;
		} else {
			
		}
		String command = "sh " + shellFile;
		ProcessBuilder builder = new ProcessBuilder(partitionCommandLine(command));
		builder.directory(new File(workDir));
		// builder.environment().putAll(envMap);
		process=builder.start();
		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();
		
		String threadName= "task=runDataAntShell";
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
					String line;
					while((line=reader.readLine())!=null){
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
			// log(e);
		} finally{
			process=null;
		}
		if(exitCode!=0){
			return exitCode;
		}
		return 0;
	}

}
