/**
 * This file created at 2013-6-9.
 *
 * Copyright (c) 2002-2013 Bingosoft, Inc. All rights reserved.
 */
package com.voson.dataant.common.cron;

import java.util.Date;

/**
 * <code>{@link CronTest}</code>
 *
 * TODO : document me
 *
 * @author litianwang
 */
public class CronTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO implement CronTest.main
		CronExpression cron = new CronExpression("0 */5 * * * ? *");
		Date runt = new Date();
		for (int i = 0; i < 10; i++) {
			System.out.println(runt);
			runt = cron.getNextValidTimeAfter(runt);
		}
			
	}

}
