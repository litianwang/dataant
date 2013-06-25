/**
 * This file created at 2013-6-8.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * <code>{@link Environment}</code>
 *
 * 环境类
 * 用于判断当前是哪个环境
 * 在spring中进行设置
 *
 * @author litianwang
 */
public class Environment {
	
	private static Properties p = new Properties();
	
	/**
	 * 执行job需要用到的路径
	 */
	private static String firstAnalyzePath = "/data/first-analyze";
	
	private static String firstAnalyzeResultPath = "/data/first-analyze-result";
	
	
	static{
		InputStream in = Environment.class.getClassLoader().getResourceAsStream("/env.properties");
		try {
			p.load(new InputStreamReader(in, "UTF-8"));
			firstAnalyzePath = p.getProperty("first.analyze.work.path");
			System.out.println(firstAnalyzePath);
			firstAnalyzeResultPath = p.getProperty("first.analyze.result.path");
			System.out.println(firstAnalyzeResultPath);
			File file=new File(firstAnalyzePath);
			if(!file.exists()){
				file.mkdirs();
			}
			file=new File(firstAnalyzeResultPath);
			if(!file.exists()){
				file.mkdirs();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getFirstAnalyzePath() {
		return firstAnalyzePath;
	}

	public static String getFirstAnalyzeResultPath() {
		return firstAnalyzeResultPath;
	}
	
	public static String getEnvConfigStirng(String key){
		return p.getProperty(key);
	}
	
	

	
	
}
