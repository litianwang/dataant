/**
 * This file created at 2013-6-17.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * <code>{@link HiveQLlParserUtils}</code>
 *
 * TODO : document me
 *
 * @author litianwang
 */
public class HiveQLlParserUtils {
	
	
	public static String paser(String hiveQL, Map<String,String> params){
		
		if(hiveQL==null){
			return null;
		}
		Map<String, String> newparams=new HashMap<String, String>();
		for(String key:params.keySet()){
			if(params.get(key)!=null){
				newparams.put("#"+key+"#", params.get(key));
			}
		}
		for(String key:newparams.keySet()){
			String old="";
			do{
				old=hiveQL;
				hiveQL=hiveQL.replace(key, newparams.get(key));
			}while(!old.equals(hiveQL));
		}
		int maxPosition = hiveQL.length();
		int position =-1;
		StringBuilder mainStr = new StringBuilder();
		StringBuilder subStr = new StringBuilder();
		boolean isExistStart = false;
		while ((position+1) < maxPosition){
			position++;
			if(hiveQL.charAt(position) == '{'){
				if(hiveQL.charAt(position+1) == '?'){
					mainStr.append(subStr.toString());
					subStr.setLength(0);
					isExistStart = true;
					position++;
					continue;
				}
			}
			if(hiveQL.charAt(position) == '}'){
				if(isExistStart){
					int paramIndex = subStr.toString().indexOf("#");
					if(paramIndex > 0){
						subStr.setLength(0);
					}
					mainStr.append(subStr.toString());
					isExistStart = false;
					subStr.setLength(0);
					continue;
				}
			}
			subStr.append(hiveQL.charAt(position));
		}
		mainStr.append(subStr.toString());
		return mainStr.toString();
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(String.valueOf(null));
		String hiveQL = FileUtils.readFileToString(new File("E:/项目/东莞网分/workspace/dataant/src/main/java/com/voson/dataant/common/util/HiveQLTest.sql"));
		Map<String,String> params = new HashMap<String, String>();
		params.put("TASK_ID", "123456ddd");
		String aa = paser(hiveQL, params);
		System.out.println(aa);
	}

}
