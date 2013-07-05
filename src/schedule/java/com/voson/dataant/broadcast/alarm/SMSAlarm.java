package com.voson.dataant.broadcast.alarm;

import java.util.List;

public class SMSAlarm extends AbstractDataantAlarm{
	@Override
	public void alarm(List<String> uids, String title, String content)
			throws Exception {
		//TODO to be implements
		System.out.println("SMSAlarm:" + title + "$$#" + content);
	}

}
