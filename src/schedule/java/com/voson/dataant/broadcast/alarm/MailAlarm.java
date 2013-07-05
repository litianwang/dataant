package com.voson.dataant.broadcast.alarm;

import java.util.List;

public class MailAlarm extends AbstractDataantAlarm{

	@Override
	public void alarm(List<String> users, String title, String content)
			throws Exception {
		//TODO to be implements
		System.out.println("MailAlarm:" + title + "$$#" + content);
	}
}
